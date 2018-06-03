package com.masi2018.chestnuts.chatbot;

import com.masi2018.chestnuts.chatbot.model.Log;
import com.masi2018.chestnuts.chatbot.repository.LogRepository;
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

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LogRepository logRepository;

    @Before
    public void before() {
        Log logRecord = new Log("0", "0", "sender", "content", new Date().getTime());
        logRepository.insert(logRecord);
    }

    @After
    public void after() {
        logRepository.deleteById("0");
        logRepository.deleteById("1");
    }

    @Test
    public void saveLogTest() {
        Log logRecord = new Log("1", "1", "sender", "content", new Date().getTime());
        ResponseEntity<Log> responseEntity = restTemplate.postForEntity(
                "/logs", logRecord, Log.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals("sender", responseEntity.getBody().getSender());

        Optional<Log> foundLog = logRepository.findById("1");
        assertEquals(foundLog.get().getId(), responseEntity.getBody().getId());

        List<Log> foundLogs = logRepository.findAllByConversationIdOrderByTimeAsc(responseEntity.getBody().getConversationId());
        assertNotNull(foundLogs);
    }

    @Test
    public void getLogsTest() {
        ResponseEntity<Log[]> responseEntity = restTemplate.getForEntity(
                "/logs", Log[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotEquals(0, responseEntity.getBody().length);
    }

    @Test
    public void getLogsByConversationIdTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("conversationId", 0);

        ResponseEntity<Log[]> responseEntity = restTemplate.getForEntity(
                "/logs/conversation/{conversationId}", Log[].class, params);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotEquals(0, responseEntity.getBody().length);
        assertEquals(responseEntity.getBody().length, logRepository.findAllByConversationIdOrderByTimeAsc("0").size());
    }
}
