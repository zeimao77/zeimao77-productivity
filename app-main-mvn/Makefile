export JAVA_HOME = /usr/local/jdk/jdk-21.0.2

APPARGV ?=

CLASSPATH := $(shell find ${PWD}/libs -maxdepth 1 -type f -name "*.jar" -printf "libs/%f:")
MANIFEST_FILE = META-INF/MANIFEST.MF

.DEFAULT_GOAL=jar

.PHONY: clean compile run

run:
	${JAVA_HOME}/bin/java --class-path "build/classes:${CLASSPATH}" com.zeimao77.App ${APPARGV}

compile:
	find src/main/java/com -name "*.java" | xargs ${JAVA_HOME}/bin/javac -d build/classes --class-path "${CLASSPATH}" --source 21 --target 21
	cp src/main/resources/* build/classes/

jar:$(MANIFEST_FILE)
	jar -cvfm libs/app.jar $(MANIFEST_FILE) -C build/classes .

$(MANIFEST_FILE): 
	echo "Manifest-Version: 1.0" > $(MANIFEST_FILE)
	echo "Main-Class: com.zeimao77.App" >> $(MANIFEST_FILE)
	echo -ne "Class-Path: " >> $(MANIFEST_FILE)
	find libs -maxdepth 1 -type f -name "*.jar" -printf " %f \n" >> $(MANIFEST_FILE)

deploy:
	wget -P libs https://maven.aliyun.com/nexus/content/groups/public/top/zeimao77/zeimao77-productivity/2.2.1/zeimao77-productivity-2.2.1.jar 
	wget -P libs https://maven.aliyun.com/nexus/content/groups/public/org/slf4j/slf4j-api/2.0.12/slf4j-api-2.0.12.jar

clean:
	rm $(MANIFEST_FILE) -f
	rm libs/app.jar -f
