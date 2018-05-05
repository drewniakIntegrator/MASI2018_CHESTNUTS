package com.masi2018.chestnuts.chatbot.controller;

import com.masi2018.chestnuts.chatbot.model.BotResponse;
import com.masi2018.chestnuts.chatbot.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/conversations")
public class ConversationRestController {

    private final ConversationService conversationService;

    @Autowired
    public ConversationRestController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    private ResponseEntity<BotResponse> sendUserMessage(@RequestBody String message) {
        return ResponseEntity.ok(conversationService.sendMessage(message));
    }

    @GetMapping(path = "/categories")
    private ResponseEntity<List<String>> getCategoriesTree() {
        return ResponseEntity.ok(conversationService.prepareCategoriesTree());
    }
}