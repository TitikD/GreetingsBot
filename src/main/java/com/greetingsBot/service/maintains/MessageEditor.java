package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.data.UpdateMessage;
import com.greetingsBot.model.entity.Users;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public interface MessageEditor {
    Optional<UpdateMessage> editMessage(Long userId, Integer messageId, String newText);
    Optional<HttpClientErrorException> editMessage(Long userId, Integer messageId, String newText, String replyMarkup);
    void editMessage(Update update, Users user, String string, String json, boolean delete);
}
