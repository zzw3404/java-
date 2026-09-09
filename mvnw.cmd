@echo off
setlocal
set "BASE_DIR=%~dp0"
if not defined JAVA_HOME if exist "C:\Program Files\Microsoft\jdk-17.0.11.9-hotspot\bin\java.exe" set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.11.9-hotspot"
if not defined JAVA_HOME if exist "C:\Program Files\Java\jdk-17\bin\java.exe" set "JAVA_HOME=C:\Program Files\Java\jdk-17"
if not defined JAVA_HOME (
  echo JAVA_HOME is not set and no supported JDK was found.
  exit /b 1
)
set "MAVEN_VERSION=3.9.9"
set "MAVEN_HOME=%BASE_DIR%.mvn\wrapper\apache-maven-%MAVEN_VERSION%"
if exist "%MAVEN_HOME%\bin\mvn.cmd" goto run

set "ZIP_FILE=%BASE_DIR%.mvn\wrapper\apache-maven-%MAVEN_VERSION%-bin.zip"
if not exist "%ZIP_FILE%" (
  echo Downloading Apache Maven %MAVEN_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%ZIP_FILE%'"
  if errorlevel 1 exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ZIP_FILE%' -DestinationPath '%BASE_DIR%.mvn\wrapper' -Force"
if errorlevel 1 exit /b 1

:run
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
