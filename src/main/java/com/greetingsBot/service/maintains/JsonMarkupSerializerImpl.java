package com.greetingsBot.service.maintains;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greetingsBot.model.keyboards.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class JsonMarkupSerializerImpl implements JsonMarkupSerializer {

    private final ObjectMapper objectMapper;

    public JsonMarkupSerializerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(ISimpleButton simpleButton) {
        return objectMapper.writeValueAsString(simpleButton);
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(ISimpleInlineButton simpleInlineButton) {
        return objectMapper.writeValueAsString(simpleInlineButton);
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(SimpleInlineKeyboard keyboard) {
        return objectMapper.writeValueAsString(keyboard);
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(SimpleReplyKeyboard keyboard) {
        return objectMapper.writeValueAsString(keyboard);
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(ReplyKeyboard replyKeyboard) {
        return objectMapper.writeValueAsString(replyKeyboard);
    }

    @SneakyThrows
    @Override
    public String getJsonRepresentation(InlineKeyboard inlineKeyboard) {
        return objectMapper.writeValueAsString(inlineKeyboard);
    }
}
