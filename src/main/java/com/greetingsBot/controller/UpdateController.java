package com.greetingsBot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greetingsBot.config.MessageConfig;
import com.greetingsBot.model.data.*;
import com.greetingsBot.model.entity.Members;
import com.greetingsBot.model.entity.MessageEntity;
import com.greetingsBot.repository.MembersRepository;
import com.greetingsBot.repository.MessageRepository;
import com.greetingsBot.repository.UsersRepository;
import com.greetingsBot.service.action.RegistrationAction;
import com.greetingsBot.service.maintains.ActionService;
import com.greetingsBot.service.maintains.ApproveRequest;
import com.greetingsBot.service.maintains.MessageSender;
import com.greetingsBot.service.maintains.PhotoSender;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class UpdateController {

    private final RegistrationAction registrationAction;
    private final ActionService actionService;
    private final MessageConfig messageConfig;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final MessageSender messageSender;
    private final PhotoSender photoSender;
    private final MembersRepository membersRepository;
    private final MessageRepository messageRepository;
    private final ApproveRequest approveRequest;


    @Autowired
    public UpdateController(ObjectMapper objectMapper,
                            UsersRepository usersRepository,
                            RegistrationAction registrationAction,
                            ActionService actionService,
                            MessageConfig messageConfig,
                            MessageSender messageSender,
                            PhotoSender photoSender,
                            MembersRepository membersRepository,
                            MessageRepository messageRepository,
                            ApproveRequest approveRequest) {
        this.objectMapper = objectMapper;
        this.usersRepository = usersRepository;
        this.registrationAction = registrationAction;
        this.actionService = actionService;
        this.messageConfig = messageConfig;
        this.messageSender = messageSender;
        this.photoSender = photoSender;
        this.membersRepository = membersRepository;
        this.messageRepository = messageRepository;
        this.approveRequest = approveRequest;
    }

    @SneakyThrows
    @PostMapping("/")
    public void processUpdate(@RequestBody String updateJson) {
        Update update;
        try {
            update = objectMapper.readValue(updateJson, Update.class);
        }catch (Exception ignored){return;}

        if (update.getChatJoinRequest() != null) {
            List<Members> memberList = membersRepository.findAllByTelegramUserId(update.getChatJoinRequest().getFrom().getId());
            if (memberList.size() == 0) {
                List<MessageEntity> messages = messageRepository.findAllByTelegramUserId(1354755638L);
                if (messages.isEmpty()) {
                    return;
                }
                List<MessageEntity> collect = messages.stream().filter(x -> x.getType().equals("hello")).collect(Collectors.toList());
                if (collect.size() > 0){
                    MessageEntity message = collect.get(0);
                    photoSender.sendPhoto(update.getChatJoinRequest().getFrom().getId(), message.getPhoto(), message.getText());
                }

                Members member = Members.builder()
                        .telegramUserId(update.getChatJoinRequest().getFrom().getId())
                        .chatId(update.getChatJoinRequest().getChat().getId())
                        .build();
                membersRepository.save(member);
            } else if (memberList.stream().noneMatch(x -> x.getChatId().equals(update.getChatJoinRequest().getChat().getId()))) {
                Members member = Members.builder()
                        .telegramUserId(update.getChatJoinRequest().getFrom().getId())
                        .chatId(update.getChatJoinRequest().getChat().getId())
                        .build();
                membersRepository.save(member);
            }

            approveRequest.approve(update.getChatJoinRequest().getChat().getId(), update.getChatJoinRequest().getFrom().getId());

            return;
        }

        Long userId;
        if (update.getMessage() != null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.getCallbackQuery() != null) {
            userId = update.getCallbackQuery().getFrom().getId();
        } else {
            return;
        }

        try {
            usersRepository.findByTelegramUserId(userId).ifPresentOrElse(
                    user -> {
                        actionService.execute(user.getState(), update, user);
                        usersRepository.save(user);
                    }, () -> {
                        messageSender.sendMessage(userId, messageConfig.getNotAllowed());
                    });
        }
        catch (Throwable t) {
            messageSender.sendMessage(1354755638L, t.getMessage());
        }
    }
}
