package com.masi2018.chestnuts.chatbot.repository;

import com.masi2018.chestnuts.chatbot.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    List<Log> findAllByConversationId(String conversationId);
}
