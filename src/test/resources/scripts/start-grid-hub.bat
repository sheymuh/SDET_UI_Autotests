@echo off
chcp 65001 >nul
echo ==========================================
echo    Запуск Selenium Grid 4 — HUB
echo ==========================================
echo.

set SERVER_JAR="C:\Program Files\Java\selenium-server-4.44.0.jar"

if not exist %SERVER_JAR% (
    echo [ERROR] Файл %SERVER_JAR% не найден!
    pause
    exit /b 1
)

echo [INFO] Запуск Hub
echo [INFO] Консоль: http://localhost:4444/ui

java -jar %SERVER_JAR% hub

pause