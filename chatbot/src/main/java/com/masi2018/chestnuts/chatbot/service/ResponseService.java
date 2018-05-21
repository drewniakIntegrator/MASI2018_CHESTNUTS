package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public BotResponse prepareResponse(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        return BotResponse.builder()
                .conversationId(watsonResponse.getContext().getConversationId())
                .message(watsonResponse.getOutput().getText().get(0))
                .url(amazonResponse.getItems().get(0).getMoreSearchResultsUrl())
                .build();
    }
}
