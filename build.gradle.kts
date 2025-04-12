plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("io.freefair.lombok") version "8.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java:5.1.49")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate6")
	implementation("javax.servlet:javax.servlet-api:4.0.1")
	implementation("org.apache.poi:poi-ooxml:5.2.3")
	implementation("commons-io:commons-io:2.11.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
