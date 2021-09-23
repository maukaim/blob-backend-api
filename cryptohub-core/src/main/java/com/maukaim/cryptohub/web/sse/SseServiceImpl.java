package com.maukaim.cryptohub.web.sse;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.maukaim.cryptohub.exchange.ExchangeStreamer;
import com.maukaim.cryptohub.plugins.api.market.model.MarketData;
import com.maukaim.cryptohub.plugins.api.order.Order;
import com.maukaim.cryptohub.web.sse.model.SseClient;
import com.maukaim.cryptohub.web.sse.model.SseEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class SseServiceImpl implements SseService, ExchangeStreamer {

    private Map<String, SseClient> sseClients = Maps.newConcurrentMap();

    //Table<R,C,Val> => <WrapperId, SseClientId, Set<SseEventType>>
    private final Table<String, String, Set<SseEventType>> subscriptionsTable = HashBasedTable.create();
    private final Long clientConnectionTimeOut = Duration.ofSeconds(250).toMillis();

    @Override
    public void streamMarketData(String fromId, MarketData message) {
        this.streamObjectForEvent(fromId, message, SseEventType.MARKET_DATA);
    }

    @Override
    public void streamOrderUpdate(String fromId, Order message) {
        this.streamObjectForEvent(fromId, message, SseEventType.ORDER_UPDATE);
    }

    private void streamObjectForEvent(String fromId, Object message, SseEventType eventType) {
        synchronized (subscriptionsTable) {
            Set<String> receiverIds = this.subscriptionsTable.row(fromId).keySet();
            receiverIds.stream()
                    .filter(extUuid -> this.subscriptionsTable.get(fromId, extUuid).contains(eventType))
                    .forEach(extUuid -> this.sseClients.get(extUuid).send(SseEmitter.event()
                            .data(message)
                            .name(eventType.name())));
            ;
        }
    }

    @Override
    public SseClient createClient(String extUuid) {
        this.sseClients.computeIfAbsent(extUuid, (id) -> {
            SseEmitter sseEmitter = new SseEmitter(this.clientConnectionTimeOut);
            sseEmitter.onCompletion(() -> this.removeClient(extUuid));
            sseEmitter.onTimeout(sseEmitter::complete);
            return new SseClient(sseEmitter, extUuid);
        });

        return this.sseClients.get(extUuid);
    }

    @Override
    public SseClient getClient(String extUuid) {
        return this.sseClients.get(extUuid);
    }

    @Override
    public void removeClient(String extUuid) {
        synchronized (this.subscriptionsTable) {
            Set<String> wrapperIds = subscriptionsTable.column(extUuid).keySet();
            wrapperIds.forEach(wrapperId -> this.subscriptionsTable.remove(wrapperId, extUuid));
        }
        SseClient clientRemoved = this.sseClients.remove(extUuid);
        clientRemoved.prepareToDestroy();
    }

    @Override
    public void addSubscriber(String extUuid, String wrapperId, SseEventType event) throws NoSuchSseClientException {
        if (this.sseClients.containsKey(extUuid)) {
            synchronized (this.subscriptionsTable) {
                Set<SseEventType> events = Objects.requireNonNullElseGet(subscriptionsTable.get(wrapperId, extUuid), HashSet::new);
                events.add(event);
                this.subscriptionsTable.put(wrapperId, extUuid, events);
            }
        } else {
            throw new NoSuchSseClientException(
                    String.format("Sse Client [%s] not recognized by server. %s Available are: %s",
                            extUuid,
                            this.sseClients.keySet().size(),
                            String.join(", ", this.sseClients.keySet())
                    ));
        }

    }

    @Override
    public void removeSubscriber(String extUuid, String wrapperId, SseEventType event) {
        if (this.sseClients.containsKey(extUuid)) {
            synchronized (this.subscriptionsTable) {
                Set<SseEventType> eventsSubscribed = subscriptionsTable.get(wrapperId, extUuid);

                if (Objects.nonNull(eventsSubscribed)) {
                    eventsSubscribed.remove(event);

                    if (eventsSubscribed.isEmpty()) {
                        this.subscriptionsTable.remove(wrapperId, extUuid);

                    } else {
                        this.subscriptionsTable.put(wrapperId, extUuid, eventsSubscribed);
                    }
                }
            }
        }
    }
}
