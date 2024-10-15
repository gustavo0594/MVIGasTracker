pluginManagement {
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
        maven {
            url = uri("https://gitlab.kgportal.com/api/v4/projects/1117/packages/maven")
            credentials {
                username = ("test-deploy-token")
                password = ("gldt-uPpxm6UrgX7jw3F_GJ-M")
            }
        }
    }
}

rootProject.name = "MVIGasTracker"
include(":app")
