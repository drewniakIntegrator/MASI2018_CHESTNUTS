package com.masi2018.chestnuts.chatbot.repository;

import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationSummaryRepository extends MongoRepository<ConversationSummary, String> {

    ConversationSummary findFirstByConversationId(String conversationId);

}
