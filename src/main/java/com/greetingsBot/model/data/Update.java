package com.greetingsBot.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {
    @JsonProperty(value = "update_id")
    private int updateId;
    @JsonProperty(value = "message")
    private UpdateMessage message;
    @JsonProperty(value = "callback_query")
    private CallbackQuery callbackQuery;
    @JsonProperty(value = "chat_join_request")
    private ChatJoinRequest chatJoinRequest;
}
