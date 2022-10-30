package com.greetingsBot.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("messages")
public class MessageEntity {
    @Id
    private Integer id;
    private Long telegramUserId;
    private String text;
    private String photo;
    private String type;
}
