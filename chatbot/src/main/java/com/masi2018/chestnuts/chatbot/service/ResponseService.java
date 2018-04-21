package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public String prepareResponse(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        return "";
    }
}
