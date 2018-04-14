package com.masi2018.chestnuts.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class ConversationService {

    private final WatsonConnector watsonConnector;

    private final AmazonConnector amazonConnector;

    private final QueryBuilder queryBuilder;

    @Autowired
    public ConversationService(WatsonConnector watsonConnector, AmazonConnector amazonConnector, QueryBuilder queryBuilder) {
        this.watsonConnector = watsonConnector;
        this.amazonConnector = amazonConnector;
        this.queryBuilder = queryBuilder;
    }

    public String prepareForWatson(String message) {
        return watsonConnector.sendToWatson(message);
    }

    public String prepareForAmazon(String message) {
        String query = queryBuilder.buildQuery(message);
        return amazonConnector.sendToAmazon(query);
    }

    public List<String> prepareCategoriesTree() {
        throw new NotImplementedException();
    }
}
