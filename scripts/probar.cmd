@echo off
setlocal
cd /d "%~dp0.."

if not exist out-test mkdir out-test

dir /s /b src\*.java test\*.java > out-test\sources-test.txt
javac -Xlint:all -encoding UTF-8 -cp "lib/*" -d out-test @out-test\sources-test.txt
if errorlevel 1 exit /b 1

java -ea -cp "out-test;lib/*" com.empresa.pruebas.PruebasSinBaseDeDatos
