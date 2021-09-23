package com.maukaim.cryptohub.web;

import com.maukaim.cryptohub.web.sse.model.SseClient;
import com.maukaim.cryptohub.web.sse.model.SseEventType;
import com.maukaim.cryptohub.web.sse.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;


@RestController
@RequestMapping("api/v1/sse-channel")
@Slf4j
public class SseController {

    private final SseService sseService;

    public SseController(@Autowired SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(path = "/")
    public SseEmitter openSseChannel(@RequestParam String uuid) {
        SseClient sseClient = sseService.createClient(uuid);
        return sseClient.getClientConnector();
    }

    @GetMapping(path = "/{extUuid}/pulse")
    public ResponseEntity<Integer> openSseChannel(@PathVariable String extUuid, @RequestParam Integer pulseMs){
        SseClient client = this.sseService.getClient(extUuid);
        return Objects.nonNull(client) ?
                ResponseEntity.ok(client.changePulse(pulseMs)) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{extUuid}/exchange/subscribe")
    public ResponseEntity<Void> subscribeToExchangeEvent(
            @RequestParam String exchangeId,
            @PathVariable String extUuid,
            @RequestParam String event) {
        this.sseService.addSubscriber(extUuid, exchangeId, SseEventType.valueOf(event));
        return ResponseEntity.ok().build();
    }


}
