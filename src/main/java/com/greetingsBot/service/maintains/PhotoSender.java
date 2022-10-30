package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.UpdateMessage;

import java.util.Optional;

public interface PhotoSender {
    Optional<UpdateMessage> sendPhoto(Long userId, String photo, String caption);
}
