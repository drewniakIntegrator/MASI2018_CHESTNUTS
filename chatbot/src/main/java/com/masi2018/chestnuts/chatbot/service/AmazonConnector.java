package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import org.springframework.stereotype.Service;

@Service
public class AmazonConnector {

    public ItemSearchResponse sendToAmazon(ItemSearchRequest request) {
        AwsApaRequester requester = new AwsApaRequesterImpl();
        return requester.itemSearch(request);
    }
}
