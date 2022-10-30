package com.greetingsBot.repository;

import com.greetingsBot.model.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UsersRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByTelegramUserId(Long telegramUserId);
    Optional<Users> findById(Integer id);
}
