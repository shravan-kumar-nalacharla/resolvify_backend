package com.cms.service.impl;

import com.cms.service.ChatService;
import com.cms.service.ComplaintService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ComplaintService complaintService;

    public ChatServiceImpl(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @Override
    public String askAI(String message, Long userId) {

        try {
            String systemPrompt = "You are Resolvify AI, a helpful complaint management assistant. Help users understand complaint statuses, suggest resolutions, and guide them through the system. Be concise and professional.\\n";
            String context = "";
            if (userId != null) {
                List<Complaint> myComplaints = complaintService.getComplaintsByUserId(userId);
                if (!myComplaints.isEmpty()) {
                    context = "User's current complaints: ";
                    for (Complaint c : myComplaints) {
                        context += "[Title: '" + c.getTitle() + "', Status: " + c.getStatus() + "] ";
                    }
                    context += "\\n";
                }
            }
            
            String safeMessage = message.replace("\"", "\\\"").replace("\n", " ");
            String fullPrompt = systemPrompt + context + "User message: " + safeMessage;

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

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String botReply = root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
                return botReply;
            }
            return "AI assistant returned an empty response.";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI assistant is temporarily unavailable. Error: " + e.getMessage();
        }
    }
}