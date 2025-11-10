@echo off
docker compose up -d --force-recreate --remove-orphans --build
::docker build -t my-first-project .
pause