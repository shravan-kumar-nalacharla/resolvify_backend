package com.cms.service;

public interface ChatService {

    String askAI(String message, Long userId, boolean isAdmin);
}
