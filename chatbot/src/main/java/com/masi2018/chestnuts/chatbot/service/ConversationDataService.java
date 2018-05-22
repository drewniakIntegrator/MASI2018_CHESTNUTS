package com.masi2018.chestnuts.chatbot.service;

import com.ibm.watson.developer_cloud.conversation.v1.model.SystemResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationData;
import com.masi2018.chestnuts.chatbot.repository.ConversationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationDataService {

    private final ConversationDataRepository conversationDataRepository;

    @Autowired
    public ConversationDataService(ConversationDataRepository conversationDataRepository) {
        this.conversationDataRepository = conversationDataRepository;
    }

    public ConversationData findByConversationId(String conversationId) {
        return conversationDataRepository.findOneByConversationId(conversationId);
    }

    public void createOrUpdateSystemResponse(String conversationId, SystemResponse systemResponse) {
        ConversationData conversationData = conversationDataRepository.findOneByConversationId(conversationId);
        if (conversationData != null) {
            conversationData.setSystemResponse(systemResponse);
        } else {
            conversationData = ConversationData
                    .builder()
                    .conversationId(conversationId)
                    .systemResponse(systemResponse)
                    .build();
        }
        conversationDataRepository.save(conversationData);
    }

    public void save(ConversationData conversationData) {
        conversationDataRepository.save(conversationData);
    }
}
