package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeIntent;
import com.masi2018.chestnuts.chatbot.model.ConversationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryBuilder {

    private static final String MOVIE_ACTOR_PARAMETER_NAME = "actor";

    private static final String MOVIE_DIRECTOR_PARAMETER_NAME = "director";

    private static final String MOVIE_AUDIENCE_RATING_PARAMETER_NAME = "movieRating";

    private static final String MOVIE_AVAILABILITY_PARAMETER_NAME = "movieAvaliability";

    private static final String MOVIE_KEYWORDS_PARAMETER_NAME = "movieKeywords";

    private static final String MOVIE_PUBLISHER_PARAMETER_NAME = "moviePublisher";

    private static final String BOOK_AUTHOR_PARAMETER_NAME = "author";

    private static final String BOOK_AVAILABILITY_PARAMETER_NAME = "bookAvaliability";

    private static final String BOOK_KEYWORDS_PARAMETER_NAME = "bookKeywords";

    private static final String BOOK_PUBLICATION_DATE_PARAMETER_NAME = "bookPublicationDate";

    private static final String BOOK_PUBLISHER_PARAMETER_NAME = "bookPublisher";

    private static final String MOVIE_SEARCH_INDEX = "DVD";

    private static final String BOOK_SEARCH_INDEX = "Books";

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
        conversationData.setSearchIndex(searchIndex);
        Map<String, String> searchParameters = getSearchParametersForConversation(message.getContext(), conversationData);
        setParameters(searchParameters, itemSearchRequest);
        conversationDataService.save(conversationData);
        itemSearchRequest.getResponseGroup().add("Images");
        itemSearchRequest.getResponseGroup().add("ItemAttributes");
        return itemSearchRequest;
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
            return MOVIE_SEARCH_INDEX;
        } else if (intents.contains("read_book")) {
            return BOOK_SEARCH_INDEX;
        }
        return "";
    }

    private void setParameters(Map<String, String> searchParameters, ItemSearchRequest itemSearchRequest) {
        switch (itemSearchRequest.getSearchIndex()) {
            case MOVIE_SEARCH_INDEX:
                fillMovieParameters(searchParameters, itemSearchRequest);
                break;
            case BOOK_SEARCH_INDEX:
                fillBookParameters(searchParameters, itemSearchRequest);
                break;
        }
    }

    private void fillMovieParameters(Map<String, String> searchParameters, ItemSearchRequest itemSearchRequest) {
        itemSearchRequest.setActor(searchParameters.getOrDefault(MOVIE_ACTOR_PARAMETER_NAME, null));
        itemSearchRequest.setDirector(searchParameters.getOrDefault(MOVIE_DIRECTOR_PARAMETER_NAME, null));
        itemSearchRequest.setPublisher(searchParameters.getOrDefault(MOVIE_PUBLISHER_PARAMETER_NAME, null));
        itemSearchRequest.setAvailability(searchParameters.getOrDefault(MOVIE_AVAILABILITY_PARAMETER_NAME, null));
        itemSearchRequest.getAudienceRating().add(searchParameters.getOrDefault(MOVIE_AUDIENCE_RATING_PARAMETER_NAME, null));
        itemSearchRequest.setKeywords(searchParameters.getOrDefault(MOVIE_KEYWORDS_PARAMETER_NAME, ""));
    }

    private void fillBookParameters(Map<String, String> searchParameters, ItemSearchRequest itemSearchRequest) {
        itemSearchRequest.setAuthor(searchParameters.getOrDefault(BOOK_AUTHOR_PARAMETER_NAME, null));
        itemSearchRequest.setAvailability(searchParameters.getOrDefault(BOOK_AVAILABILITY_PARAMETER_NAME, null));
        itemSearchRequest.setKeywords(searchParameters.getOrDefault(BOOK_KEYWORDS_PARAMETER_NAME, ""));
        itemSearchRequest.setReleaseDate(searchParameters.getOrDefault(BOOK_PUBLICATION_DATE_PARAMETER_NAME, null));
        itemSearchRequest.setPublisher(searchParameters.getOrDefault(BOOK_PUBLISHER_PARAMETER_NAME, null));
    }

    private Map<String, String> getSearchParametersForConversation(Context context, ConversationData conversationData) {
        return prepareSearchParameters(context, conversationData.getSearchParameters(), conversationData.getSearchIndex());
    }

    private Map<String, String> prepareSearchParameters(Context context, Map<String, String> searchParameters, String searchIndex) {
        List<String> parameterNames = prepareParameterNames(searchIndex);
        for (String parameterName : parameterNames) {
            checkIfContextContainAndUpdateData(context, searchParameters, parameterName);
        }
        return searchParameters;
    }

    private List<String> prepareParameterNames(String searchIndex) {
        switch (searchIndex) {
            case MOVIE_SEARCH_INDEX:
                return prepareMovieParameterNames();
            case BOOK_SEARCH_INDEX:
                return prepareBookParameterName();
            default:
                return Collections.emptyList();
        }
    }

    private List<String> prepareBookParameterName() {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add(BOOK_AUTHOR_PARAMETER_NAME);
        parameterNames.add(BOOK_AVAILABILITY_PARAMETER_NAME);
        parameterNames.add(BOOK_KEYWORDS_PARAMETER_NAME);
        parameterNames.add(BOOK_PUBLICATION_DATE_PARAMETER_NAME);
        parameterNames.add(BOOK_PUBLISHER_PARAMETER_NAME);
        return parameterNames;
    }

    private List<String> prepareMovieParameterNames() {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add(MOVIE_ACTOR_PARAMETER_NAME);
        parameterNames.add(MOVIE_DIRECTOR_PARAMETER_NAME);
        parameterNames.add(MOVIE_AUDIENCE_RATING_PARAMETER_NAME);
        parameterNames.add(MOVIE_AVAILABILITY_PARAMETER_NAME);
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
