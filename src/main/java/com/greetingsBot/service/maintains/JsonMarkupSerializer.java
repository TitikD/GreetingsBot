package com.greetingsBot.service.maintains;

import com.greetingsBot.model.keyboards.*;

public interface JsonMarkupSerializer {
    String getJsonRepresentation (ISimpleButton simpleButton);
    String getJsonRepresentation (ISimpleInlineButton simpleInlineButton);
    String getJsonRepresentation(SimpleInlineKeyboard keyboard);
    String getJsonRepresentation(SimpleReplyKeyboard keyboard);
    String getJsonRepresentation(ReplyKeyboard replyKeyboard);
    String getJsonRepresentation(InlineKeyboard inlineKeyboard);
}
