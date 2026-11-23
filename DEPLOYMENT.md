# Deploying to Railway

This app is containerized via the root `Dockerfile` and configured for Railway
using `railway.json`. Follow these steps to get it live.

## 1. Create the Railway project
1. Sign in to [railway.app](https://railway.app) with GitHub.
2. **New Project** → **Deploy from GitHub repo** → select `AkashChandra06/WeddingSite`.
3. Railway detects `railway.json` and builds using the Dockerfile automatically.

## 2. Add a persistent Volume
The app stores its H2 database file under `./data` and uploaded photos under
`./uploads`. Both must survive restarts/redeploys.

1. In the Railway project, open the service → **Volumes** tab → **New Volume**.
2. Mount path: `/app/data` (create a second volume for `/app/uploads`, or mount
   a single volume at `/app` parent if Railway's plan allows multiple mount
   points — otherwise create two separate volumes).
3. Redeploy after attaching volumes so the app writes into the mounted paths.

## 3. Set environment variables
In the service **Variables** tab, add:

| Variable | Value |
|---|---|
| `ADMIN_USERNAME` | your chosen admin login |
| `ADMIN_PASSWORD` | a strong password (never commit this) |
| `GOOGLE_DRIVE_ENABLED` | `false` (Drive integration not needed yet) |

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

