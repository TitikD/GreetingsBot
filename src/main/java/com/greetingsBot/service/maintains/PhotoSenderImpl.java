package com.greetingsBot.service.maintains;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greetingsBot.config.BotConfig;
import com.greetingsBot.model.data.UpdateMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PhotoSenderImpl implements PhotoSender {

    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PhotoSenderImpl(BotConfig botConfig,
                           RestTemplate restTemplate,
                           ObjectMapper objectMapper) {
        this.botConfig = botConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Optional<UpdateMessage> sendPhoto(Long userId, String photo, String caption) {
        String params = "?chat_id={userId}&photo={photo}&caption={caption}&parse_mode=HTML";
        String uri = getRequestUri(params);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(uri,
                    String.class,
                    userId,
                    photo,
                    caption);
            String responseJson = responseEntity.getBody();
            String result = objectMapper
                    .readTree(responseJson)
                    .get("result")
                    .toString();
            UpdateMessage sentMessage = objectMapper.readValue(result, UpdateMessage.class);
            return Optional.ofNullable(sentMessage);
        } catch (HttpClientErrorException ignored) {

        }
        return Optional.empty();
    }

    private String getRequestUri(String params) {
        return botConfig.getApiReference() + botConfig.getToken() + "/" + "sendPhoto" + params;
    }
}
