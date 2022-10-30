package com.greetingsBot.model.keyboards;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ISimpleButton{
    @JsonProperty("text")
    String getTextButton();
}
