package com.maukaim.blob.core.web.sse.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class SseClient {
    @Getter
    private SseEmitter clientConnector;
    private String clientId;

    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> currentlyScheduledTask;

    private Integer delayMs;
    private final Integer minDelayMs = 500;
    private final Integer maxDelayMs = 3000;

    private ArrayBlockingQueue<SseEventBuilder> messageQueue;

    public SseClient(SseEmitter sseEmitter, String clientId) {
        this.clientConnector = sseEmitter;
        this.clientId = clientId;
        this.delayMs = minDelayMs;
        this.messageQueue = new ArrayBlockingQueue<>(10000, true);
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.start();
    }

    public Integer changePulse(Integer newDelayMs) {
        this.delayMs = Math.min(this.maxDelayMs, Math.max(newDelayMs, minDelayMs));
        this.currentlyScheduledTask.cancel(true);
        this.start();

        return this.delayMs;
    }

    private void start() {

        if (Objects.isNull(this.currentlyScheduledTask) ||
                this.currentlyScheduledTask.isCancelled() ||
                this.currentlyScheduledTask.isDone()) {

            this.currentlyScheduledTask = this.executorService.scheduleWithFixedDelay(() -> {
                        LinkedList<SseEventBuilder> toBeSentItems = new LinkedList<>();
                        this.messageQueue.drainTo(toBeSentItems);
                        toBeSentItems.forEach(msg -> {
                            try {
                                this.clientConnector.send(msg);
                            } catch (IOException ignored) {
                                log.info("Failed to send {} to {} client.", msg.toString(), clientId);
                            }
                        });
                    },
                    Duration.ofSeconds(2).toMillis(),
                    this.delayMs,
                    TimeUnit.MILLISECONDS);

        }

    }


    @Override
    public int hashCode() {
        return Objects.hashCode(clientId);
    }

    public void send(SseEventBuilder eventBuilder) {
        try {
            this.messageQueue.put(eventBuilder);
        } catch (InterruptedException e) {
            log.warn("Thread interrupted, messsage {} will never be delivered to {}. Silent pass.",
                    eventBuilder.toString(),
                    this.clientId);
        }
    }

    public void prepareToDestroy() {
        this.executorService.shutdown();
    }

    //TODO: D'abord desabonner le client, puis ensuite seulement Detruire le SseClient
}
