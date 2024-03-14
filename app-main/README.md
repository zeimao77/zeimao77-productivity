# gradle示例

## 环境说明

```bash
$ gradle -V
------------------------------------------------------------
Gradle 8.6
------------------------------------------------------------

Build time:   2024-02-02 16:47:16 UTC
Revision:     d55c486870a0dc6f6278f53d21381396d0741c6e

Kotlin:       1.9.20
Groovy:       3.0.17
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          21.0.2 (Oracle Corporation 21.0.2+13-LTS-58)
OS:           Windows 11 10.0 amd64
```

## 常用命令

```bash
## 编译
gralde compileJava
## 执行
gradle run
## 测试一个方法
gradle test --tests com.zeimao77.AppTest.test
## 分析依赖
gradle dependencies
## 打包
gradle distZip
gradle distTar
gradle installDist
```