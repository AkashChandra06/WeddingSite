package com.akashpriya.wedding.service.impl;

import com.akashpriya.wedding.config.GoogleDriveProperties;
import com.akashpriya.wedding.constants.FileTypeConstants;
import com.akashpriya.wedding.service.FileStorageService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Stores Memory Wall photos in the site owner's Google Drive so uploads count against
 * the owner's own storage quota (not a limited service account). Images are compressed
 * before upload to conserve space. If Drive is disabled/misconfigured or the upload fails
 * for any reason (network issue, quota exceeded, etc.), the file is saved to local disk
 * instead so guest submissions are never lost.
 */
@Service
public class GoogleDriveStorageService implements FileStorageService {

    private static final int MAX_DIMENSION = 1600;
    private static final double JPEG_QUALITY = 0.75;

    private final GoogleDriveProperties googleDriveProperties;
    private final LocalFileStorageService localFileStorageService;

    public GoogleDriveStorageService(GoogleDriveProperties googleDriveProperties,
                                      LocalFileStorageService localFileStorageService) {
        this.googleDriveProperties = googleDriveProperties;
        this.localFileStorageService = localFileStorageService;
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        if (contentType == null || !FileTypeConstants.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType +
                    ". Only JPEG, PNG, or WEBP photos are allowed.");
        }

        if (file.getSize() > FileTypeConstants.MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("File exceeds the maximum allowed size of 10 MB.");
        }

        byte[] compressed = compress(file);

        if (googleDriveProperties.isEnabled() && StringUtils.hasText(googleDriveProperties.getRefreshToken())
                && StringUtils.hasText(googleDriveProperties.getFolderId())) {
            try {
                return uploadToDrive(compressed);
            } catch (Exception ex) {
                // Fall back to local disk so the submission still succeeds even if Drive is
                // unreachable, misconfigured, or its quota is full.
                return "/uploads/" + localFileStorageService.saveBytes(compressed, ".jpg");
            }
        }

        return "/uploads/" + localFileStorageService.saveBytes(compressed, ".jpg");
    }

    @Override
    public String resolveFileType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return FileTypeConstants.resolveFileType(file.getContentType());
    }

    /** Resizes and re-encodes the image as a compressed JPEG to minimize storage footprint. */
    private byte[] compress(MultipartFile file) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(MAX_DIMENSION, MAX_DIMENSION)
                    .outputQuality(JPEG_QUALITY)
                    .outputFormat("jpg")
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process the uploaded image", e);
        }
    }

    private String uploadToDrive(byte[] data) throws Exception {
        Drive drive = buildDriveClient();

        File metadata = new File();
        metadata.setName(java.util.UUID.randomUUID() + ".jpg");
        metadata.setParents(Collections.singletonList(googleDriveProperties.getFolderId()));

        ByteArrayContent content = new ByteArrayContent("image/jpeg", data);
        File uploaded = drive.files().create(metadata, content)
                .setFields("id")
                .execute();

        // Make the file viewable via direct link (anyone with the link can view).
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");
        drive.permissions().create(uploaded.getId(), permission).execute();

        return "https://drive.google.com/uc?export=view&id=" + uploaded.getId();
    }

    /**
     * Returns Drive storage usage as [usedBytes, totalBytes], or null if Drive
     * storage is disabled/misconfigured or the API call fails.
     */
    public long[] getStorageQuota() {
        if (!googleDriveProperties.isEnabled() || !StringUtils.hasText(googleDriveProperties.getRefreshToken())) {
            return null;
        }
        try {
            Drive drive = buildDriveClient();
            About about = drive.about().get().setFields("storageQuota").execute();
            long used = about.getStorageQuota().getUsage() != null ? about.getStorageQuota().getUsage() : 0L;
            Long limit = about.getStorageQuota().getLimit();
            long total = limit != null ? limit : 15L * 1024 * 1024 * 1024; // default 15GB if unlimited/not reported
            return new long[]{used, total};
        } catch (Exception ex) {
            return null;
        }
    }

    private Drive buildDriveClient() throws Exception {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(googleDriveProperties.getClientId())
                .setClientSecret(googleDriveProperties.getClientSecret())
                .setRefreshToken(googleDriveProperties.getRefreshToken())
                .build();

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(googleDriveProperties.getApplicationName())
                .build();
    }
}

