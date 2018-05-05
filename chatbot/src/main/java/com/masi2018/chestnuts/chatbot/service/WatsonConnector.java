package com.masi2018.chestnuts.chatbot.service;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.*;
import com.masi2018.chestnuts.chatbot.model.BotRequest;
import lombok.extern.slf4j.Slf4j;
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
        messageOptions.context().setConversationId(botRequest.getConversationId());
        return conversation.message(messageOptions).execute();
    }
}
