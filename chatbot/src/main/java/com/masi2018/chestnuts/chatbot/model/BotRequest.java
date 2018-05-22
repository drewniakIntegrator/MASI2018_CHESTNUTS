package com.masi2018.chestnuts.chatbot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BotRequest {
    private String conversationId;
    private String message;
}
