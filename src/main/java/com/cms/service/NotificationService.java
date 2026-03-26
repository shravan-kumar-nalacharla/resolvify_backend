package com.cms.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    public void notifyStatusChange(String username, Long complaintId, String newStatus) {
        System.out.println("NOTIFY -> user@" + username + ".com: Your complaint #" + complaintId + " status changed to " + newStatus);
    }
    
    public void notifyNewComplaint(String username, Long complaintId) {
        System.out.println("NOTIFY -> admin: New complaint #" + complaintId + " submitted by " + username);
    }
}
