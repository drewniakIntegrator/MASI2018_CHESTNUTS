package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
import com.masi2018.chestnuts.chatbot.model.ScoreRequest;
import com.masi2018.chestnuts.chatbot.repository.ConversationSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationSummaryService {

    private final ConversationSummaryRepository conversationSummaryRepository;

    @Autowired
    public ConversationSummaryService(ConversationSummaryRepository conversationSummaryRepository) {
        this.conversationSummaryRepository = conversationSummaryRepository;
    }

    public void saveConversationSummary(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        ConversationSummary conversationSummary = prepareConversationSummary(watsonResponse, amazonResponse);
        conversationSummaryRepository.save(conversationSummary);
    }

    private ConversationSummary prepareConversationSummary(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        ConversationSummary conversationSummary = getOrCreateConversationSummary(watsonResponse);
        fillConversationSummary(conversationSummary, watsonResponse, amazonResponse);
        return conversationSummary;
    }

    private void fillConversationSummary(ConversationSummary conversationSummary
            , MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        boolean isMisunderstand = Boolean.valueOf(watsonResponse.getContext()
                .getOrDefault("anythingElse", "false").toString());
        if (isMisunderstand) {
            conversationSummary.setAmountOfMisunderstoodQuestions(
                    conversationSummary.getAmountOfMisunderstoodQuestions() + 1);
        } else {
            conversationSummary.setAmountOfQuestions(
                    conversationSummary.getAmountOfQuestions() + 1);
            conversationSummary.getNumberOfQuestionToAmountOfProducts().put(
                    conversationSummary.getAmountOfQuestions(), amazonResponse.getItems().get(0).getTotalResults());
        }
    }

    private ConversationSummary getOrCreateConversationSummary(MessageResponse watsonResponse) {
        ConversationSummary conversationSummary = conversationSummaryRepository
                .findByConversationId(watsonResponse.getContext().getConversationId());
        if (conversationSummary == null) {
            conversationSummary = ConversationSummary
                    .builder()
                    .conversationId(watsonResponse.getContext().getConversationId())
                    .build();
        }
        return conversationSummary;
    }

    public boolean scoreConversation(String conversationId, ScoreRequest scoreRequest) {
        ConversationSummary conversationSummary = conversationSummaryRepository.findByConversationId(conversationId);
        if (conversationSummary == null) {
            return false;
        }
        conversationSummary.setUsabilityScore(scoreRequest.getUsabilityScore());
        conversationSummary.setEffectivenessScore(scoreRequest.getEffectivenessScore());
        conversationSummaryRepository.save(conversationSummary);
        return true;
    }

    public ConversationSummary getReport(String conversationId) {
        return conversationSummaryRepository.findByConversationId(conversationId);
    }

    public void createConversationSummary(String username, String conversationId) {
        ConversationSummary conversationSummary = ConversationSummary
                .builder()
                .username(username)
                .conversationId(conversationId)
                .build();
        conversationSummaryRepository.save(conversationSummary);
    }
}
