package com.maukaim.cryptohub.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("api/v1/marketData")
public class MarketDataController {

    @GetMapping(value = "/")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok("Good, I received the request.");
    }
}
