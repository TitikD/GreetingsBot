package com.greetingsBot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "message")
public class MessageConfig {
    private String notAllowed;

    private String greeting;
    private String mainMenu;
    private String success;

    private String fillText;
    private String fillPhoto;
    private String fillType;
    private String fillAgree;
    private String notFill;
}
