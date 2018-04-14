package com.masi2018.chestnuts.chatbot.controller;

import com.masi2018.chestnuts.chatbot.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/messages")
public class ConversationRestController {

    private final ConversationService conversationService;

    @Autowired
    public ConversationRestController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    private ResponseEntity<String> sendUserMessage(@RequestBody String message) {
        return ResponseEntity.ok(conversationService.prepareForWatson(message));
    }

    @GetMapping(path = "/categories")
    private ResponseEntity<List<String>> getCategoriesTree() {
        return ResponseEntity.ok(conversationService.prepareCategoriesTree());
    }
}
