package com.greetingsBot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.greetingsBot")
@PropertySource(
        value = "classpath:button.properties",
        encoding = "utf-8")
@PropertySource(
        value = "classpath:message.properties",
        encoding = "utf-8")

@EnableJdbcRepositories(basePackages = "com.greetingsBot.repository")
@EnableRedisRepositories(basePackages = "com.greetingsBot.repository")
@EnableScheduling
public class GreetingsBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreetingsBotApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(GreetingsBotApplication.class);
    }
}
