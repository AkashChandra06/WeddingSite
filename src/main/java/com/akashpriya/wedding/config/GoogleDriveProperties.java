package com.akashpriya.wedding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly typed binding for "google.drive.*" properties used to authenticate as the
 * site owner's Google account (via OAuth2 refresh token) and upload Memory Wall photos
 * into a specific Drive folder so storage counts against the owner's own quota.
 */
@ConfigurationProperties(prefix = "google.drive")
public class GoogleDriveProperties {

    /** Whether Google Drive storage is enabled. If false, falls back to local disk storage. */
    private boolean enabled = false;

    /** OAuth2 client ID from Google Cloud Console credentials. */
    private String clientId;

    /** OAuth2 client secret from Google Cloud Console credentials. */
    private String clientSecret;

    /** OAuth2 refresh token obtained once via the consent flow (see README for setup steps). */
    private String refreshToken;

    /** The ID of the destination Drive folder (from its share link) where photos are uploaded. */
    private String folderId;

    /** Application name sent to the Drive API (any descriptive string). */
    private String applicationName = "Akash & Priya Wedding Site";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}

