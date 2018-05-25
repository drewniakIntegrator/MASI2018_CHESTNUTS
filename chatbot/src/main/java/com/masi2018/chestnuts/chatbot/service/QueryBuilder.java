package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemLookupRequest;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeIntent;
import com.masi2018.chestnuts.chatbot.model.ConversationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryBuilder {

    private static final String MOVIE_ACTOR_PARAMETER_NAME = "actor";

    private static final String BOOK_AUTHOR_PARAMETER_NAME = "author";

    private static final String MOVIE_DIRECTOR_PARAMETER_NAME = "director";

    private static final String MOVIE_AUDIENCE_RATING_PARAMETER_NAME = "movie_audience_rating";

    private static final String MOVIE_AVAILABILITY_PARAMETER_NAME = "movie_avaliability";

    private static final String MOVIE_FORMAT_PARAMETER_NAME = "movie_format";

    private static final String MOVIE_KEYWORDS_PARAMETER_NAME = "movie_keywords";

    private static final String MOVIE_PUBLISHER_PARAMETER_NAME = "movie_publisher";
    private final ConversationDataService conversationDataService;

    @Autowired
    public QueryBuilder(ConversationDataService conversationDataService) {
        this.conversationDataService = conversationDataService;
    }

    public ItemSearchRequest buildQuery(MessageResponse message) {
        return prepareItemSearchRequest(message);
    }

    private ItemSearchRequest prepareItemSearchRequest(MessageResponse message) {
        ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
        String conversationId = message.getContext().getConversationId();
        ConversationData conversationData = getConversationData(conversationId);
        String searchIndex = setSearchIndex(message, conversationData);
        itemSearchRequest.setSearchIndex(searchIndex);
        Map<String, String> searchParameters = getSearchParametersForConversation(message.getContext(), conversationData);
        setParameters(searchParameters, itemSearchRequest);
        conversationData.setSearchIndex(searchIndex);
        conversationDataService.save(conversationData);
        setResponseGroups(itemSearchRequest);
        return itemSearchRequest;
    }

    private void setResponseGroups(ItemSearchRequest itemSearchRequest) {
        try {
            List<String> responseGroup = new ArrayList<>();
            responseGroup.add("Images");
            responseGroup.add("ItemAttributes");
            Class<?> c = itemSearchRequest.getClass();
            Field responseGroupField = c.getDeclaredField("responseGroup");
            responseGroupField.setAccessible(true);
            responseGroupField.set(itemSearchRequest, responseGroup);
            responseGroupField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ConversationData getConversationData(String conversationId) {
        ConversationData conversationData = conversationDataService.findByConversationId(conversationId);
        if (conversationData == null) {
            conversationData = new ConversationData();
            conversationData.setConversationId(conversationId);
        }
        return conversationData;
    }

    private String setSearchIndex(MessageResponse message, ConversationData conversationData) {
        List<String> intents = message.getIntents().stream().map(RuntimeIntent::getIntent).collect(Collectors.toList());
        String searchIndex;
        if (conversationData != null && conversationData.getSearchIndex() != null && !conversationData.getSearchIndex().equals("")) {
            searchIndex = conversationData.getSearchIndex();
        } else {
            searchIndex = getSearchIndexBasedOnIntents(intents);
        }
        return searchIndex;
    }

    private String getSearchIndexBasedOnIntents(List<String> intents) {
        if (intents.contains("watch_movie")) {
            return "DVD";
        } else if (intents.contains("read_book")) {
            return "Books";
        }
        return null;
    }

    private void setParameters(Map<String, String> searchParameters, ItemSearchRequest itemSearchRequest) {
        itemSearchRequest.setActor(searchParameters.getOrDefault(MOVIE_ACTOR_PARAMETER_NAME, null));
        itemSearchRequest.setDirector(searchParameters.getOrDefault(MOVIE_DIRECTOR_PARAMETER_NAME, null));
        itemSearchRequest.setPublisher(searchParameters.getOrDefault(MOVIE_PUBLISHER_PARAMETER_NAME, null));
        itemSearchRequest.setAvailability(searchParameters.getOrDefault(MOVIE_AVAILABILITY_PARAMETER_NAME, null));
        itemSearchRequest.setKeywords(searchParameters.getOrDefault(MOVIE_KEYWORDS_PARAMETER_NAME, null));
        }

    private Map<String, String> getSearchParametersForConversation(Context context, ConversationData conversationData) {
        return prepareSearchParameters(context, conversationData.getSearchParameters());
    }

    private Map<String, String> prepareSearchParameters(Context context, Map<String, String> searchParameters) {
        List<String> parameterNames = prepareParameterNames();
        for (String parameterName : parameterNames) {
            checkIfContextContainAndUpdateData(context, searchParameters, parameterName);
        }
        return searchParameters;
    }

    private List<String> prepareParameterNames() {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add(MOVIE_ACTOR_PARAMETER_NAME);
        parameterNames.add(BOOK_AUTHOR_PARAMETER_NAME);
        parameterNames.add(MOVIE_DIRECTOR_PARAMETER_NAME);
        parameterNames.add(MOVIE_AUDIENCE_RATING_PARAMETER_NAME);
        parameterNames.add(MOVIE_AVAILABILITY_PARAMETER_NAME);
        parameterNames.add(MOVIE_FORMAT_PARAMETER_NAME);
        parameterNames.add(MOVIE_KEYWORDS_PARAMETER_NAME);
        parameterNames.add(MOVIE_PUBLISHER_PARAMETER_NAME);
        return parameterNames;
    }

    private void checkIfContextContainAndUpdateData(Context context, Map<String, String> searchParameters
            , String parameterName) {
        if (context.containsKey(parameterName)) {
            searchParameters.put(parameterName, context.get(parameterName).toString());
        }
    }
}
