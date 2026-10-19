package com.akashpriya.wedding.constants;

import java.util.Set;

/**
 * Allowed file types and size limits for Memory Wall uploads.
 */
public final class FileTypeConstants {

    private FileTypeConstants() {
    }

    public static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    public static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB (pre-compression)

    public static String resolveFileType(String contentType) {
        if (contentType == null) {
            return "unknown";
        }
        if (contentType.startsWith("image/")) {
            return "image";
        }
        return "unknown";
    }
}

