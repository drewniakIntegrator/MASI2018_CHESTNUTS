package com.masi2018.chestnuts.chatbot.controller;

import com.masi2018.chestnuts.chatbot.model.Log;
import com.masi2018.chestnuts.chatbot.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("")
    public ResponseEntity<Log> saveLog(@RequestBody Log log) {
        return ResponseEntity.ok(logService.saveLog(log));
    }

    @GetMapping("")
    public ResponseEntity<List<Log>> getLogs() {
        return ResponseEntity.ok(logService.getLogs());
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Log>> getLogsByConversationId(
            @PathVariable("conversationId") String conversationId) {
        return ResponseEntity.ok(logService.getLogsByConversationId(conversationId));
    }
}
