package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.UpdateMessage;

import java.util.Optional;

public interface MessageSender {
    Optional<UpdateMessage> sendMessage(Long userId, String messageText);
    Optional<UpdateMessage> sendMessage(Long userId, String messageText, String jsonMarkup);
}
