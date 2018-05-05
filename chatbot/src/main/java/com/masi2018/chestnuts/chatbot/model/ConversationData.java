package com.masi2018.chestnuts.chatbot.model;

import com.ibm.watson.developer_cloud.conversation.v1.model.SystemResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "conversationData")
public class ConversationData {

    @Id
    private String id;

    private SystemResponse systemResponse;

    private String conversationId;
}
