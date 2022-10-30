package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ISimpleInlineButton extends ISimpleButton {
    @JsonProperty("callback_data")
    String getCallbackData();
}
