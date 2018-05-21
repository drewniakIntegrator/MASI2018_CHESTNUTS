package com.masi2018.chestnuts.chatbot.service;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.*;
import com.masi2018.chestnuts.chatbot.model.BotRequest;
import com.masi2018.chestnuts.chatbot.model.ConversationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class WatsonConnector {

    @Value("${watson.version}")
    private String version;

    @Value("${watson.username}")
    private String username;

    @Value("${watson.password}")
    private String password;

    @Value("${watson.defaultApiEndpoint}")
    private String defaultApiEndpoint;

    @Value("${watson.workspace.id}")
    private String workspaceId;

    private Conversation conversation;

    private ConversationDataService conversationDataService;

    @Autowired
    public WatsonConnector(ConversationDataService conversationDataService) {
        this.conversationDataService = conversationDataService;
    }

    @PostConstruct
    public void createConversation() {
        this.conversation = new Conversation(
                version);
        conversation.setUsernameAndPassword(username, password);
        conversation.setEndPoint(defaultApiEndpoint);
    }

    public MessageResponse send(BotRequest botRequest) {
        InputData inputData = new InputData.Builder(botRequest.getMessage()).build();
        MessageOptions messageOptions = new MessageOptions.Builder(workspaceId).context(new Context()).input(inputData).build();
        prepareMessageOptionsBasedOnConversation(botRequest.getConversationId(), messageOptions);
        MessageResponse messageResponse = conversation.message(messageOptions).execute();
        conversationDataService.createOrUpdateSystemResponse(messageResponse.getContext().getConversationId(), messageResponse.getContext().getSystem());
        return messageResponse;
    }

    private void prepareMessageOptionsBasedOnConversation(String conversationId, MessageOptions messageOptions) {
        if (conversationId != null) {
            messageOptions.context().setConversationId(conversationId);
            ConversationData conversationData = conversationDataService.findByConversationId(conversationId);
            messageOptions.context().setSystem(conversationData.getSystemResponse());
        }
    }

    public MessageResponse initConversation() {
        InputData inputData = new InputData.Builder("hi").build();
        MessageOptions messageOptions = new MessageOptions.Builder(workspaceId).context(new Context()).input(inputData).build();
        MessageResponse messageResponse = conversation.message(messageOptions).execute();
        conversationDataService.createOrUpdateSystemResponse(messageResponse.getContext().getConversationId(), messageResponse.getContext().getSystem());
        return messageResponse;
    }
}
