package com.greetingsBot.model.keyboards;

import com.greetingsBot.model.data.IAction;
import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.data.UpdateMessage;
import com.greetingsBot.model.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Keyboard implements IAction{
    private Map<String, IAction> mapButtons = new HashMap<>();

    public void neutralText(Update update, Users user){};

    protected void addKey (String string, IAction action) {mapButtons.put(string, action);}

    public void execute(Update update, Users user) {
        String action = "";
        if (update.getCallbackQuery() != null){
            action = update.getCallbackQuery().getData();
        }
        else if (update.getMessage() != null) {
            action = update.getMessage().getText();
        }

        IAction button = mapButtons.get(action);
        if(button != null)
            button.execute(update, user);
        else
            neutralText(update, user);
    }
}
