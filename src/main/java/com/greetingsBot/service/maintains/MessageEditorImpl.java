package com.greetingsBot.service.maintains;

import com.greetingsBot.config.BotConfig;
import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.data.UpdateMessage;
import com.greetingsBot.model.entity.Users;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class MessageEditorImpl implements MessageEditor {

    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private final MessageSender messageSender;
    private final MessageDeleter messageDeleter;

    @Autowired
    public MessageEditorImpl(BotConfig botConfig,
                             RestTemplate restTemplate,
                             MessageSender messageSender,
                             MessageDeleter messageDeleter) {
        this.botConfig = botConfig;
        this.restTemplate = restTemplate;
        this.messageSender = messageSender;
        this.messageDeleter = messageDeleter;
    }

    @SneakyThrows
    @Override
    public Optional<UpdateMessage> editMessage(Long userId, Integer messageId, String newText) {
        String params = String.format("?chat_id=%d&message_id=%s&text=%s", userId, messageId, newText);
        String uri = getRequestUri(params);
        try {
            restTemplate.getForEntity(uri,
                String.class,
                userId,
                messageId,
                newText);
            return Optional.empty();
        } catch (HttpClientErrorException exception) {
            if (exception.getRawStatusCode() == 400) {
                return Optional.empty();
            }
            return messageSender.sendMessage(userId, newText);
        }
    }

    @SneakyThrows
    @Override
    public void editMessage(Update update, Users user, String string, String json, boolean delete) {
        if (user.getLastMessageId() == null){
            messageSender.sendMessage(user.getTelegramUserId(), string, json).ifPresent(msg -> {
                user.setLastMessageId(msg.getMessageId());
            });

            return;
        }
        Optional<HttpClientErrorException> Error = editMessage(
                user.getTelegramUserId(),
                user.getLastMessageId(),
                string,
                json);
        if (delete) {
            messageDeleter.deleteMessage(user.getTelegramUserId(), user.getLastMessageId());
        }
        Error.ifPresent(err -> {
            if (err.getRawStatusCode() == 400) {
                if (Objects.requireNonNull(err.getMessage()).contains("are exactly the same")) {
                    messageSender.sendMessage(user.getTelegramUserId(), string, json).ifPresent(msg -> {
                        user.setLastMessageId(msg.getMessageId());
                    });
                    messageDeleter.deleteMessage(user.getTelegramUserId(), update.getMessage().getMessageId());
                } else  {
                    messageSender.sendMessage(user.getTelegramUserId(), string, json).ifPresent(msg -> {
                        user.setLastMessageId(msg.getMessageId());
                    });
                    messageDeleter.deleteMessage(user.getTelegramUserId(), update.getMessage().getMessageId());
                }
            } else {
                messageSender.sendMessage(user.getTelegramUserId(), string, json).ifPresent(msg -> {
                    user.setLastMessageId(msg.getMessageId());
                });
            }
        });
    }

    @SneakyThrows
    @Override
    public Optional<HttpClientErrorException> editMessage(Long userId, Integer messageId, String newText, String replyMarkup) {
        String params = "?chat_id={userId}&message_id={messageId}&text={newText}&reply_markup={replyMarkup}";
        String uri = getRequestUri(params);
        try {
            restTemplate.getForEntity(uri,
                    String.class,
                    userId,
                    messageId,
                    newText,
                    replyMarkup);
            return Optional.empty();
        } catch (HttpClientErrorException exception) {
            return Optional.of(exception);
        }
    }

    private String getRequestUri(String params) {
        return botConfig.getApiReference() + botConfig.getToken() + "/" + "editMessageText" + params;
    }
}