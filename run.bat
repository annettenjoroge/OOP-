@echo off
REM ============================================================
REM  RadioButtonDemo - Java Assignment 2  (run script)
REM  Compiles and launches the application using the Adoptium
REM  JDK 17 that is cached by Gradle on this machine.
REM ============================================================

set JAVA_HOME=C:\Users\dell\.gradle\jdks\eclipse_adoptium-17-amd64-windows.2
set JAVAC=%JAVA_HOME%\bin\javac.exe
set JAVA=%JAVA_HOME%\bin\java.exe

echo Compiling RadioButtonDemo.java ...
"%JAVAC%" -encoding UTF-8 RadioButtonDemo.java
if %ERRORLEVEL% NEQ 0 (
    echo Compilation FAILED.
    pause
    exit /b 1
)

echo Launching RadioButtonDemo ...
"%JAVA%" RadioButtonDemo
