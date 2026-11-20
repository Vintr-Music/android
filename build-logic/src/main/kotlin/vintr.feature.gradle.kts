import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("vintr.compose.library")
}

extensions.configure<KotlinMultiplatformExtension>("kotlin") {
    sourceSets.commonMain.dependencies {
        implementation(project(":core:ui"))
        implementation(project(":core:navigation"))
    }
}
