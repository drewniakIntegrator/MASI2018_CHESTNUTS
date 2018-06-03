package com.masi2018.chestnuts.chatbot;

import com.masi2018.chestnuts.chatbot.model.Log;
import com.mongodb.client.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoDbIntegrationTests {

    private String collectionName;
    private Log logToInsert;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void before() {
        collectionName = "log";
        mongoTemplate.dropCollection(collectionName);
        logToInsert = new Log("1", "1", "sender", "content", new Date().getTime());
    }

    @After
    public void after() {
        mongoTemplate.dropCollection(collectionName);
    }

    @Test
    public void checkMongoTemplate() {
        assertNotNull(mongoTemplate);
        MongoCollection createdCollection = mongoTemplate.createCollection(collectionName);
        assertTrue(mongoTemplate.collectionExists(collectionName));
    }

    @Test
    public void checkDocumentAndQuery() {
        mongoTemplate.save(logToInsert, collectionName);
        Query query = new Query(new Criteria()
                .andOperator(Criteria.where("sender").regex(logToInsert.getSender()),
                        Criteria.where("content").regex(logToInsert.getContent())));

        Log retrievedLogRecord = mongoTemplate.findOne(query, Log.class, collectionName);
        assertNotNull(retrievedLogRecord);
    }
}
