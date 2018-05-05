package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class QueryBuilder {

    public ItemSearchRequest buildQuery(MessageResponse message) {
        return prepareItemSearchRequest();
    }

    private ItemSearchRequest prepareItemSearchRequest() {
        ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
        itemSearchRequest.setSearchIndex("Movies");
        return itemSearchRequest;
    }
}
