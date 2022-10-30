package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReplyKeyboard extends Keyboard implements ISimpleReplyKeyboard{

    private String text;
    @JsonProperty("keyboard")
    List<List<ISimpleButton>> keyboard = new ArrayList<>(new ArrayList<>());

    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard;

    public List<List<ISimpleButton>> getKeyboard(){return keyboard;}

    public void addRow(List<IButton> buttons) {
        List<ISimpleButton> firstRow = new ArrayList<>(new ArrayList<>());
        for (IButton button: buttons) {
            addKey(button.getTextButton(), button);
            firstRow.add(button);
        }
        keyboard.add(firstRow);
    }

    public void setResize(boolean resize) {
        resizeKeyboard = resize;
    }
}
