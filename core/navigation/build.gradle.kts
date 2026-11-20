plugins {
    id("vintr.compose.library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "pw.vintr.music.core.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}
