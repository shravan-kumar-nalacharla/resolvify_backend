package com.cms.service.impl;

import com.cms.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String askAI(String message) {

        try {

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
                    """.formatted(message);

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