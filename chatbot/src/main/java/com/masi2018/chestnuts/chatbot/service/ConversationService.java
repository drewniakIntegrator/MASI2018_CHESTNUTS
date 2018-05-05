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

    @Autowired
    public ConversationService(WatsonConnector watsonConnector, AmazonConnector amazonConnector, ResponseService responseService, QueryBuilder queryBuilder) {
        this.watsonConnector = watsonConnector;
        this.amazonConnector = amazonConnector;
        this.responseService = responseService;
        this.queryBuilder = queryBuilder;
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

    public List<String> prepareCategoriesTree() {
        throw new NotImplementedException();
    }
}
