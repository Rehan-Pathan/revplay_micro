@echo off
echo Stopping all RevPlay services...

taskkill /F /IM java.exe

echo All services stopped.
pause