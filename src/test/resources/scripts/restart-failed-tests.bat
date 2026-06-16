@echo off
chcp 65001 >nul

echo =================================================
echo    Запуск упавших тестов с предыдущего прогона
echo =================================================
echo.

cd ../../../..

set TESTNG_FAILED_PATH=target\surefire-reports\testng-failed.xml

if not exist %TESTNG_FAILED_PATH% (
    echo [ERROR] Файл %TESTNG_FAILED_PATH% не найден!
    pause
    exit /b 1
)

mvn test -DsuiteXmlFile=%TESTNG_FAILED_PATH%

pause