package com.masi2018.chestnuts.chatbot;

import com.masi2018.chestnuts.chatbot.model.BotRequest;
import com.masi2018.chestnuts.chatbot.model.BotResponse;
import com.masi2018.chestnuts.chatbot.model.ConversationReport;
import com.masi2018.chestnuts.chatbot.model.ScoreRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversationRestControllerTest {

    private String url = "/conversations/init";
    private String conversationId;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void before() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username", "user");

        ResponseEntity<BotResponse> responseEntity = restTemplate.getForEntity(builder.build().toUri(), BotResponse.class);
        conversationId = responseEntity.getBody().getConversationId();
    }

    @After
    public void after() {
        conversationId = null;
    }

    @Test
    public void sendUserMessageTest() {
        BotRequest botRequest = new BotRequest(conversationId, "message");
        ResponseEntity<BotResponse> responseEntity = restTemplate.postForEntity(
                "/conversations", botRequest, BotResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(conversationId, responseEntity.getBody().getConversationId());
    }

    @Test
    public void initConversationTest() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("username", "user1");

        ResponseEntity<BotResponse> responseEntity = restTemplate.getForEntity(builder.build().toUri(), BotResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(responseEntity.getBody().getMessage(), "Hello. How can I help you?");
    }

    @Test
    public void sendScores() {
        ScoreRequest scoreRequest = new ScoreRequest(10, 10);

        Map<String, Object> params = new HashMap<>();
        params.put("conversationId", conversationId);

        ResponseEntity responseEntity = restTemplate.postForEntity(
                "/conversations/{conversationId}/score", scoreRequest, null, params);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getReportTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("conversationId", conversationId);

        ResponseEntity<ConversationReport> responseEntity = restTemplate.getForEntity(
                "/conversations/{conversationId}/report", ConversationReport.class, params);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
    }
}
