#!/bin/sh
set -e

# Railway mounts the persistent volume at /app/data at container start,
# which can overwrite any ownership/permissions baked into the image.
# Ensure the directories exist and are writable before the app starts.
mkdir -p /app/data/uploads

exec java -jar /app/app.jar

