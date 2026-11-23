package com.akashpriya.wedding.repository;

import com.akashpriya.wedding.model.MemoryPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryPostRepository extends JpaRepository<MemoryPost, Long> {

    List<MemoryPost> findByApprovedTrueOrderByCreatedAtDesc();

    List<MemoryPost> findByApprovedFalseOrderByCreatedAtDesc();
}

