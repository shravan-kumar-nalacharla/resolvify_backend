package com.cms.controller;

import com.cms.entity.Complaint;
import com.cms.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin("*")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public Complaint create(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    @GetMapping
    public List<Complaint> getAll() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint get(@PathVariable Long id) {
        return complaintService.getComplaint(id);
    }

    @PutMapping("/{id}")
    public Complaint update(@PathVariable Long id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return "Complaint deleted";
    }
}