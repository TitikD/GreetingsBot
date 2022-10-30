package com.greetingsBot.model.data;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Getter
@Repository
public class Actions {

    private Map<String, IAction> actions;

    @Autowired
    public Actions() {
        actions = new HashMap<>();
    }

    public void addAction(String state, IAction action)
    {
        actions.put(state, action);
    }
}
