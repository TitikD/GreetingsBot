package com.greetingsBot.repository;

import com.greetingsBot.model.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface MessageRepository extends CrudRepository<MessageEntity, Long> {
    Optional<MessageEntity> findByType(String type);
    List<MessageEntity> findAllByTelegramUserId(Long telegramUserId);
}
