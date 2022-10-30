package com.greetingsBot.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatJoinRequest {
    @JsonProperty(value = "chat")
    private Chat chat;
    @JsonProperty(value = "from")
    private UpdateUser from;
}
