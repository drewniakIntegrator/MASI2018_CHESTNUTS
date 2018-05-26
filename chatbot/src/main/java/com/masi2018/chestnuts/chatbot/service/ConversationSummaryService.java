package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
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
        //@TODO  implement
        return ConversationSummary.builder().build();
    }
}
