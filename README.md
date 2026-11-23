# Akash & Priya Wedding Website

A full-stack Java Spring Boot wedding invitation website for Akash & Priya.

## Features

- Welcome page
- Wedding invitation
- Countdown timer
- Event schedule
- Google Maps integration
- Hotel information
- Family introduction
- Interactive family tree (deep, multi-generation)
- Photo gallery
- Memory Wall with wishes, photos, videos, and voice notes
- Admin approval panel

## Tech Stack

- Java 21
- Spring Boot 3
- Thymeleaf
- Bootstrap 5
- JavaScript
- Spring Data JPA
- H2 Database
- Spring Security

## Project Structure

```
src/
├── main/
│   ├── java/com/akashpriya/wedding/
│   │   ├── WeddingSiteApplication.java
│   │   ├── controller/   (HomeController, MemoryWallController, AdminController)
│   │   ├── model/        (Event, FamilyMember, Hotel, GalleryImage, MemoryPost)
│   │   ├── repository/   (MemoryPostRepository)
│   │   ├── service/       + service/impl/ (Event, Family, Hotel, Gallery, FileStorage, MemoryWall)
│   │   ├── config/       (SecurityConfig, WebConfig, WeddingProperties, AdminProperties)
│   │   └── constants/    (FileTypeConstants, AppConstants)
│   └── resources/
│       ├── application.yaml
│       ├── templates/    (index.html, admin.html, admin-login.html, fragments/)
│       └── static/       (css/, js/, images/, uploads/)
└── test/java/com/akashpriya/wedding/
```

## Run Locally

```bash
mvn spring-boot:run
```

Open the public site:

```
http://localhost:8080
```

Admin panel:

```
http://localhost:8080/admin
```

## Configuration

Update `src/main/resources/application.yaml`:

```yaml
wedding:
  date: 2027-02-14T19:00:00
  upload-dir: uploads

admin:
  username: admin
  password: change-this-password
```

Admin credentials can also be supplied via environment variables `ADMIN_USERNAME` and `ADMIN_PASSWORD`,
which override the defaults in `application.yaml`.

### File Upload Rules

- Allowed types: `image/jpeg`, `image/png`, `image/webp`, `video/mp4`, `audio/mpeg`, `audio/wav`, `audio/webm`
- Max size: 25 MB
- New Memory Wall posts default to `approved = false`; only admin-approved posts appear publicly.

### Google Drive Storage (Memory Wall photos)

Memory Wall photos are uploaded to a Google Drive folder (destination is the shared folder
`https://drive.google.com/drive/folders/12c17KLRu1fp5ZoF8VYNp5NfRzpaQvtl9`, pre-configured as
the default `GOOGLE_DRIVE_FOLDER_ID`). If Drive upload is disabled or fails for any reason, the
app automatically falls back to saving the file on local disk under `uploads/`, so submissions
are never lost.

**Important:** sharing a folder link (even "anyone with access") only grants *browser* access to
view/edit its contents — it does **not** let the Drive API upload files on your behalf. To
actually enable Drive uploads you still need OAuth2 credentials for the owner's Google account:

1. In [Google Cloud Console](https://console.cloud.google.com/), create/select a project and
   enable the **Google Drive API**.
2. Create an **OAuth client ID** (type: Desktop app) under *APIs & Services → Credentials*. Note
   the `Client ID` and `Client Secret`.
3. Use the OAuth 2.0 Playground (https://developers.google.com/oauthplayground) — gear icon → check
   "Use your own OAuth credentials" and paste your client ID/secret — authorize the
   `https://www.googleapis.com/auth/drive.file` scope while logged in as the account that owns the
   shared folder, then exchange the authorization code for tokens to get a **refresh token**.
4. Set these environment variables on the host (or in `application.yaml` for local testing):

```bash
GOOGLE_DRIVE_ENABLED=true
GOOGLE_DRIVE_CLIENT_ID=your-client-id
GOOGLE_DRIVE_CLIENT_SECRET=your-client-secret
GOOGLE_DRIVE_REFRESH_TOKEN=your-refresh-token
GOOGLE_DRIVE_FOLDER_ID=12c17KLRu1fp5ZoF8VYNp5NfRzpaQvtl9
```

Once `GOOGLE_DRIVE_ENABLED=true` and a valid refresh token are set, uploaded Memory Wall photos
will be stored directly in the configured Drive folder (and count against the owner's own Drive
storage quota) instead of local disk.

## Deployment

This is a Java Spring Boot application. It **cannot** be hosted directly on GitHub Pages.
Use Render, Railway, Oracle Cloud Free Tier, or any VPS for hosting.
GitHub should be used only as the source code repository.

Steps:
1. Push the repository to GitHub.
2. Connect the repo to Render/Railway (or build a jar with `mvn clean package` and deploy to a VPS/Oracle Cloud instance).
3. Set environment variables `ADMIN_USERNAME`, `ADMIN_PASSWORD`, and any datasource overrides on the hosting platform.
4. Ensure the `uploads` directory (or an attached persistent volume) is writable on the host.

