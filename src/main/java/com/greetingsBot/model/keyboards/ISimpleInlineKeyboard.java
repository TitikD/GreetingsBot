package com.greetingsBot.model.keyboards;

import java.util.List;

public interface ISimpleInlineKeyboard extends IKeyboard {
    List<List<ISimpleInlineButton>> getKeyboard();
}
