package com.masi2018.chestnuts.chatbot.repository;

import com.masi2018.chestnuts.chatbot.model.ConversationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationDataRepository extends MongoRepository<ConversationData, String> {
    ConversationData findOneByConversationId(String conversationId);
}
