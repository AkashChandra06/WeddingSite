package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.config.WeddingProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Low-level helper that writes raw bytes to the local "uploads" directory.
 * Used as a fallback whenever Google Drive storage is disabled or unavailable,
 * so guest submissions are never lost even if Drive has an outage or hits its quota.
 */
@Component
public class LocalFileStorageService {

    private final WeddingProperties weddingProperties;

    public LocalFileStorageService(WeddingProperties weddingProperties) {
        this.weddingProperties = weddingProperties;
    }

    /**
     * Saves the given bytes under a random unique file name (with the given extension,
     * e.g. ".jpg") inside the configured upload directory, and returns the file name.
     */
    public String saveBytes(byte[] data, String extension) {
        try {
            Path uploadPath = Path.of(weddingProperties.getUploadDir());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String uniqueFileName = UUID.randomUUID() + extension;
            Files.write(uploadPath.resolve(uniqueFileName), data);
            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store uploaded file locally", e);
        }
    }
}

