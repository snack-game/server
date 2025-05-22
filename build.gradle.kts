plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlin.kapt") version "1.9.24"
    id("io.freefair.lombok") version "8.10"
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.4")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.jsonwebtoken:jjwt:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.session:spring-session-jdbc")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.github.classgraph:classgraph:4.8.147")

    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.2.2")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.8.0")
    implementation("org.springdoc:springdoc-openapi-javadoc:1.8.0")
    kapt("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
    implementation("com.github.therapi:therapi-runtime-javadoc:0.15.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured")
    implementation(kotlin("stdlib-jdk8"))
    runtimeOnly(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}
