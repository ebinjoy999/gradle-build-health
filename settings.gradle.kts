pluginManagement {
    includeBuild("build-health-plugin")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ShopLite"

// App module
include(":app")

// Core modules
include(":core-model")
include(":core-network")
include(":core-data")

// Feature modules
include(":feature-home")
include(":feature-search")
