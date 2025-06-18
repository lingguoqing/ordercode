@echo off
setlocal

:: 检查JRE
if not exist "%~dp0jre\bin\java.exe" (
    echo 未找到jre目录，请确保jre文件夹和本bat在同一目录下！
    pause
    exit /b
)

:: 启动程序
"%~dp0jre\bin\java.exe" -jar "%~dp0import-excel-sqlserver-1.0-SNAPSHOT-jar-with-dependencies.jar"
pause 