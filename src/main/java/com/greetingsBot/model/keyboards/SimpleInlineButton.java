package com.greetingsBot.model.keyboards;

public class SimpleInlineButton implements ISimpleInlineButton{
    private String callbackData;
    private String textButton;


    public SimpleInlineButton(String textButton, String callbackData) {
        this.textButton = textButton;
        this.callbackData = callbackData;
    }
    public SimpleInlineButton(String textButton) {
        this.textButton = textButton;
        this.callbackData = textButton;
    }
    @Override
    public String getCallbackData() {
        return callbackData;
    }

    @Override
    public String getTextButton() {
        return textButton;
    }
}
