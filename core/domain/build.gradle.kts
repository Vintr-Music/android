plugins {
    id("vintr.kmp.library")
}

kotlin {
    android {
        namespace = "pw.vintr.music.core.domain"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
