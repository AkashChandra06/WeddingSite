package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.model.MemoryPost;
import com.akashpriya.wedding.repository.MemoryPostRepository;
import com.akashpriya.wedding.service.MemoryWallService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemoryWallServiceImpl implements MemoryWallService {

    private final MemoryPostRepository memoryPostRepository;

    public MemoryWallServiceImpl(MemoryPostRepository memoryPostRepository) {
        this.memoryPostRepository = memoryPostRepository;
    }

    @Override
    public MemoryPost submit(String guestName, String relationship, String message,
                              String fileName, String fileType, String filePath) {
        MemoryPost post = new MemoryPost();
        post.setGuestName(guestName);
        post.setRelationship(relationship);
        post.setMessage(message);
        post.setFileName(fileName);
        post.setFileType(fileType);
        post.setFilePath(filePath);
        post.setApproved(false);
        return memoryPostRepository.save(post);
    }

    @Override
    public List<MemoryPost> getApprovedPosts() {
        return memoryPostRepository.findByApprovedTrueOrderByCreatedAtDesc();
    }

    @Override
    public List<MemoryPost> getPendingPosts() {
        return memoryPostRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }

    @Override
    public void approve(Long id) {
        MemoryPost post = memoryPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Memory post not found: " + id));
        post.setApproved(true);
        memoryPostRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        memoryPostRepository.deleteById(id);
    }
}

