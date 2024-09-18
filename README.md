# zeimao77-productivity

**它想要干什么？**

- 快速的构建一个可执行的JAR包，收集依赖包;
- 初始化最小的环境(<a href="#5ad276d9-622e-4383-a552-0a15e71d0239">日志</a>、<a href="#57e1d183-a4fd-4832-aa15-664ccd559961">配置</a>境等)，快速开启业务功能编程;
- 提供常用工具等的支持;

## 快速开始一个jar

### gradle&jdk17

1. 新建一个工程，以`app-amin`为例,gradle(7.4.2)文件配置如下;
1. 导入一个工程
> 如果您希望用maven管理您的项目，可以使用`app-main-mvn`项目导入;  
> 如果您希望用gradle管理您的项目，可以使用`app-main`项目导入;  
> 如果想了解详细信息请移步项目README;  

2. 如果您想依赖<a id="bd99e813-7894-4bc6-a502-f8932db31ebf">在docker容器中执行</a>

```bash
## 前台执行
docker run --rm -it -v /home/docker/app-main/libs:/home/user0 openjdk:17.0.2-jdk-oraclelinux8\
 -w /home/user0 java -Dlog.level=INFO -Dlog.file=/home/user0/app.log -jar app-main.jar
## 守护进程执行
docker run -d -v /home/docker/app-main/libs:/home/user0 -w /home/user0 openjdk:17.0.2-jdk-oraclelinux8\
 java -Dlog.level=INFO -Dlog.file=/home/user0/app.log -jar app-main.jar
```

### maven&jdk8

````bat
## mvn依赖
mvn install:install-file -Dfile=zeimao77-productivity-jdk8-2.1.9.jar -DgroupId=top.zeimao77 -DartifactId=zeimao77-productivity-jdk8 -Dversion=2.1.9 -Dpackaging=jar
mvn install:install-file -Dfile=zeimao77-productivity-jdk8-2.1.9-sources.jar -DgroupId=top.zeimao77 -DartifactId=zeimao77-productivity-jdk8 -Dversion=2.1.9 -Dclassifier=sources -Dpackaging=jar
## 设置JAVA_HOME
set JAVA_HOME=D:\Program Files\Java\jdk1.8.0_351
## 收集依赖
mvn dependency:copy-dependencies -DoutputDirectory=libs
## 打包
mvn jar:jar
copy target\app-main-jdk8-1.0.0.jar libs\
````

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>top.zeimao77</groupId>
    <artifactId>app-main-jdk8</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>top.zeimao77</groupId>
            <artifactId>zeimao77-productivity-jdk8</artifactId>
            <version>2.1.9</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.17.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
            <version>2.20.0</version>
        </dependency>
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
            <version>4.0.3</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.29</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <!-- <version>3.2.2</version> -->
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <!-- <classpathPrefix>lib/</classpathPrefix> -->
                            <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                            <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
                            <mainClass>top.zeimao77.Main</mainClass>
                        </manifest>
                        <manifestEntries>
                            <mode>development</mode>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

如果您想对它所提供的工具有更详细的了解，请继续;

## 环境及配置

`top.zeimao77.product.main.BaseMain`类提供了日志及配置环境，您只需要让主函数类继承它就可以使用它了;

### <a id="5ad276d9-622e-4383-a552-0a15e71d0239">日志</a>

`BaseMain`提供了log4j2的日志支持，您可以使用环境变量或者命令行参数来简单配置它;
- `log.level` : 日志级别,缺省为`DEBUG`;
- `log.file` : 指定日志文件路径以开启文件日志功能;
- `log.rolling` : TRUE:开启滚动日志，缺省为`FALSE`;
- `log.stdout.enable` : FALSE 关闭终端log日志输出 缺少为TRUE;
- `log.rolloverStrategyMax` : 最多保留几个历史日志,缺省值`10`;


当然它仅仅是一个默认的实现，其优先值为3，如果您觉得它很烂完全可以自己通过`log4j2.xml`等文件重新配置,配置文件有较高的优先级;

### <a id="57e1d183-a4fd-4832-aa15-664ccd559961">配置</a>

`BaseMain`提供了默认的环境配置，您只需要在classpath或者用户目录下放置一个`localcontext.properties`文件即可;  

如果在程序中显式读取配置，调用`LocalContext.get***()`方法就可以;  
如果您需要在执行时指定该配置，您也可以通过环境变量或者命令行参数来指定它;  
- `local.context.file` 支持您通过一个路径来指定一个配置文件;
- `local.context.active` 如果有多个环境需要区分，可以提前将配置放置到classpath下，然后通过参数指定它;例如:`-Dlocal.context.active=dev`将加载`localcontext-dev.properties`配置文件;


配置的主要作用还是通过`ComponentFactory`类来初始化配置常用组件，使用它可以简化代码;

## 代码示例

### 工具

- [ID生成工具](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/LongIdGeneratorTest.java)
- [单词风格转换](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/WordUtilTest.java)
- [字节数组编解码](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/ByteArrayCoDesUtilTest.java)
- [字符串的处理](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/StringUtilTest.java)
- [集合工具](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/util/CollectionUtilTest.java)

### 数据库

- [静态与动态SQL](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/sql/SimpleSqlClientTest.java)
- [结合事务](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/sql/SimpleSqlTemplateTest.java)
- [结合Bean](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/mysql/SimpleRepositoryTest.java)

### 安全

- [消息摘要](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/DigestUtilTest.java)
- [对秤加密(AES)](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/AesUtilTest.java)
- [非对秤加密(RSA)](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/RsaUtilTest.java)
- [签名](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/security/RsaSignUtilTest.java)

### 任务

- [多线程消费处理任务模板示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/jobs/JobExecTemplateTest.java)
- 如果在消费任务时需要控制处理的频率，我们也提供了令牌桶(`TokenBucket`);

### 本地缓存

`IConverter`提供了键值对转换功能，其有4个抽象的实现对应了4种不同的刷新机制;
- `AbstractNonReFreshConverter`: 没有刷新机制的转换器，仅在第一次使用调用刷新;
- `AbstractIntervalRefreshConverter`: 固定间隔(秒)刷新，刷新规则时如果过期将加上间隔时间设置为过期时间;
- `AbstractIntervalDayRefreshConverter`: 每天刷新，以临界点为限，临界点后的第一次使用时刷新;
- `AbstractCustomRefreshConverter`: 通过`setExpiryTime(expiryTime)`自定义过期时间，最为灵活;

- [代码示例](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/converter/AbstractIntervalRefreshConverterTest.java)

### HTTP

- [GET请求](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/http/HttpClientUtilTest.java)

### JSON

- [字符串与JSON](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/json/IjsonTest.java)

### 序列化

- [自实现序列化](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/fileio/serialize/SerializeUtilTest.java)

### 邮件

- [发送一封电子邮件](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/email/SimpleEmailSenderTest.java)

### EXCEL

- [导出excel](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/fileio/oexcel/ExcelXlsxDocumentBuilderTest.java)
- [解析excel](https://github.com/zeimao77/zeimao77-productivity/blob/master/zeimao77-productivity/src/test/java/top/zeimao77/product/fileio/iexcel/ExcelXlsxDocumentResolveTest.java)

