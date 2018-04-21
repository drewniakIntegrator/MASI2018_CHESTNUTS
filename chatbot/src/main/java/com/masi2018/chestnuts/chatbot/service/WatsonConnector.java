package com.masi2018.chestnuts.chatbot.service;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
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

    public MessageResponse send(String message) {
        InputData inputData = new InputData.Builder(message).build();
        MessageOptions messageOptions = new MessageOptions.Builder(workspaceId).input(inputData).build();
        return conversation.message(messageOptions).execute();
    }
}
