// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        // Pin KGP above AGP 9's default so built-in Kotlin uses the catalog version.
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.ksp)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.appmetrica) apply false
}
