package com.greetingsBot.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoSize {
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("width")
    private String width;
    @JsonProperty("height")
    private String height;
}
