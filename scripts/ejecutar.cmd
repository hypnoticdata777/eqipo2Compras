@echo off
setlocal
cd /d "%~dp0.."

call scripts\compilar.cmd
if errorlevel 1 exit /b 1

java -cp "out;lib/*" com.empresa.compras.MainPruebaCompras
