package com.maukaim.cryptohub.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/marketData")
public class MarketDataController {

    @GetMapping(value = "/")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok("Good, I received the request.");
    }
}
