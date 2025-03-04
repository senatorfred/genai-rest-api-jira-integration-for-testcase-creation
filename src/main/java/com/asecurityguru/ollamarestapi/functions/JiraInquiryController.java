package com.asecurityguru.ollamarestapi.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
//import java.io.FileWriter;
//import java.io.IOException;

@RestController
public class JiraInquiryController {
    private static final Logger log = LoggerFactory.getLogger(JiraInquiryController.class);
    private final ChatClient chatClient;
    private final Function<JiraDataService.Request, JiraDataService.Response> jiraFunction;

    public JiraInquiryController(ChatClient.Builder chatClientBuilder,
                                 Function<JiraDataService.Request, JiraDataService.Response> jiraFunction) {
        this.chatClient = chatClientBuilder
                         .defaultFunctions("jiraFunction")
                         .build();
        this.jiraFunction = jiraFunction;
    }

    @GetMapping("/api/v1/jira-story")
    public String getJiraStoryDetails(@RequestParam String storyKey) {
        try {
            // Fetch Jira story details
            JiraDataService.Response jiraResponse = jiraFunction.apply(new JiraDataService.Request(storyKey));
    
            // Generate test cases using AI
            String prompt = "Based on the following acceptance criteria, assume you are a very experienced QA Engineer, generate exhaustive and detailed test cases with Test Case ID, Steps, Test Data, Test Case Description, and Expected Result:\n\n" 
                            + jiraResponse.acceptanceCriteria();
    
            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
    
            // Process and generate CSV file
            List<String[]> testCaseData = new ArrayList<>();
            testCaseData.add(new String[]{"Test Case ID", "Steps", "Test Data", "Description", "Expected Result"}); // Header
    
            String[] testCases = aiResponse.split("\n");
            for (String testCase : testCases) {
                testCaseData.add(testCase.split(";"));
            }
    
            CSVFIleGenerator.generateCsvFile( "functional_test_cases.csv", testCaseData);

            log.info("AI response: {}", aiResponse);
            return aiResponse;  // Ensure return at the end
        } catch (Exception e) {
            log.error("Error processing request for storyKey: {}", storyKey, e);
            return "Unable to process your request at this time.";  // Add return in catch block
        }
    }
    

    @GetMapping("/api/v2/jira-story")
    public String getJiraStoryDetailsForSecurity(@RequestParam String storyKey) {

        try {
            // Fetch Jira story details
            JiraDataService.Response jiraResponse = jiraFunction.apply(new JiraDataService.Request(storyKey));

            // Generate test cases using the AI model based on acceptance criteria
            String prompt = "Based on the following acceptance criteria, assume you are a very experienced Penetration Tester, give me detailed steps for OWASP web app security check and steps that i can hand over to a functional QA with Test Case ID, Steps, Test Data, Test Case Description, and Expected Result:\n\n" 
                            + jiraResponse.acceptanceCriteria();

            String aiResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

               // Process and generate CSV file
               List<String[]> testCaseData = new ArrayList<>();
               testCaseData.add(new String[]{"Test Case ID", "Steps", "Test Data", "Description", "Expected Result"}); // Header
       
               String[] testCases = aiResponse.split("\n");
               for (String testCase : testCases) {
                   testCaseData.add(testCase.split(";"));
               }
       
               CSVFIleGenerator.generateCsvFile( "functional_test_cases.csv", testCaseData);        
            log.info("AI response: {}", aiResponse);
            return aiResponse;
        } catch (Exception e) {
            log.error("Error processing request for storyKey: {}", storyKey, e);
            return "Unable to process your request at this time.";
        }
    }
}





