@echo off
if %1 equ 8 (
SET JAVA_HOME=D:\Program Files\Java\jdk1.8.0_351
SET M2_HOME=D:\MyProgramFile\apache-maven-3.6.3-bin\apache-maven-3.6.3
SET GRADLE_HOME=D:\MyProgramFile\gradle-7.5.1-bin\gradle-7.5.1
) else if %1 equ 21 (
SET JAVA_HOME=D:\MyProgramFile\jdk-21_windows-x64_bin\jdk-21.0.2
SET M2_HOME=D:\MyProgramFile\apache-maven-3.9.6-bin\apache-maven-3.9.6
SET GRADLE_HOME=D:\MyProgramFile\gradle-8.6-bin\gradle-8.6
) else (
SET JAVA_HOME=D:\MyProgramFile\jdk-17_windows-x64_bin\jdk-17.0.5
SET M2_HOME=D:\MyProgramFile\apache-maven-3.9.6-bin\apache-maven-3.9.6
SET GRADLE_HOME=D:\MyProgramFile\gradle-8.4-bin\gradle-8.4
)

SET PATH=%GRADLE_HOME%\bin;%JAVA_HOME%\bin;%PATH%

gradle -V
echo "gradle publishMavenJavaPublicationToAliyunRepository"