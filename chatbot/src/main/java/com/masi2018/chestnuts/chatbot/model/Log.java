package com.masi2018.chestnuts.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "log")
public class Log {
    @Id
    private String id;
    private String conversationId;
    private String content;
}
