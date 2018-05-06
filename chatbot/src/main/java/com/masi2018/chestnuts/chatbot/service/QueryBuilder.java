package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryBuilder {

    private static final String GENRE_PARAMETER_NAME = "genre";

    private static final String FORMAT_PARAMETER_NAME = "format";

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
        itemSearchRequest.setSearchIndex("Movies");
        setParametersOfItemSearchRequest(itemSearchRequest, message.getContext());
        return itemSearchRequest;
    }

    private void setParametersOfItemSearchRequest(ItemSearchRequest itemSearchRequest, Context context) {
        Map<String, String> searchParameters = getSearchParametersForConversation(context);
        itemSearchRequest.setKeywords(buildKeywords(searchParameters));
    }

    private String buildKeywords(Map<String, String> searchParameters) {
        StringBuilder keyword = new StringBuilder();
        for(String value: searchParameters.values()) {
            keyword.append(value).append(", ");
        }
        String result = keyword.toString();
        return (!result.equals("")) ? result: null;
    }

    private Map<String, String> getSearchParametersForConversation(Context context) {
        ConversationData conversationData = conversationDataService
                .findByConversationId(context.getConversationId());
        Map<String, String> searchParameters = prepareSearchParameters(context, conversationData.getSearchParameters());
        conversationDataService.save(conversationData);
        return searchParameters;
    }

    private Map<String, String> prepareSearchParameters(Context context, Map<String, String> searchParameters) {
        List<String> parameterNames = prepareParameterNames();
        for(String parameterName : parameterNames) {
            checkIfContextContainAndUpdateData(context, searchParameters, parameterName);
        }
        return searchParameters;
    }

    private List<String> prepareParameterNames() {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add(GENRE_PARAMETER_NAME);
        parameterNames.add(FORMAT_PARAMETER_NAME);
        return parameterNames;
    }

    private void checkIfContextContainAndUpdateData(Context context, Map<String, String> searchParameters
            , String parameterName) {
        if (context.containsKey(parameterName)) {
            searchParameters.put(parameterName, context.get(parameterName).toString());
        }
    }
}
