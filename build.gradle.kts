// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        // Pin KGP above AGP 9's default so built-in Kotlin uses 2.4.21.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.12")
    }
}

plugins {
    id("com.android.application") version "9.5.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.21" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.4.21" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
    id("io.appmetrica.analytics") version "2.1.0" apply false
}
