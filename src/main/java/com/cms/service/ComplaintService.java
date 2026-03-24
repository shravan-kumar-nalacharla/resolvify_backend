package com.cms.service;

import com.cms.entity.Complaint;
import java.util.List;

public interface ComplaintService {

    Complaint createComplaint(Complaint complaint);

    List<Complaint> getAllComplaints();

    List<Complaint> getComplaintsByUserId(Long userId);

    Complaint getComplaint(Long id);

    void deleteComplaint(Long id);

    Complaint updateComplaint(Long id, Complaint complaint);
}
