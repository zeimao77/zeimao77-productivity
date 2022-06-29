# zeimao77-productivity

利用它可以快速的帮我们建立一个运维小工具;它帮我们实现了默认的环境、日志、工具类、常用的任务框架;  
环境要求:JDK版本大于等于17;  

## 入门  

### BaseMain

  - 它实现了一个默认的日志配置，支持使用环境变量或者命令参数利用以下参数来简单配置它;
    - `log.level` : 日志级别;
    - `log.file` : 开启文件日志，并指定日志文件路径;
    - `log.rolling` : TRUE:开启滚动日志;
    - `log.rolloverStrategyMax` : 最多保留几个历史日志,缺省值10
  - 该配置是一个优先值为3的实现，如果您对它不满意完全可以自己利用`log4j2.xml`等文件重新配置,配置文件有较高的优先级;
  - 它实现了一个初步的环境配置，默认读取工作变量或者类路径的`localcontext.properties`文件;可以使用环境变量或者命令参数(`local.context.file`)来配置它;

### 可执行JAR包

1. 新建一个工程，以`app-amin`为例，gradle(版本:7.4.2)配置如下;

```groovy
plugins {
    id 'java'
    id 'application'
}

group 'com.zeimao77'
version '1.0.0'

repositories {
    maven {
        url 'https://maven.aliyun.com/nexus/content/groups/public'
    }
    mavenCentral()
}

tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
    implementation group: 'top.zeimao77',name:'zeimao77-productivity',version: '2.0.9'
}

application {
    mainClass = 'com.zeimao77.Main'
    applicationDefaultJvmArgs = ['-Dlog.level=DEBUG','-Dlog.file=app.log']
}

test {
    useJUnitPlatform()
}

jar {
    archiveFileName = 'app-main.jar'
    String cp = configurations.runtimeClasspath.collect {it.getName()}.join(" ")
    manifest {
        attributes("Main-Class": "com.zeimao77.Main")
        attributes("Class-Path": cp)
    }
}

task collectlib(type:Copy) {
    dependsOn('jar')
    configurations.runtimeClasspath.collect {
        var t = buildDir.getPath() + "\\" + libsDirName + "\\"
        from it.getAbsoluteFile()
        into t
    }
}
```

2. 编写可执行类

```java
package com.zeimao77;

import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.LongIdGenerator;

public class Main extends BaseMain {

    public static void main(String[] args) {
        BaseMain.showBanner();
        logger.trace(LongIdGenerator.INSTANCE.generate());
        logger.debug(LongIdGenerator.INSTANCE.generate());
        logger.info(LongIdGenerator.INSTANCE.generate());
        logger.warn(LongIdGenerator.INSTANCE.generate());
        logger.error(LongIdGenerator.INSTANCE.generate());
        logger.fatal(LongIdGenerator.INSTANCE.generate());
    }
}
```

3. 执行`gradle collectlib`打包，打包的文件放在`build/libs`目录下;

4. 分析依赖

```bash
/jdk-17.0.3/bin/jdeps -cp '/home/libs/*' --module-path '/home/libs/*' \
--multi-release 9 --print-module-deps --ignore-missing-deps libs/app-main.jar
```

5. 打包JRE

```bash
/jdk-17.0.3/bin/jlink --add-modules java.base,java.compiler,java.desktop,java.management\
,java.naming,java.net.http,java.rmi,java.scripting,java.security.sasl,java.sql,jdk.unsupported \
--output app-main-with-jre
```

6. 执行

```bash
nohup /jdk-17.0.3/bin/java -Dlog.file=app-main.log \
-jar /home/libs/app-main.jar >> /dev/null 2>&1 &
```

## 代码示例及说明

### 数据库部分

- [MYSQL数据库操作示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/mysql/SimpleMysqlTest.java)
- [基于实体Model的增删改查](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/mysql/SimpleRepositoryTest.java)
- 利用`top.zeimao77.product.mysql.SQL` 可以轻松实现动态SQL
- 通过`top.zeimao77.product.mysql.StatementParamResolver#getExecSql`可以输出可以手工执行的SQL，可以用于生成SQL脚本使用;
- [REDIS单节点操作示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/redis/SimpleJedisTest.java)
- [REDIS集群发布者](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/redis/JedisClusterBuilderTest2.java)
- [REDIS集群订阅者](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/redis/JedisClusterBuilderTest.java)

### 转换器部分

- [转换器示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/converter/AbstractIntervalRefreshConverterTest.java)
- 转换器有一个接口`top.zeimao77.product.converter.IConverter`,它实现了一个简单的本地缓存,4个实现子类:
- `top.zeimao77.product.converter.AbstractNonReFreshConverter` 没有任何过期刷新机制
- `top.zeimao77.product.converter.AbstractIntervalRefreshConverter` 通过时间间隔刷新
- `top.zeimao77.product.converter.AbstractIntervalDayRefreshConverter` 每天刷新一次
- `top.zeimao77.product.converter.AbstractCustomRefreshConverter` 自定义过期时间

### 邮件部分

- [发送邮件示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/email/SimpleEmailSenderTest.java)

### 安全部分

- [对秤加密(AES)](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/AesUtilTest.java)
- [非对秤加密(RSA)](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/RsaUtilTest.java)
- [签名](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/RsaSignUtilTest.java)
- [消息摘要](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/DigestUtilTest.java)

### HTTP部分

- [GET请求](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/http/HttpClientUtilTest.java)

### JSON

- [字符串与JSON](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/json/IjsonTest.java)

### 多线程任务模型

- [令牌桶](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/jobs/TokenBucketTest.java)
- [多线程消费处理任务模板示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/jobs/JobExecTemplateTest.java)

### 列表与树

- [随机投票器](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/tree/RandomVoterTest.java)
- [表决器](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/tree/ThresholdVoterComponentTest.java)
- [列表与树](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/tree/TreeUtilTest.java)

### 工具类

- [ID生成器(安全)](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/LongIdGeneratorTest.java)
- [字节数组编解码](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/ByteArrayCoDesUtilTest.java)
- [驼峰与下划线风格互转](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/WordUtilTest.java)
