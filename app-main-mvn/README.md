# mvn示例工程 

## 环境说明

```bash
$ mvn --version
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: D:\MyProgramFile\apache-maven-3.9.6-bin\apache-maven-3.9.6
Java version: 21.0.2, vendor: Oracle Corporation, runtime: D:\MyProgramFile\jdk-21_windows-x64_bin\jdk-21.0.2
Default locale: zh_CN, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## 常用命令

```bash
## 编译
$ mvn compile
## 执行
$ mvn exec:java -Dexec.mainClass="com.zeimao77.App"
$ mvn exec:java -Dlog.level=INFO -Dexec.mainClass="com.zeimao77.App" -Dexec.args="arg0 arg1 arg2"

## 打包
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

## 常用依赖

```xml
<dependencys>
    <!-- mysql连接 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>5.1.0</version>
    </dependency>

    <!-- redis连接 -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>5.1.0</version>
    </dependency>

    <!-- 邮件发送 -->
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>jakarta.mail</artifactId>
        <version>2.0.1</version>
    </dependency>

    <!-- excel读写 -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>5.2.5</version>
    </dependency>
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.5</version>
    </dependency>

    <!-- postgresql连接 -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.1</version>
    </dependency>

    <!-- bean工具 -->
    <dependency>
        <groupId>com.guicedee.services</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.2.2.1</version>
    </dependency>

</dependencys>


```
