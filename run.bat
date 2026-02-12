@echo off
set "SCRIPT_DIR=%~dp0"
set "PROJECT_ROOT=%SCRIPT_DIR%"
cd /d "%PROJECT_ROOT%"

if not exist "target\classes" mkdir "target\classes"
xcopy /E /I /Y "src\main\resources" "target\classes" > nul

echo Compiling...
javac --release 22 -d target\classes -sourcepath src\main\java -cp "C:\Users\user\Documents\NetBeansProjects\OOP Assignment\dist\lib\jcalendar-1.4.jar" src\main\java\oodj_project\app\App.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running...
java -cp "target\classes;C:\Users\user\Documents\NetBeansProjects\OOP Assignment\dist\lib\jcalendar-1.4.jar" oodj_project.app.App
pause
