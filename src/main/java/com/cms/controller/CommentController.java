package com.cms.controller;

import com.cms.entity.Comment;
import com.cms.repository.CommentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints/{id}/comments")
@CrossOrigin("*")
public class CommentController {
    
    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @PostMapping
    public Comment addComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setComplaintId(id);
        return commentRepository.save(comment);
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepository.findByComplaintIdOrderByCreatedAtDesc(id);
    }
}
