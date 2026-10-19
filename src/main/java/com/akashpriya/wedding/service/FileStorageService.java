package com.akashpriya.wedding.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Validates and stores the uploaded file, returning the generated unique file name.
     * Returns null if the file is empty (upload is optional).
     */
    String store(MultipartFile file);

    /** Resolves the content-type category (image/video/audio) of a stored file. */
    String resolveFileType(MultipartFile file);
}

