package com.masi2018.chestnuts.chatbot.controller;

import com.masi2018.chestnuts.chatbot.model.BotRequest;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationReport;
import com.masi2018.chestnuts.chatbot.model.ScoreRequest;
import com.masi2018.chestnuts.chatbot.service.ConversationService;
import com.masi2018.chestnuts.chatbot.service.ConversationSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/conversations")
public class ConversationRestController {

    private final ConversationService conversationService;

    private final ConversationSummaryService conversationSummaryService;

    @Autowired
    public ConversationRestController(ConversationService conversationService, ConversationSummaryService conversationSummaryService) {
        this.conversationService = conversationService;
        this.conversationSummaryService = conversationSummaryService;
    }

    @PostMapping
    private ResponseEntity<BotResponse> sendUserMessage(@RequestBody BotRequest botRequest) {
        return ResponseEntity.ok(conversationService.sendMessage(botRequest));
    }

    @GetMapping(value = "/init")
    private ResponseEntity<BotResponse> initConversation(@RequestParam String username, HttpServletRequest request) {
        return ResponseEntity.ok(conversationService.initConversation(username, request.getRemoteAddr()));
    }

    @GetMapping(path = "/categories")
    private ResponseEntity<List<String>> getCategoriesTree() {
        return ResponseEntity.ok(conversationService.prepareCategoriesTree());
    }

    @PostMapping(path = "/{conversationId}/score")
    private ResponseEntity sendScores(@PathVariable String conversationId, @RequestBody ScoreRequest scoreRequest) {
        if (conversationSummaryService.scoreConversation(conversationId, scoreRequest)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).body("Conversation with this id doesn't exist");
        }
    }

    @GetMapping(path = "/{conversationId}/report")
    private ResponseEntity<ConversationReport> getReport(@PathVariable String conversationId) {
        return ResponseEntity.ok(conversationSummaryService.getReport(conversationId));
    }
}
