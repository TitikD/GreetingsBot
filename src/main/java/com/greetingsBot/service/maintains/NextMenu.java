package com.greetingsBot.service.maintains;


import com.greetingsBot.model.data.*;
import com.greetingsBot.model.entity.Users;
import com.greetingsBot.model.keyboards.InlineKeyboard;
import com.greetingsBot.model.keyboards.ReplyKeyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NextMenu {
    private final MessageEditor messageEditor;
    private final JsonMarkupSerializer jsonMarkupSerializer;
    private final MessageSender messageSender;
    private final Actions actions;

    @Autowired
    public NextMenu(
            MessageEditor messageEditor,
            JsonMarkupSerializer jsonMarkupSerializer,
            MessageSender messageSender, Actions actions) {
        this.messageEditor = messageEditor;
        this.jsonMarkupSerializer = jsonMarkupSerializer;
        this.messageSender = messageSender;
        this.actions = actions;
    }

    public void execute(String nextState, Update update , Users user) {
        IAction next = actions.getActions().get(nextState);
        user.setState(nextState);
        if(next instanceof ReplyKeyboard) {
            messageSender.sendMessage(user.getTelegramUserId(),
                    ((ReplyKeyboard)next).getText(),
                    jsonMarkupSerializer.getJsonRepresentation((ReplyKeyboard)next));
        }else {
            messageSender.sendMessage(user.getTelegramUserId(),
                    ((InlineKeyboard)next).getText(),
                    jsonMarkupSerializer.getJsonRepresentation((InlineKeyboard) next));
        }
        // messageDao.deleteAll(user.getTelegramUserId());
    }
}
