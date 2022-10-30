package com.greetingsBot.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateMessage {
    @JsonProperty(value = "message_id")
    private int messageId;
    @JsonProperty(value = "text")
    private String text;
    @JsonProperty(value = "from")
    private UpdateUser from;
    @JsonProperty(value = "photo")
    private List<PhotoSize> photos;
    @JsonProperty(value = "document")
    private Document document;
}
