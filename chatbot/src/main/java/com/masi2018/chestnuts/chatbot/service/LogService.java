package com.masi2018.chestnuts.chatbot.service;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.Log;
import com.masi2018.chestnuts.chatbot.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return logRepository.findAllByConversationIdOrderByTimeAsc(conversationId);
    }

    public void saveLogs(MessageResponse watsonResponse) {
        logRepository.saveAll(prepareLogs(watsonResponse));
    }

    private List<Log> prepareLogs(MessageResponse watsonResponse) {
        List<Log> logs = new ArrayList<>();
        logs.add(prepareUserLog(watsonResponse));
        logs.add(prepareWatsonLog(watsonResponse));
        return logs;
    }

    private Log prepareWatsonLog(MessageResponse watsonResponse) {
        return Log
                .builder()
                .content(watsonResponse.getOutput().getText().get(0))
                .conversationId(watsonResponse.getContext().getConversationId())
                .sender("watson")
                .time(System.nanoTime())
                .build();
    }

    private Log prepareUserLog(MessageResponse watsonResponse) {
        return Log
                .builder()
                .content(watsonResponse.getInput().getText())
                .conversationId(watsonResponse.getContext().getConversationId())
                .sender("user")
                .time(System.nanoTime())
                .build();
    }
}
