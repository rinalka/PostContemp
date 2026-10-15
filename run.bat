@echo off
rem Builds PostContemp from src into the build folder and runs it.
rem Uses the Java that comes with Processing, and ignores Windows display
rem scaling so the main window shows at full resolution.
setlocal
set P=%~dp0
set JDK=C:\Program Files\Processing\app\resources\jdk\bin
set CP=%P%lib\base\core\*;%P%application.windows\lib\controlP5.jar;%P%application.windows\lib\sDrop.jar

if not exist "%P%build" mkdir "%P%build"
rem javac does not expand *.java on Windows, so give it a list of the files
dir /b /s "%P%src\postContemp\*.java" > "%P%build\sources.txt"
"%JDK%\javac" -nowarn -encoding UTF-8 -cp "%CP%" -d "%P%build" @"%P%build\sources.txt"
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
xcopy /e /i /y /q "%P%src\data" "%P%build\data" >nul

cd /d "%P%"
"%JDK%\java" -Dsun.java2d.uiScale=1 -cp "%P%build;%CP%" postContemp.PostContemp
