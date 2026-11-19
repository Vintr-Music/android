import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("vintr.kmp.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

// Use Gradle path (:core:ui → core.ui) so nested modules don't collide on project.name ("ui").
compose.resources {
    val modulePackage = project.path
        .removePrefix(":")
        .replace(':', '.')
        .replace('-', '.')
    packageOfResClass = "pw.vintr.music.$modulePackage.resources"
}

extensions.configure<KotlinMultiplatformExtension>("kotlin") {
    extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
        androidResources {
            enable = true
        }
    }

    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.ui)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(compose.components.resources)
        implementation(compose.components.uiToolingPreview)
        implementation(libs.jetbrains.lifecycle.runtime.compose)
        implementation(libs.jetbrains.lifecycle.viewmodel.compose)
    }
}

dependencies {
    add("androidRuntimeClasspath", compose.uiTooling)
}
