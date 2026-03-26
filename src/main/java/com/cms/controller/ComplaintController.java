package com.cms.controller;

import com.cms.entity.Complaint;
import com.cms.entity.StatusHistory;
import com.cms.repository.StatusHistoryRepository;
import com.cms.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin("*")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final StatusHistoryRepository statusHistoryRepository;

    public ComplaintController(ComplaintService complaintService, StatusHistoryRepository statusHistoryRepository) {
        this.complaintService = complaintService;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    @PostMapping
    public Complaint create(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    @GetMapping
    public List<Complaint> getAll(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return complaintService.getComplaintsByUserId(userId);
        }
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint get(@PathVariable Long id) {
        return complaintService.getComplaint(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Complaint complaint) {
        Complaint existing = complaintService.getComplaint(id);
        if (!"OPEN".equalsIgnoreCase(existing.getStatus()) && (complaint.getStatus() == null || existing.getStatus().equalsIgnoreCase(complaint.getStatus()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only OPEN complaints can be edited by users.");
        }
        return ResponseEntity.ok(complaintService.updateComplaint(id, complaint));
    }

    @GetMapping("/search")
    public List<Complaint> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId
    ) {
        return complaintService.searchComplaints(status, category, priority, keyword, userId);
    }

    @GetMapping("/{id}/history")
    public List<StatusHistory> getHistory(@PathVariable Long id) {
        return statusHistoryRepository.findByComplaintIdOrderByChangedAtDesc(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return "Complaint deleted";
    }
}