package com.masi2018.chestnuts.chatbot.model;

import com.ibm.watson.developer_cloud.conversation.v1.model.SystemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "conversationData")
public class ConversationData {

    @Id
    private String id;

    private SystemResponse systemResponse;

    private String conversationId;

    private String searchIndex;

    @Builder.Default
    private Map<String, String> searchParameters = new HashMap<>();
}
