package com.cms.controller;

import com.cms.dto.ChatRequest;
import com.cms.dto.ChatResponse;
import com.cms.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String reply = chatService.askAI(request.getMessage(), request.getUserId());

        return new ChatResponse(reply);
    }
}