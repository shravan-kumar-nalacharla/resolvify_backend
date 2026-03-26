package com.cms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long complaintId;
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;
    private boolean isAdminNote;

    public Comment() {}

    public Comment(Long complaintId, Long userId, String text, boolean isAdminNote) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.text = text;
        this.isAdminNote = isAdminNote;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isAdminNote() { return isAdminNote; }
    public void setAdminNote(boolean adminNote) { isAdminNote = adminNote; }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
