package com.greetingsBot.service.maintains;

import com.greetingsBot.model.data.Actions;
import com.greetingsBot.model.data.ApplicationState;
import com.greetingsBot.model.data.Update;
import com.greetingsBot.model.data.UpdateMessage;
import com.greetingsBot.model.entity.Users;
import com.greetingsBot.service.action.MainMenuKeyboard;
import com.greetingsBot.service.action.RegistrationAction;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class ActionService {

    private final Actions actions;

    public ActionService(Actions actions,
                         MainMenuKeyboard mainMenuKeyboard,
                         RegistrationAction registrationAction) {
        this.actions = actions;

        actions.addAction(ApplicationState.MainMenu, mainMenuKeyboard);

        actions.addAction(ApplicationState.SelectMessageType, registrationAction);
        actions.addAction(ApplicationState.FillMessageText, registrationAction);
        actions.addAction(ApplicationState.FillMessagePhoto, registrationAction);
        actions.addAction(ApplicationState.FillAgree, registrationAction);
    }

    public void execute(String state, Update update, Users user)
    {
        actions.getActions().get(state).execute(update, user);
    }

}
