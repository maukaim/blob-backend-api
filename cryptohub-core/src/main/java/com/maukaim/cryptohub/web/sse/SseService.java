package com.maukaim.cryptohub.web.sse;

import com.maukaim.cryptohub.web.sse.model.SseClient;
import com.maukaim.cryptohub.web.sse.model.SseEventType;

public interface SseService {
    SseClient createClient(String extUuid);
    SseClient getClient(String extUuid);
    void removeClient(String extUuid);

    void addSubscriber(String extUuid, String wrapperId, SseEventType event) throws NoSuchSseClientException;
    void removeSubscriber(String extUuid, String wrapperId, SseEventType event);
}
