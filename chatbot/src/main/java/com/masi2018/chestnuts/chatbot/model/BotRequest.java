package com.masi2018.chestnuts.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BotRequest {
    private String conversationId;
    private String message;

    BotRequest() {}
}
