package com.greetingsBot.repository;

import com.greetingsBot.model.entity.Members;
import com.greetingsBot.model.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembersRepository extends CrudRepository<Members, Long> {
    List<Members> findAllByTelegramUserId(Long telegramUserId);
}
