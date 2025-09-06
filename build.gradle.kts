plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	war
}

group = "com.recovis"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("mysql:mysql-connector-java:8.0.33")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	compileOnly("javax.servlet:javax.servlet-api:4.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.war {
	enabled = true
}

tasks.bootJar {
	enabled = false
}