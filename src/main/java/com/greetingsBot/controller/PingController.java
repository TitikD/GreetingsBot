package com.greetingsBot.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final Logger logger;

    @Autowired
    public PingController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping("/")
    public void processPingRequest() {
        logger.info("ping request received");
    }
}
