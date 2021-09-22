package com.maukaim.cryptohub.web.stream;

import com.google.common.collect.Maps;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public class SseDataDispatcherImpl implements DataDispatcher<SseEmitter> {
    Map<String, SseEmitter> emitterMap = Maps.newConcurrentMap();

    @Override
    public void addListener(String extUuid, SseEmitter dispatcher) {
        this.emitterMap.put(extUuid,dispatcher);
    }
}
