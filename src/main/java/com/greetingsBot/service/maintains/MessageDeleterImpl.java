package com.greetingsBot.service.maintains;

import com.greetingsBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageDeleterImpl implements MessageDeleter {

    private final BotConfig botConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public MessageDeleterImpl(BotConfig botConfig,
                              RestTemplate restTemplate) {
        this.botConfig = botConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public void deleteMessage(Long userId, int messageId) {
        String uri = botConfig.getApiReference() +
                botConfig.getToken() + "/" +
                "deleteMessage";
        String params = String.format("?chat_id=%d&message_id=%d",
                userId, messageId);
        uri += params;

        try {
            restTemplate.getForEntity(uri, String.class);
        } catch (HttpClientErrorException ignored) {
        }
    }
}
