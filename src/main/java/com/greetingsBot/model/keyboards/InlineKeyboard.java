package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InlineKeyboard extends Keyboard implements ISimpleInlineKeyboard {
    private String text;

    @JsonProperty("inline_keyboard")
    protected List<List<ISimpleInlineButton>> keyboard = new ArrayList<>(new ArrayList<>());

    public void addRow(List<IInlineButton> buttons) {
        List<ISimpleInlineButton> firstRow = new ArrayList<>(new ArrayList<>());
        for (IInlineButton button: buttons) {
            addKey(button.getCallbackData(), button);
            firstRow.add(button);
        }
        getKeyboard().add(firstRow);
    }
}
