package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import com.masi2018.chestnuts.chatbot.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ResponseService {

    private final ConversationSummaryService conversationSummaryService;

    private final LogService logService;

    @Autowired
    public ResponseService(ConversationSummaryService conversationSummaryService, LogService logService) {
        this.conversationSummaryService = conversationSummaryService;
        this.logService = logService;
    }

    public BotResponse prepareResponse(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        List<String> hints = getHintsFromWatsonResponse(watsonResponse);
        boolean isFinal = Boolean.valueOf(
                watsonResponse.getContext().getOrDefault("isFinal", "false").toString());
        String message = getMessageFromWatsonResponse(watsonResponse);
        List<Item> items = buildItemList(amazonResponse);
        saveLogs(watsonResponse, amazonResponse);
        return BotResponse.builder()
                .conversationId(watsonResponse.getContext().getConversationId())
                .message(message)
                .url(amazonResponse.getItems().get(0).getMoreSearchResultsUrl())
                .hints(hints)
                .items(items)
                .isFinal(isFinal)
                .build();
    }

    private String getMessageFromWatsonResponse(MessageResponse watsonResponse) {
        List<String> watsonResponses = watsonResponse.getOutput().getText();
        String message;
        if(watsonResponses.size() > 0) {
            message = watsonResponses.get(0);
            if (message == null || message.equals("")) {
                message = "Error occurred";
            }
        } else {
            message = "Error occurred";
        }
        return message;
    }

    private void saveLogs(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        conversationSummaryService.saveConversationSummary(watsonResponse, amazonResponse);
        logService.saveLogs(watsonResponse);
    }

    private List<Item> buildItemList(ItemSearchResponse amazonResponse) {
        List<Item> items = new ArrayList<>();
        for (am.ik.aws.apa.jaxws.Item amazonItem : amazonResponse.getItems().get(0).getItem()) {
            String imageUrl = getImageUrl(amazonItem);
            getImageUrl(amazonItem);
            Item item = Item
                    .builder()
                    .title(amazonItem.getItemAttributes().getTitle())
                    .url(amazonItem.getDetailPageURL())
                    .imageUrl(imageUrl)
                    .build();
            items.add(item);
        }
        return items;
    }

    private String getImageUrl(am.ik.aws.apa.jaxws.Item amazonItem) {
        if (amazonItem.getLargeImage() != null) {
            return amazonItem.getLargeImage().getURL();
        }
        return null;
    }

    private List<String> getHintsFromWatsonResponse(MessageResponse watsonResponse) {
        String hintsAsString = watsonResponse.getContext().getOrDefault("hints", "").toString();
        hintsAsString = hintsAsString.replace("'", "\"");
        List<String> hints = new ArrayList<>();
        if (hintsAsString != null && !hintsAsString.equals("")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                hints = mapper.readValue(hintsAsString
                        , mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            } catch (IOException e) {
                log.info("Can't parse hints");
                return Collections.emptyList();
            }
        }
        return hints;
    }
}
