package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchRequest;
import org.springframework.stereotype.Service;

@Service
public class QueryBuilder {

    public ItemSearchRequest buildQuery(String message) {
        return new ItemSearchRequest();
    }
}
