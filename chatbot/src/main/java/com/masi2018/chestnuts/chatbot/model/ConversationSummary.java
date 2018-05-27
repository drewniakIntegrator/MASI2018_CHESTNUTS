package com.masi2018.chestnuts.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "conversationSummary")
public class ConversationSummary {
    @Id
    private String id;
    private String username;
    private String userAddress;
    private String conversationId;
    private int amountOfQuestions;
    private int amountOfMisunderstoodQuestions;
    private int usabilityScore;
    private int effectivenessScore;
    @Builder.Default
    Map<Integer, BigInteger> numberOfQuestionToAmountOfProducts = new HashMap<>();
}
