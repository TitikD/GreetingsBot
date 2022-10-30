package com.greetingsBot.model.keyboards;

import java.util.List;

public interface ISimpleReplyKeyboard extends IKeyboard {
    List<List<ISimpleButton>> getKeyboard();
}
