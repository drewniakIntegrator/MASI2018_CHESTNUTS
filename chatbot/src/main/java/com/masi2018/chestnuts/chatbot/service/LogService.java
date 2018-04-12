package com.masi2018.chestnuts.chatbot.service;

import com.masi2018.chestnuts.chatbot.model.Log;
import com.masi2018.chestnuts.chatbot.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    public List<Log> getLogs() {
        return logRepository.findAll();
    }

    public List<Log> getLogsByConversationId(String conversationId) {
        return logRepository.findAllByConversationId(conversationId);
    }
}
