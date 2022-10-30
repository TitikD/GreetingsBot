package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SimpleInlineKeyboard implements ISimpleInlineKeyboard{
    private String text;

    @JsonProperty("inline_keyboard")
    protected List<List<ISimpleInlineButton>> keyboard = new ArrayList<>(new ArrayList<>());

    public void addRow(List<ISimpleInlineButton> buttons) {
        keyboard.add(buttons);
    }
}
