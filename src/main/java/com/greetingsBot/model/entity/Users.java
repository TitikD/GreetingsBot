package com.greetingsBot.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Builder
@Table("users")
public class Users {

    @Id
    private Integer id;
    private Long telegramUserId;
    private String state;
    private String role;
    private Integer lastMessageId;

    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
}
