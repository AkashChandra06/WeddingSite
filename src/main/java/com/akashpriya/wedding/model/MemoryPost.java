package com.akashpriya.wedding.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A guest submission on the Memory Wall: a wish, optionally with an uploaded photo/video/voice note.
 * New posts default to approved = false and only become publicly visible after admin approval.
 */
@Entity
@Table(name = "memory_posts")
public class MemoryPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guestName;
    private String relationship;

    @Column(length = 2000)
    private String message;

    private String fileName;
    private String fileType;
    private String filePath;

    private boolean approved = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public MemoryPost() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

