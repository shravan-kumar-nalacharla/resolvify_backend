package com.cms.service.impl;

import com.cms.entity.Complaint;
import com.cms.repository.ComplaintRepository;
import com.cms.service.ComplaintService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    	private final ComplaintRepository complaintRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Override
    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
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
        if (complaintDetail.getTitle() != null) existingComplaint.setTitle(complaintDetail.getTitle());
        if (complaintDetail.getDescription() != null) existingComplaint.setDescription(complaintDetail.getDescription());
        if (complaintDetail.getCategory() != null) existingComplaint.setCategory(complaintDetail.getCategory());
        if (complaintDetail.getPriority() != null) existingComplaint.setPriority(complaintDetail.getPriority());
        if (complaintDetail.getStatus() != null) existingComplaint.setStatus(complaintDetail.getStatus());
        return complaintRepository.save(existingComplaint);
    }
}
