plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.ebinjoy999"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        create("buildHealth") {
            id = "com.ebinjoy999.build-health"
            implementationClass = "com.ebinjoy999.buildhealth.BuildHealthPlugin"
            displayName = "Build Health Plugin"
            description = "A Gradle plugin that provides build health metrics and summaries"
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
