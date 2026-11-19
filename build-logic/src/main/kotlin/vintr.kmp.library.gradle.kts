import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

extensions.configure<KotlinMultiplatformExtension>("kotlin") {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.path
                .removePrefix(":")
                .replace(':', '_')
                .replace('-', '_')
            isStatic = true
        }
    }

    extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        }
    }

    sourceSets.commonMain.dependencies {
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.collections.immutable)
    }
    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
    }
    sourceSets.androidMain.dependencies {
        implementation(libs.androidx.core.ktx)
    }
}
