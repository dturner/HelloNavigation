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
        /*maven {
            // You can find the maven URL for other artifacts (e.g. KMP, METALAVA) on their
            // build pages.
            url = uri("https://androidx.dev/snapshots/builds/12208038/artifacts/repository")
        }*/
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        /*maven {
            url = uri("https://androidx.dev/snapshots/builds/12208038/artifacts/repository")
        }*/
    }
}

rootProject.name = "HelloNavigation"
include(":app")
 