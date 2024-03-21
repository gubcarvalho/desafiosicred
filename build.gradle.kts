plugins {
    java
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.freefair.lombok") version "8.1.0"
}

group = "com.nt"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

extra.apply {
    set("mapStructVersion", "1.5.5.Final")
}

dependencies {
    val mapStructVersion = rootProject.extra.get("mapStructVersion") as String

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")

    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.liquibase:liquibase-core")

    implementation("org.mapstruct:mapstruct:${mapStructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapStructVersion}");

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.3")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
