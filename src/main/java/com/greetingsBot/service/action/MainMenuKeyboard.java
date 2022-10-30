package com.greetingsBot.service.action;

import com.greetingsBot.config.ButtonConfig;
import com.greetingsBot.config.MessageConfig;
import com.greetingsBot.model.data.ApplicationState;
import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.entity.MessageEntity;
import com.greetingsBot.model.entity.Users;
import com.greetingsBot.model.keyboards.*;
import com.greetingsBot.repository.MessageRepository;
import com.greetingsBot.service.maintains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainMenuKeyboard extends InlineKeyboard {

    private final NeutralTextAction neutralTextAction;
    private final ButtonConfig buttonConfig;
    private final MessageConfig messageConfig;
    private final PhotoSender photoSender;
    private final JsonMarkupSerializer jsonMarkupSerializer;
    private final NextMenu nextMenu;
    private final MessageDeleter messageDeleter;
    private final MessageSender messageSender;
    private final MessageRepository messageRepository;
    private final MessageEditor messageEditor;

    @Override
    public void neutralText(Update update, Users user) {
        neutralTextAction.execute(update, user);
    }

    @Autowired
    public MainMenuKeyboard(
            MessageConfig messageConfig,
            NeutralTextAction neutralTextAction,
            ButtonConfig buttonConfig,
            PhotoSender photoSender, JsonMarkupSerializer jsonMarkupSerializer, NextMenu nextMenu,
            MessageDeleter messageDeleter,
            MessageSender messageSender, MessageRepository messageRepository,
            MessageEditor messageEditor) {
        this.neutralTextAction = neutralTextAction;
        this.buttonConfig = buttonConfig;
        this.photoSender = photoSender;
        this.jsonMarkupSerializer = jsonMarkupSerializer;
        this.nextMenu = nextMenu;
        this.messageDeleter = messageDeleter;
        this.messageSender = messageSender;
        this.messageRepository = messageRepository;
        this.messageEditor = messageEditor;
        this.messageConfig = messageConfig;
        setText(messageConfig.getMainMenu());
    }

    @PostConstruct
    private void load() {
        List<IInlineButton> firstRow = new ArrayList<>();
        IInlineButton show = new IInlineButton() {
            @Override
            public String getCallbackData() {
                return "show";
            }

            @Override
            public void execute(Update update, Users user) {
                List<MessageEntity> messages = messageRepository.findAllByTelegramUserId(user.getTelegramUserId());
                if (messages.isEmpty()) {
                    return;
                }
                List<MessageEntity> collect = messages.stream().filter(x -> x.getType().equals("hello")).collect(Collectors.toList());
                if (collect.size() > 0){
                    MessageEntity message = collect.get(collect.size() - 1);
                    photoSender.sendPhoto(user.getTelegramUserId(), message.getPhoto(), message.getText());
                } else {
                    messageSender.sendMessage(user.getTelegramUserId(), messageConfig.getNotFill() + " " + buttonConfig.getTypeHello());
                }

//                collect = messages.stream().filter(x -> x.getType().equals("bye")).collect(Collectors.toList());
//                if (collect.size() > 0){
//                    MessageEntity message = collect.get(collect.size() - 1);
//                    messageSender.sendMessage(user.getTelegramUserId(), buttonConfig.getTypeBye());
//                    photoSender.sendPhoto(user.getTelegramUserId(), message.getPhoto(), message.getText());
//                } else {
//                    messageSender.sendMessage(user.getTelegramUserId(), messageConfig.getNotFill() + " " + buttonConfig.getTypeBye());
//                }

                neutralText(update, user);
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getShow();
            }
        };

        firstRow.add(show);

        SimpleInlineKeyboard chooseType = new SimpleInlineKeyboard();
        List<ISimpleInlineButton> chooseFirstRow = new ArrayList<>();
        ISimpleInlineButton hello = new ISimpleInlineButton() {
            @Override
            public String getCallbackData() {
                return "hello";
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getTypeHello();
            }
        };
        chooseFirstRow.add(hello);
        chooseType.addRow(chooseFirstRow);

//        List<ISimpleInlineButton> chooseSecondRow = new ArrayList<>();
//        ISimpleInlineButton bye = new ISimpleInlineButton() {
//
//            @Override
//            public String getCallbackData() {
//                return "bye";
//            }
//
//            @Override
//            public String getTextButton() {
//                return buttonConfig.getTypeBye();
//            }
//        };
//        chooseSecondRow.add(bye);
//        chooseType.addRow(chooseSecondRow);

        List<IInlineButton> secondRow = new ArrayList<>();
        IInlineButton change = new IInlineButton() {

            @Override
            public String getCallbackData() {
                return "change";
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getFillMessage();
            }

            @Override
            public void execute(Update update, Users user) {
                user.setState(ApplicationState.SelectMessageType);
                messageSender.sendMessage(user.getTelegramUserId(),
                        messageConfig.getFillType(),
                        jsonMarkupSerializer.getJsonRepresentation(chooseType));
            }
        };
        secondRow.add(change);

        List<IInlineButton> thirdRow = new ArrayList<>();
        IInlineButton send = new IInlineButton() {

            @Override
            public String getCallbackData() {
                return "send";
            }

            @Override
            public String getTextButton() {
                return buttonConfig.getSendMessage();
            }

            @Override
            public void execute(Update update, Users user) {
                MessageEntity message = MessageEntity.builder()
                        .telegramUserId(user.getTelegramUserId())
                        .type("sending")
                        .text("")
                        .photo("")
                        .build();
                messageRepository.save(message);
                user.setState(ApplicationState.FillMessageText);
                messageSender.sendMessage(user.getTelegramUserId(),
                        messageConfig.getFillText(),
                        "");
            }
        };
        thirdRow.add(send);

        addRow(firstRow);
        addRow(secondRow);
        addRow(thirdRow);
    }
}
