package com.masi2018.chestnuts.chatbot.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BotResponse {
    private String url;
    private String message;
    private String conversationId;
    private List<String> hints;

    private List<Item> items;
}
