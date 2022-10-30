package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.UpdateMessage;

import java.util.Optional;

public interface ApproveRequest {
    Optional<Boolean> approve(Long chatId, Long userId);
}
