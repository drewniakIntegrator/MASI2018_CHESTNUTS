package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
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

        return BotResponse.builder()
                .conversationId(watsonResponse.getContext().getConversationId())
                .message(watsonResponse.getOutput().getText().get(0))
                .url(amazonResponse.getItems().get(0).getMoreSearchResultsUrl())
                .hints(hints)
                .build();
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
