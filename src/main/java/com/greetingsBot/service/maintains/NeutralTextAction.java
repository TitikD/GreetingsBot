package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.Actions;
import com.greetingsBot.model.data.IAction;
import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.entity.Users;
import com.greetingsBot.model.keyboards.InlineKeyboard;
import com.greetingsBot.model.keyboards.ReplyKeyboard;
import org.springframework.stereotype.Service;

@Service
public class NeutralTextAction {
    private final MessageEditor messageEditor;
    private final MessageSender messageSender;
    private final JsonMarkupSerializer jsonMarkupSerializer;
    private final Actions actions;

    public NeutralTextAction(MessageEditor messageEditor,
                             MessageSender messageSender, JsonMarkupSerializer jsonMarkupSerializer,
                             Actions actions) {
        this.messageEditor = messageEditor;
        this.messageSender = messageSender;
        this.jsonMarkupSerializer = jsonMarkupSerializer;
        this.actions = actions;
    }

    public void execute(Update update, Users user) {
        IAction next = actions.getActions().get(user.getState());
        if(next instanceof ReplyKeyboard) {
            messageSender.sendMessage(user.getTelegramUserId(),
                    ((ReplyKeyboard)next).getText(),
                    jsonMarkupSerializer.getJsonRepresentation((ReplyKeyboard)next));
        }else {
            messageSender.sendMessage(user.getTelegramUserId(),
                    ((InlineKeyboard)next).getText(),
                    jsonMarkupSerializer.getJsonRepresentation((InlineKeyboard) next));
        }
    }
}
