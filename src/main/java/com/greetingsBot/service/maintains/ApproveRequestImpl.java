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
public class ApproveRequestImpl implements ApproveRequest{
    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApproveRequestImpl(BotConfig botConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.botConfig = botConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Optional<Boolean> approve(Long chatId, Long userId) {
        String params = "?chat_id={chatId}&user_id={userId}";
        String uri = getRequestUri(params);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity =
                    restTemplate.getForEntity(uri,
                            String.class,
                            chatId,
                            userId);
            String responseJson = responseEntity.getBody();
            String result = objectMapper
                    .readTree(responseJson)
                    .get("result")
                    .toString();
            Boolean sentMessage = objectMapper.readValue(result, Boolean.class);
            return Optional.ofNullable(sentMessage);
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }
    }
    private String getRequestUri(String params) {
        return botConfig.getApiReference() + botConfig.getToken() + "/" + "approveChatJoinRequest" + params;
    }
}
