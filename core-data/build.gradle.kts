plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.shoplite.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // This module depends on both core modules - central dependency hub
    api(project(":core-model"))
    implementation(project(":core-network"))
    
    implementation(libs.kotlinx.coroutines.core)
}
