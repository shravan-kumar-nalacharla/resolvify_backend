package com.cms.service.impl;

import com.cms.entity.Complaint;
import com.cms.entity.StatusHistory;
import com.cms.repository.ComplaintRepository;
import com.cms.repository.StatusHistoryRepository;
import com.cms.repository.UserRepository;
import com.cms.service.ComplaintService;
import com.cms.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, StatusHistoryRepository statusHistoryRepository, NotificationService notificationService, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Override
    public Complaint createComplaint(Complaint complaint) {
        Complaint saved = complaintRepository.save(complaint);
        statusHistoryRepository.save(new StatusHistory(saved.getId(), null, "OPEN", "SYSTEM"));
        if (saved.getUserId() != null) {
            userRepository.findById(saved.getUserId()).ifPresent(user -> {
                notificationService.notifyNewComplaint(user.getUsername(), saved.getId());
            });
        }
        return saved;
    }

    @Override
    public List<Complaint> getComplaintsByUserId(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    public Complaint getComplaint(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    @Override
    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    @Override
    public Complaint updateComplaint(Long id, Complaint complaintDetail) {
        Complaint existingComplaint = getComplaint(id);
        String oldStatus = existingComplaint.getStatus();

        if (complaintDetail.getTitle() != null) existingComplaint.setTitle(complaintDetail.getTitle());
        if (complaintDetail.getDescription() != null) existingComplaint.setDescription(complaintDetail.getDescription());
        if (complaintDetail.getCategory() != null) existingComplaint.setCategory(complaintDetail.getCategory());
        if (complaintDetail.getPriority() != null) existingComplaint.setPriority(complaintDetail.getPriority());
        if (complaintDetail.getStatus() != null) {
            String newStatus = complaintDetail.getStatus();
            existingComplaint.setStatus(newStatus);
            if (!oldStatus.equals(newStatus)) {
                statusHistoryRepository.save(new StatusHistory(id, oldStatus, newStatus, "ADMIN"));
                if (existingComplaint.getUserId() != null) {
                    userRepository.findById(existingComplaint.getUserId()).ifPresent(user -> {
                        notificationService.notifyStatusChange(user.getUsername(), id, newStatus);
                    });
                }
            }
        }
        return complaintRepository.save(existingComplaint);
    }

    @Override
    public List<Complaint> searchComplaints(String status, String category, String priority, String keyword, Long userId) {
        return complaintRepository.findAll().stream().filter(c -> {
            boolean match = true;
            if (userId != null && !userId.equals(c.getUserId())) match = false;
            if (status != null && !status.isEmpty() && !status.equalsIgnoreCase(c.getStatus())) match = false;
            if (category != null && !category.isEmpty() && !category.equalsIgnoreCase(c.getCategory())) match = false;
            if (priority != null && !priority.isEmpty() && !priority.equalsIgnoreCase(c.getPriority())) match = false;
            if (keyword != null && !keyword.isEmpty()) {
                boolean titleMatch = c.getTitle() != null && c.getTitle().toLowerCase().contains(keyword.toLowerCase());
                boolean descMatch = c.getDescription() != null && c.getDescription().toLowerCase().contains(keyword.toLowerCase());
                if (!titleMatch && !descMatch) match = false;
            }
            return match;
        }).collect(Collectors.toList());
    }
}
