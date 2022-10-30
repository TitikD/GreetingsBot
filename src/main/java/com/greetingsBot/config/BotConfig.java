package com.greetingsBot.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotConfig {
    private String apiReference;
    private String token;
    private String webhook;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    @Autowired
    public BotConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void setWebhook() {
        String uri = apiReference + token + "/" + "setWebhook";
        String params = String.format("?url=%s", webhook);
        uri += params;

        restTemplate.getForObject(uri, String.class);
    }

    @PreDestroy
    public void removeWebhook() {
        String uri = apiReference + token + "/" + "removeWebhook";
        restTemplate.getForObject(uri, String.class);
    }
}
