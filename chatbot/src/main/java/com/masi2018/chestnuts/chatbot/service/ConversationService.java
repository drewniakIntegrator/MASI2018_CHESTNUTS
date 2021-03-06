package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.BotRequest;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class ConversationService {

    private final WatsonConnector watsonConnector;

    private final AmazonConnector amazonConnector;

    private final ResponseService responseService;

    private final QueryBuilder queryBuilder;

    private final ConversationSummaryService conversationSummaryService;

    @Autowired
    public ConversationService(WatsonConnector watsonConnector, AmazonConnector amazonConnector, ResponseService responseService, QueryBuilder queryBuilder, ConversationSummaryService conversationSummaryService) {
        this.watsonConnector = watsonConnector;
        this.amazonConnector = amazonConnector;
        this.responseService = responseService;
        this.queryBuilder = queryBuilder;
        this.conversationSummaryService = conversationSummaryService;
    }

    public BotResponse sendMessage(BotRequest botRequest) {
        MessageResponse watsonResponse = sendToWatson(botRequest);
        ItemSearchResponse amazonResponse = sendToAmazon(watsonResponse);
        return responseService.prepareResponse(watsonResponse, amazonResponse);
    }

    private MessageResponse sendToWatson(BotRequest botRequest) {
        return watsonConnector.send(botRequest);
    }

    private ItemSearchResponse sendToAmazon(MessageResponse message) {
        ItemSearchRequest query = queryBuilder.buildQuery(message);
        return amazonConnector.send(query);
    }

    public BotResponse initConversation(String username, String userAddress) {
        MessageResponse messageResponse = watsonConnector.initConversation();
        conversationSummaryService.createConversationSummary(
                username, messageResponse.getContext().getConversationId(), userAddress);
        return BotResponse.builder()
                .message(messageResponse.getOutput().getText().get(0))
                .conversationId(messageResponse.getContext().getConversationId())
                .build();
    }
}
