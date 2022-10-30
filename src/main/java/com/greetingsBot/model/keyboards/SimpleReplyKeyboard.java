package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SimpleReplyKeyboard implements ISimpleReplyKeyboard{
    private String text;

    @JsonProperty("keyboard")
    protected List<List<ISimpleButton>> keyboard = new ArrayList<>(new ArrayList<>());

    public void addRow(List<ISimpleButton> buttons) { keyboard.add(buttons); }
}
