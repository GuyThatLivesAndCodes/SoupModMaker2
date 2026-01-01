@echo off
echo ================================================
echo   SoupModMaker2 - Minecraft Mod Generator
echo ================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java 17 or higher from:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo Starting SoupModMaker2...
echo.

REM Run the JAR with any command-line arguments passed to this script
java -jar soupmodmaker2-all-0.1.0-SNAPSHOT.jar %*

echo.
echo ================================================
echo   SoupModMaker2 finished!
echo ================================================
pause
