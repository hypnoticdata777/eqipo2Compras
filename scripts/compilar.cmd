@echo off
setlocal
cd /d "%~dp0.."

if not exist out mkdir out

dir /s /b src\*.java > out\sources.txt
javac -Xlint:all -encoding UTF-8 -cp "lib/*" -d out @out\sources.txt
if errorlevel 1 exit /b 1

echo Compilacion completada sin errores.
