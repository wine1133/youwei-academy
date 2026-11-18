@echo off
cd /d "%~dp0"
set JAVA_HOME=C:\Users\shanh\.jdks\graalvm-jdk-24.0.2
if not exist "springboot\target\springboot-0.0.1-SNAPSHOT.jar" (
  echo 正在构建后端，请稍候...
  pushd springboot
  call mvn -q -DskipTests package
  popd
)
start "YouweiServer" /min "%JAVA_HOME%\bin\java.exe" -jar "springboot\target\springboot-0.0.1-SNAPSHOT.jar"
timeout /t 6 /nobreak >nul
start "YouweiTunnel" /min "tools\cloudflared.exe" tunnel --url http://localhost:9090 --no-autoupdate
echo 本地地址: http://localhost:9090
echo 公网地址请查看 YouweiTunnel 窗口
pause
