@echo off
chcp 65001 >nul
echo Finding and stopping process on port 4444...

for /f "tokens=5" %%a in ('netstat -aon ^| find ":4444" ^| find "LISTENING"') do (
    echo Found process with PID: %%a
    taskkill /F /PID %%a
)

echo Port 4444 released.
exit 0
