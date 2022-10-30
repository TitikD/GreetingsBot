package com.greetingsBot.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackQuery {
    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private UpdateUser from;

    @JsonProperty("message")
    private UpdateMessage message;

    @JsonProperty("data")
    String data;
}
