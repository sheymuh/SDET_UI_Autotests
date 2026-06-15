@echo off
chcp 65001 >nul

echo =================================================
echo    Запуск упавших тестов с предыдущего прогона
echo =================================================
echo.

set PROJECT_ABSOLUTE_PATH=C:\Users\Андрей\OneDrive\Рабочий стол\учеба\simbirsoft\SDET_внешняя_стажировка\SDET_UI_Autotests\
set TESTNG_FAILED_PATH=target\surefire-reports\testng-failed.xml
set TESTNG_FAILED_ABSOLUTE_PATH="%PROJECT_ABSOLUTE_PATH%%TESTNG_FAILED_PATH%"

if not exist %TESTNG_FAILED_ABSOLUTE_PATH% (
    echo [ERROR] Файл %TESTNG_FAILED_ABSOLUTE_PATH% не найден!
    pause
    exit /b 1
)

cd %PROJECT_ABSOLUTE_PATH%
mvn test -DsuiteXmlFile=%TESTNG_FAILED_PATH%

pause