package com.cms.controller;

import com.cms.entity.Complaint;
import com.cms.entity.User;
import com.cms.repository.UserRepository;
import com.cms.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public AdminController(ComplaintService complaintService, UserRepository userRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String status = body.get("status");
            Complaint complaintDetail = new Complaint();
            complaintDetail.setStatus(status);
            Complaint updated = complaintService.updateComplaint(id, complaintDetail);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Complaint not found");
        }
    }

    @DeleteMapping("/complaints/{id}")
    public ResponseEntity<?> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Complaint> allComplaints = complaintService.getAllComplaints();
        
        long openCount = allComplaints.stream().filter(c -> "OPEN".equalsIgnoreCase(c.getStatus())).count();
        long inProgressCount = allComplaints.stream().filter(c -> "IN_PROGRESS".equalsIgnoreCase(c.getStatus())).count();
        long resolvedCount = allComplaints.stream().filter(c -> "RESOLVED".equalsIgnoreCase(c.getStatus())).count();
        
        Map<String, Long> complaintsByCategory = allComplaints.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory() != null ? c.getCategory() : "Other", Collectors.counting()));
                
        Map<String, Long> complaintsByPriority = allComplaints.stream()
                .collect(Collectors.groupingBy(c -> c.getPriority() != null ? c.getPriority() : "Low", Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalComplaints", allComplaints.size());
        stats.put("openCount", openCount);
        stats.put("inProgressCount", inProgressCount);
        stats.put("resolvedCount", resolvedCount);
        stats.put("complaintsByCategory", complaintsByCategory);
        stats.put("complaintsByPriority", complaintsByPriority);
        
        return stats;
    }
}
