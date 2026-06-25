@echo off
chcp 65001 >nul
echo ==========================================
echo    Запуск Selenium Grid 4 — STANDALONE
echo ==========================================
echo.

set SELENIUM_JAR="C:\Program Files\Java\selenium-server-4.44.0.jar"

if not exist %SELENIUM_JAR% (
    echo [ERROR] Файл %SELENIUM_JAR% не найден!
    pause
    exit /b 1
)

echo [INFO] Запуск Standalone Grid
echo [INFO] Консоль: http://localhost:4444/ui
echo [INFO] max-sessions=5
echo.

java -jar %SELENIUM_JAR% standalone --max-sessions 5 --session-timeout 300

pause