package com.masi2018.chestnuts.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ConversationReport {
    private ConversationSummary conversationSummary;
    private double avgEffectivenessScore;
    private double avgUsabilityScore;
}
