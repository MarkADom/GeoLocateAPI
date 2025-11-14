import org.gradle.kotlin.dsl.implementation

plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.synchlabs"
version = "0.0.1-SNAPSHOT"
description = "GeoLocate API - Spring Boot project for IP Geolocation"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // Core Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework:spring-webflux")

    // Utils & Dev
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Apache HttpClient 5
    implementation ("org.apache.httpcomponents.client5:httpclient5")


    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Swagger / OpenAPI
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:2.2.21")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Tests
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
