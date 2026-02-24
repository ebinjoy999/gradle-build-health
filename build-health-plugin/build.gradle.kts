/*
 * Build Health Gradle Plugin
 * Version 1.0.0
 *
 * Publishing to Gradle Plugin Portal requires credentials.
 * These should be provided via:
 *   - gradle.properties file (recommended for local development):
 *       gradle.publish.key=YOUR_KEY
 *       gradle.publish.secret=YOUR_SECRET
 *   - Environment variables (recommended for CI):
 *       GRADLE_PUBLISH_KEY=YOUR_KEY
 *       GRADLE_PUBLISH_SECRET=YOUR_SECRET
 *
 * To publish:
 *   ./gradlew :build-health-plugin:publishPlugins
 *
 * To validate without publishing:
 *   ./gradlew :build-health-plugin:publishPlugins --validate-only
 */

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.github.ebinjoy999"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    website.set("https://github.com/ebinjoy999/gradle-build-health")
    vcsUrl.set("https://github.com/ebinjoy999/gradle-build-health")

    plugins {
        create("buildHealth") {
            id = "io.github.ebinjoy999.gradle-build-health"
            implementationClass = "io.github.ebinjoy999.gradle_build_health.BuildHealthPlugin"
            displayName = "Build Health"
            description = "A Gradle plugin that prints a build-end executive summary with timing metrics, slowest tasks, and cache statistics. Supports JSON export for CI integration."
            tags.set(listOf("build-performance", "android", "ci", "developer-tools", "metrics"))
        }
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
}
