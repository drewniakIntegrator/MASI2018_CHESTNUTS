package com.masi2018.chestnuts.chatbot.repository;

import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationSummaryRepository extends MongoRepository<ConversationSummary, String> {

    ConversationSummary findByConversationId(String conversationId);

    List<ConversationSummary> findAllByUsernameAndUserAddress(String username, String userAddress);

}
