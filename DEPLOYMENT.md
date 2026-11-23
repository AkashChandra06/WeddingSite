# Deploying to Railway

This app is containerized via the root `Dockerfile` and configured for Railway
using `railway.json`. Follow these steps to get it live.

## 1. Create the Railway project
1. Sign in to [railway.app](https://railway.app) with GitHub.
2. **New Project** → **Deploy from GitHub repo** → select `AkashChandra06/WeddingSite`.
3. Railway detects `railway.json` and builds using the Dockerfile automatically.

## 2. Add a persistent Volume
The app stores its H2 database file and uploaded photos under a single base
directory so **one** Railway volume covers both.

1. Click the **WeddingSite** service card → open its **Settings** tab.
2. Scroll to the **Volumes** section → click **+ New Volume**.
3. Set **Mount Path** to: `/app/data`
4. Click **Add**. Railway redeploys the service automatically with the volume
   attached.

With this single mount, the H2 DB file (default `./data/weddingdb`, which
resolves to `/app/data/weddingdb` since the container's working directory is
`/app`) is already covered. To make uploaded photos persist too, add one more
environment variable in step 3 below: `UPLOAD_DIR=/app/data/uploads` — this
nests uploads inside the same mounted volume instead of the default
`uploads` folder (which lives outside the volume and would be wiped on
redeploy).

## 3. Set environment variables
In the service **Variables** tab, add:

| Variable | Value |
|---|---|
| `ADMIN_USERNAME` | your chosen admin login |
| `ADMIN_PASSWORD` | a strong password (never commit this) |
| `GOOGLE_DRIVE_ENABLED` | `false` (Drive integration not needed yet) |
| `UPLOAD_DIR` | `/app/data/uploads` (keeps uploads inside the mounted volume) |

Railway automatically injects `PORT`; the app already reads it via
`server.port=${PORT:8080}` in `application.yaml`, so no manual port config is
needed.

## 4. Deploy & verify
1. Trigger a deploy (push to `main` auto-deploys, or use **Deploy** button).
2. Open the generated `*.up.railway.app` URL and confirm the homepage loads.
3. Log in to `/admin-login`, upload a test photo, then redeploy and confirm
   the photo and data persisted (validates the volume mount).

## Notes
- Google Drive integration exists in the codebase but is disabled by default
  (`GOOGLE_DRIVE_ENABLED=false`). Enable later by setting the Drive env vars
  documented in `application.yaml`.
- GitHub Pages is not used for this project — it hosts a full Spring Boot
  backend (Thymeleaf + JPA + H2 + file uploads), which Pages cannot run.

