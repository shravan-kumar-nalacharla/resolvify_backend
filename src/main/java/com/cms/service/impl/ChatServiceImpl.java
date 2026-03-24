package com.cms.service.impl;

import com.cms.entity.Complaint;
import com.cms.service.ChatService;
import com.cms.service.ComplaintService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ComplaintService complaintService;

    public ChatServiceImpl(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @Override
    public String askAI(String message, Long userId) {

        try {
            String context = "";
            if (userId != null) {
                List<Complaint> myComplaints = complaintService.getComplaintsByUserId(userId);
                context = "System Data: The user asking this question has the following complaints in our database: ";
                for (Complaint c : myComplaints) {
                    context += "[ID: " + c.getId() + ", Title: '" + c.getTitle() + "', Status: " + c.getStatus() + "] ";
                }
                context += " Use this system data ONLY to answer questions about the user's complaints.\\n";
            }
            
            String safeMessage = message.replace("\"", "\\\"");
            String fullPrompt = context + "User message: " + safeMessage;

            String url = apiUrl + "?key=" + apiKey;

            String requestBody = """
                    {
                      "contents": [
                        {
                          "parts": [
                            {"text": "%s"}
                          ]
                        }
                      ]
                    }
                    """.formatted(fullPrompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            return "AI service currently unavailable.";
        }
    }
}