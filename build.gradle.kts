// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("io.github.ebinjoy999.gradle-build-health")
}

group = "io.github.ebinjoy999"
version = "1.0.0"

buildHealth {
    exportJson = true
}