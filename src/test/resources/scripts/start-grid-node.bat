@echo off
chcp 65001 >nul
echo ==========================================
echo    Запуск Selenium Grid 4 — NODE
echo ==========================================
echo.

set SERVER_JAR="C:\Program Files\Java\selenium-server-4.44.0.jar"

if not exist %SERVER_JAR% (
    echo [ERROR] Файл %SERVER_JAR% не найден!
    pause
    exit /b 1
)

echo [INFO] Запуск Node
echo [INFO] Консоль: http://localhost:4444/ui
echo [INFO] max-sessions=2

java -jar %SERVER_JAR% node --hub http://localhost:4444 --max-sessions 2 --session-timeout 300

pause