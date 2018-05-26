package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
import com.masi2018.chestnuts.chatbot.model.ScoreRequest;
import com.masi2018.chestnuts.chatbot.repository.ConversationSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        //@TODO  implement
        return ConversationSummary.builder().build();
    }

    public boolean scoreConversation(String conversationId, ScoreRequest scoreRequest) {
//        ConversationSummary conversationSummary = conversationSummaryRepository.findByConversationId(conversationId);
//        if(conversationSummary == null) {
//            return false;
//        }
        //temporary for mock
        //TODO implement, this is only for mocked version
        ConversationSummary conversationSummary = new ConversationSummary();
        conversationSummary.setConversationId(conversationId);
        conversationSummary.setUsabilityScore(scoreRequest.getUsabilityScore());
        conversationSummary.setEffectivenessScore(scoreRequest.getEffectivenessScore());
        conversationSummaryRepository.save(conversationSummary);
        return true;
    }

    public ConversationSummary getReport(String conversationId) {
        //TODO implement, this is only mocked version
        ConversationSummary conversationSummary = new ConversationSummary();
        conversationSummary.setConversationId(conversationId);
        conversationSummary.setAmountOfMisunderstoodQuestions(2);
        conversationSummary.setAmountOfQuestions(2);
        conversationSummary.setUsername("username");
        Map<Integer, Integer> numberOfQuestionToAmountOfProducts = new HashMap<>();
        numberOfQuestionToAmountOfProducts.put(1, 25);
        numberOfQuestionToAmountOfProducts.put(2, 15);
        numberOfQuestionToAmountOfProducts.put(3, 10);
        numberOfQuestionToAmountOfProducts.put(4, 3);
        conversationSummary.setNumberOfQuestionToAmountOfProducts(numberOfQuestionToAmountOfProducts);
        return conversationSummary;
    }
}
