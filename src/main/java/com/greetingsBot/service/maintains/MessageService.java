package com.greetingsBot.service.maintains;

import com.greetingsBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Scope("prototype")
public class MessageService {
    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private final String uri;
    private UriComponentsBuilder builder;

    @Autowired
    public MessageService(BotConfig botConfig, RestTemplate restTemplate) {
        this.botConfig = botConfig;
        this.restTemplate = restTemplate;
        this.uri = botConfig.getApiReference() + botConfig.getToken() + "/{method}?";
        this.builder = UriComponentsBuilder.fromUriString(uri);
    }

    public void addChatId(int chatId){
        this.builder.queryParam("chat_id", chatId);
    }

}
