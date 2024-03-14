@echo off

if %0 == '17' (
SET GRADLE_HOME=D:\MyProgramFile\gradle-8.6-bin\gradle-8.6
SET JAVA_HOME=D:\MyProgramFile\jdk-21_windows-x64_bin\jdk-21.0.2
) else (
SET GRADLE_HOME=D:\MyProgramFile\gradle-7.5.1-bin\gradle-7.5.1
SET JAVA_HOME=D:\Program Files\Java\jdk1.8.0_351
)


SET PATH=%GRADLE_HOME%\bin;%JAVA_HOME%\bin;%PATH%

start cmd /K
