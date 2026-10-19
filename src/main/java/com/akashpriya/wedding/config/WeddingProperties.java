package com.akashpriya.wedding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;

/**
 * Strongly typed binding for "wedding.*" properties defined in application.yaml.
 */
@ConfigurationProperties(prefix = "wedding")
public class WeddingProperties {

    /** The wedding date/time used by the countdown timer. */
    private LocalDateTime date;

    /** Directory (relative or absolute) where guest uploaded files are stored. */
    private String uploadDir;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}

