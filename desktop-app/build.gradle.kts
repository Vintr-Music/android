import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(project(":core:ui"))
        }
    }
}

compose.desktop {
    application {
        mainClass = "pw.vintr.music.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "VintrMusic"
            packageVersion = "0.6.2"
            description = "Vintr Music"
            copyright = "© Vintr"
            vendor = "Vintr"

            windows {
                menuGroup = "Vintr Music"
                upgradeUuid = "A1B2C3D4-E5F6-7890-ABCD-EF1234567890"
            }
            macOS {
                bundleID = "pw.vintr.music.desktop"
            }
            linux {
                packageName = "vintr-music"
            }
        }
    }
}
