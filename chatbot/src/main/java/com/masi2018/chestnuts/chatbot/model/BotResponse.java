package com.masi2018.chestnuts.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private boolean isFinal;
    private List<Item> items;

    @JsonProperty(value = "isFinal")
    public boolean isFinal() {
        return isFinal;
    }
}
