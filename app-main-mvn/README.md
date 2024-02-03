# mvn示例工程 

## 环境说明

```bash
$ mvn --version
Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
Maven home: D:\MyProgram\apache-maven-3.8.4-bin\apache-maven-3.8.4
Java version: 21, vendor: Oracle Corporation, runtime: D:\MyProgram\jdk-21_windows-x64_bin\jdk-21
Default locale: zh_CN, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## 常用命令

```bash
$ mvn clean package
$ mvn -Dmaven.test.skip=true clean package
$ mvn test
## 单元测试一个某个方法
$ mvn -Dtest=com.zeimao77.AppTest#test test
## 分析依赖
$ mvn dependency:list
$ mvn dependency:tree
$ mvn dependency:copy-dependencies
$ mvn dependency:analyze
## 源码包
$ mvn source:jar
```