package com.greetingsBot.service.action;

import com.greetingsBot.config.ButtonConfig;
import com.greetingsBot.config.MessageConfig;
import com.greetingsBot.model.data.*;
import com.greetingsBot.model.entity.MessageEntity;
import com.greetingsBot.model.entity.Users;
import com.greetingsBot.model.keyboards.ISimpleInlineButton;
import com.greetingsBot.model.keyboards.SimpleInlineKeyboard;
import com.greetingsBot.repository.MembersRepository;
import com.greetingsBot.repository.MessageRepository;
import com.greetingsBot.repository.UsersRepository;
import com.greetingsBot.service.maintains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RegistrationAction implements IAction {
    private final UsersRepository usersRepository;
    private final MessageSender messageSender;
    private final MessageConfig messageConfig;
    private final SimpleInlineKeyboard yesNo;
    private final ButtonConfig buttonConfig;
    private final MessageEditor messageEditor;
    private final PhotoSender photoSender;
    private final JsonMarkupSerializer jsonMarkupSerializer;
    private final MessageRepository messageRepository;
    private final MembersRepository membersRepository;
    private final NextMenu nextMenu;

    @Autowired
    public RegistrationAction(
            UsersRepository usersRepository,
            MessageSender messageSender, MessageConfig messageConfig,
            ButtonConfig buttonConfig,
            MessageEditor messageEditor,
            PhotoSender photoSender, JsonMarkupSerializer jsonMarkupSerializer,
            MessageRepository messageRepository, MembersRepository membersRepository, NextMenu nextMenu) {
        this.usersRepository = usersRepository;
        this.messageSender = messageSender;
        this.messageConfig = messageConfig;
        this.buttonConfig = buttonConfig;
        this.messageEditor = messageEditor;
        this.photoSender = photoSender;
        this.jsonMarkupSerializer = jsonMarkupSerializer;
        this.messageRepository = messageRepository;
        this.membersRepository = membersRepository;
        this.nextMenu = nextMenu;

        this.yesNo = new SimpleInlineKeyboard();

        List<ISimpleInlineButton> firstRow = new ArrayList<>();
        ISimpleInlineButton yes = new ISimpleInlineButton() {
            @Override
            public String getCallbackData() {
                return "yes";
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getYes();
            }
        };
        firstRow.add(yes);
        this.yesNo.addRow(firstRow);

        List<ISimpleInlineButton> secondRow = new ArrayList<>();
        ISimpleInlineButton no = new ISimpleInlineButton() {
            @Override
            public String getCallbackData() {
                return "no";
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getNo();
            }
        };
        secondRow.add(no);
        this.yesNo.addRow(secondRow);
    }

    @Override
    public void execute(Update update, Users user) {
        if(Objects.equals(user.getState(), ApplicationState.SelectMessageType)) {
            CallbackQuery callBack = update.getCallbackQuery();
            if (callBack == null){
                return;
            }

            MessageEntity message = MessageEntity.builder()
                        .telegramUserId(user.getTelegramUserId())
                        .type(callBack.getData())
                        .text("")
                        .photo("")
                        .build();
            messageRepository.save(message);
            user.setState(ApplicationState.FillMessageText);
            messageSender.sendMessage(user.getTelegramUserId(),
                    messageConfig.getFillText(),
                    "");
        } else if (Objects.equals(user.getState(), ApplicationState.FillMessageText)) {
            UpdateMessage updateMessage = update.getMessage();
            if (updateMessage == null){
                return;
            }

            List<MessageEntity> messages = messageRepository.findAllByTelegramUserId(user.getTelegramUserId());
            if (messages.isEmpty()) {
                return;
            }
            MessageEntity message = messages.get(messages.size() - 1);
            message.setText(updateMessage.getText());
            user.setState(ApplicationState.FillMessagePhoto);
            messageSender.sendMessage(user.getTelegramUserId(),
                    messageConfig.getFillPhoto(),
                    "");
            messageRepository.save(message);
        } else if (Objects.equals(user.getState(), ApplicationState.FillMessagePhoto)) {
            UpdateMessage updateMessage = update.getMessage();
            if (updateMessage == null){
                return;
            }

            List<PhotoSize> photoSizes = updateMessage.getPhotos();
            if (photoSizes == null){
                return;
            }

            List<MessageEntity> messages = messageRepository.findAllByTelegramUserId(user.getTelegramUserId());
            if (messages.isEmpty()) {
                return;
            }
            MessageEntity message = messages.get(messages.size() - 1);
            message.setPhoto(photoSizes.get(0).getFileId());

            user.setState(ApplicationState.FillAgree);

            photoSender.sendPhoto(user.getTelegramUserId(), photoSizes.get(0).getFileId(), message.getText());
            messageSender.sendMessage(user.getTelegramUserId(),
                    messageConfig.getFillAgree(),
                    jsonMarkupSerializer.getJsonRepresentation(yesNo));
            messageRepository.save(message);
        } else if (Objects.equals(user.getState(), ApplicationState.FillAgree)) {
            CallbackQuery callBack = update.getCallbackQuery();
            if (callBack == null){
                messageSender.sendMessage(user.getTelegramUserId(),
                        messageConfig.getFillAgree(),
                        jsonMarkupSerializer.getJsonRepresentation(yesNo));
                return;
            }

            List<MessageEntity> messages = messageRepository.findAllByTelegramUserId(
                    user.getTelegramUserId());
            if (messages.isEmpty()) {
                return;
            }

            MessageEntity last = messages.get(messages.size() - 1);
            messages = messages.stream().filter(x -> x.getType().equals(last.getType())).collect(Collectors.toList());

            if (callBack.getData().equals("yes")){
                if (messages.size() > 1){
                    MessageEntity message = messages.get(0);
                    messageRepository.delete(message);
                }
                if (last.getType().equals("sending"))
                {
                    List<Long> list = new ArrayList<>();
                    membersRepository.findAll().forEach(x -> {
                        if (list.stream().noneMatch(y -> y.equals(x.getTelegramUserId()))) {
                            list.add(x.getTelegramUserId());
                        }
                    });

                    MessageEntity message = messages.get(0);
                    list.forEach(x -> photoSender.sendPhoto(x, message.getPhoto(), message.getText()));

                    messageRepository.delete(messages.get(0));
                }
                user.setState(ApplicationState.MainMenu);
                nextMenu.execute(ApplicationState.MainMenu, update, user);

            } else if (callBack.getData().equals("no")) {
                MessageEntity message = messages.get(messages.size() - 1);
                messageRepository.delete(message);
                user.setState(ApplicationState.MainMenu);
                nextMenu.execute(ApplicationState.MainMenu, update, user);
            }
        }
    }
}
