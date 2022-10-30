package com.greetingsBot.model.data;

import com.greetingsBot.model.entity.Users;

public interface IAction {
    void execute(Update update, Users user);
}
