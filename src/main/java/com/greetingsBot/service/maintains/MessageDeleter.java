package com.greetingsBot.service.maintains;

public interface MessageDeleter {
    void deleteMessage(Long userId, int messageId);
}
