package com.akashpriya.wedding.service;

import com.akashpriya.wedding.model.MemoryPost;

import java.util.List;

public interface MemoryWallService {

    MemoryPost submit(String guestName, String relationship, String message,
                       String fileName, String fileType, String filePath);

    List<MemoryPost> getApprovedPosts();

    List<MemoryPost> getPendingPosts();

    void approve(Long id);

    void delete(Long id);
}

