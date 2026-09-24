@echo off
where gradle >nul 2>nul
if %errorlevel%==0 (gradle %* & exit /b %errorlevel%)
echo Gradle is not installed. On Windows install Gradle 9.5.1 or run the project through your IDE.
exit /b 1
