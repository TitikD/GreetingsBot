package com.greetingsBot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "button")
public class ButtonConfig {
    private String show;
    private String fillMessage;
    private String sendMessage;

    private String typeHello;
    private String typeBye;

    private String back;
    private String yes;
    private String no;
}
