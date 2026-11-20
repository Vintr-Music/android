plugins {
    id("vintr.compose.library")
}

kotlin {
    android {
        namespace = "pw.vintr.music.core.presentation"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(project(":core:navigation"))
            implementation(project(":core:domain"))
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.koin.core)
        }
    }
}
