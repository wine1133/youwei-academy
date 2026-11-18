@echo off
taskkill /FI "WINDOWTITLE eq YouweiServer*" /F >nul 2>nul
taskkill /FI "WINDOWTITLE eq YouweiTunnel*" /F >nul 2>nul
echo 有为书院与公网隧道已停止。
pause
