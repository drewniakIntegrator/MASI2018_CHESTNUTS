package com.masi2018.chestnuts.chatbot.service;

import am.ik.aws.apa.jaxws.ItemSearchResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.masi2018.chestnuts.chatbot.model.AllConversationsStatistics;
import com.masi2018.chestnuts.chatbot.model.ConversationReport;
import com.masi2018.chestnuts.chatbot.model.ConversationSummary;
import com.masi2018.chestnuts.chatbot.model.ScoreRequest;
import com.masi2018.chestnuts.chatbot.repository.ConversationSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationSummaryService {

    private final ConversationSummaryRepository conversationSummaryRepository;

    @Autowired
    public ConversationSummaryService(ConversationSummaryRepository conversationSummaryRepository) {
        this.conversationSummaryRepository = conversationSummaryRepository;
    }

    public void saveConversationSummary(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        ConversationSummary conversationSummary = prepareConversationSummary(watsonResponse, amazonResponse);
        conversationSummaryRepository.save(conversationSummary);
    }

    private ConversationSummary prepareConversationSummary(MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        ConversationSummary conversationSummary = getOrCreateConversationSummary(watsonResponse);
        fillConversationSummary(conversationSummary, watsonResponse, amazonResponse);
        return conversationSummary;
    }

    private void fillConversationSummary(ConversationSummary conversationSummary
            , MessageResponse watsonResponse, ItemSearchResponse amazonResponse) {
        boolean isMisunderstand = Boolean.valueOf(watsonResponse.getContext()
                .getOrDefault("anythingElse", "false").toString());
        if (isMisunderstand) {
            conversationSummary.setAmountOfMisunderstoodQuestions(
                    conversationSummary.getAmountOfMisunderstoodQuestions() + 1);
        } else {
            conversationSummary.setAmountOfQuestions(
                    conversationSummary.getAmountOfQuestions() + 1);
            conversationSummary.getNumberOfQuestionToAmountOfProducts().put(
                    conversationSummary.getAmountOfQuestions(), amazonResponse.getItems().get(0).getTotalResults());
        }
    }

    private ConversationSummary getOrCreateConversationSummary(MessageResponse watsonResponse) {
        ConversationSummary conversationSummary = conversationSummaryRepository
                .findByConversationId(watsonResponse.getContext().getConversationId());
        if (conversationSummary == null) {
            conversationSummary = ConversationSummary
                    .builder()
                    .conversationId(watsonResponse.getContext().getConversationId())
                    .build();
        }
        return conversationSummary;
    }

    public boolean scoreConversation(String conversationId, ScoreRequest scoreRequest) {
        ConversationSummary conversationSummary = conversationSummaryRepository.findByConversationId(conversationId);
        if (conversationSummary == null) {
            return false;
        }
        conversationSummary.setUsabilityScore(scoreRequest.getUsabilityScore());
        conversationSummary.setEffectivenessScore(scoreRequest.getEffectivenessScore());
        conversationSummaryRepository.save(conversationSummary);
        return true;
    }

    public ConversationReport getReport(String conversationId) {
        ConversationReport conversationReport = new ConversationReport();
        ConversationSummary conversationSummary = conversationSummaryRepository.findByConversationId(conversationId);
        conversationReport.setConversationSummary(conversationSummary);
        List<ConversationSummary> conversationSummaries = conversationSummaryRepository
                .findAllByUsernameAndUserAddress(conversationSummary.getUsername()
                        , conversationSummary.getUserAddress());
        conversationReport.setAllConversationsStatistics(prepareAllConversationStatisticsForUser(conversationSummaries));
        return conversationReport;
    }

    private AllConversationsStatistics prepareAllConversationStatisticsForUser(List<ConversationSummary> conversationSummaries) {
        AllConversationsStatistics allConversationsStatistics = new AllConversationsStatistics();
        setUsabilityAndEffectivenessScore(allConversationsStatistics, conversationSummaries);
        return allConversationsStatistics;
    }

    private void setUsabilityAndEffectivenessScore(AllConversationsStatistics conversationReport, List<ConversationSummary> conversationSummaries) {
        double sumEffectivenessScore = 0;
        double sumUsabilityScore = 0;
        double sumAmountOfQuestions = 0;
        double sumAmountOfMissUnderstoodQuestions = 0;
        int numberOfEffectivenessScore = 0;
        int numberOfUsabilityScore = 0;
        int numberOfConversations = conversationSummaries.size();
        for (ConversationSummary conversationSummary : conversationSummaries) {
            int effectivenessScore = conversationSummary.getEffectivenessScore();
            int usabilityScore = conversationSummary.getUsabilityScore();
            if (effectivenessScore != 0) {
                sumEffectivenessScore += effectivenessScore;
                numberOfEffectivenessScore++;
            }
            if (usabilityScore != 0) {
                sumUsabilityScore += usabilityScore;
                numberOfUsabilityScore++;
            }
            sumAmountOfQuestions += conversationSummary.getAmountOfQuestions();
            sumAmountOfMissUnderstoodQuestions += conversationSummary.getAmountOfMisunderstoodQuestions();
        }
        conversationReport.setAvgUsabilityScore(sumUsabilityScore / numberOfUsabilityScore);
        conversationReport.setAvgEffectivenessScore(sumEffectivenessScore / numberOfEffectivenessScore);
        conversationReport.setAvgAmountOfQuestions(sumAmountOfQuestions / numberOfConversations);
        conversationReport.setAvgAmountOfMisunderstoodQuestions(sumAmountOfMissUnderstoodQuestions / numberOfConversations);
    }

    public void createConversationSummary(String username, String conversationId, String userAddress) {
        Map<Integer, BigInteger> numberOfQuestionToAmountOfProducts = new HashMap<>();
        int amountOfQuestions = 1;
        numberOfQuestionToAmountOfProducts.put(amountOfQuestions, BigInteger.ZERO);
        ConversationSummary conversationSummary = ConversationSummary
                .builder()
                .username(username)
                .conversationId(conversationId)
                .numberOfQuestionToAmountOfProducts(numberOfQuestionToAmountOfProducts)
                .amountOfQuestions(amountOfQuestions)
                .userAddress(userAddress)
                .build();
        conversationSummaryRepository.save(conversationSummary);
    }
}
