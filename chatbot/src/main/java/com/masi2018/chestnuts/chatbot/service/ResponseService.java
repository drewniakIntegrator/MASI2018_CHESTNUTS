package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import com.masi2018.chestnuts.chatbot.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ResponseService {

    public BotResponse prepareResponse(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        String hintsAsString = watsonResponse.getContext().getOrDefault("hints", "").toString();
        List<String> hints = getHintsFromWatsonResponse(hintsAsString);
        boolean isFinal = Boolean.valueOf(
                watsonResponse.getContext().getOrDefault("isFinal", "false").toString());
        String message = watsonResponse.getOutput().getText().get(0);
        if (message == null || message.equals("")) {
            message = "Error occurred";
        }
        List<Item> items = buildItemList(amazonResponse);
        return BotResponse.builder()
                .conversationId(watsonResponse.getContext().getConversationId())
                .message(message)
                .url(amazonResponse.getItems().get(0).getMoreSearchResultsUrl())
                .hints(hints)
                .items(items)
                .isFinal(isFinal)
                .build();
    }

    private List<Item> buildItemList(ItemSearchResponse amazonResponse) {
        List<Item> items = new ArrayList<>();
        for (am.ik.aws.apa.jaxws.Item amazonItem : amazonResponse.getItems().get(0).getItem()) {
            Item item = Item
                    .builder()
                    .title(amazonItem.getItemAttributes().getTitle())
                    .url(amazonItem.getDetailPageURL())
                    .imageUrl(amazonItem.getLargeImage().getURL())
                    .build();
            items.add(item);
        }
        return items;
    }

    private List<String> getHintsFromWatsonResponse(String hintsAsString) {
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
