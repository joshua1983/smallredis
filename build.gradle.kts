import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    id("jacoco")
}

buildscript {

    val kotlinVersion = "1.3.61"
    val springBootVersion = "2.2.4.RELEASE"

    repositories {
        mavenCentral()
        maven(url = "https://repo.spring.io/snapshot")
        maven(url = "https://repo.spring.io/milestone")
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.2.1")
        classpath(kotlin("gradle-plugin"))
        classpath(kotlin("allopen", kotlinVersion))
        classpath(kotlin("noarg", kotlinVersion))
    }
}

subprojects {
    group = "com.rappi.pay.fraudsters"
}

allprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-noarg")
    apply(plugin = "kotlin-allopen")
    apply(plugin = "jacoco")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
        maven(url = "https://repo.spring.io/snapshot")
        maven(url = "https://repo.spring.io/milestone")
    }

    java.sourceCompatibility = JavaVersion.VERSION_1_8

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.google.code.gson:gson")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
    }


    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }

    tasks.getByName<BootJar>("bootJar") {
        mainClassName = "com.rappi.pay.fraudsters.app.ApplicationKt"
        classifier = "boot"
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
        }
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed", "standardError")
        }

        addTestListener(object : TestListener {
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
            override fun beforeSuite(testDescriptor: TestDescriptor) {
                println("=> Running ${testDescriptor.name} ...")
            }

            override fun afterSuite(testDescriptor: TestDescriptor, testResult: TestResult) {
                val summary = mapOf(
                    "total" to testResult.testCount,
                    "passed" to testResult.successfulTestCount,
                    "failed" to testResult.failedTestCount,
                    "skipped" to testResult.skippedTestCount
                )

            }
        })
    }
}
