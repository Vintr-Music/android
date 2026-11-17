// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.4.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.21" apply false
    // KSP is versioned independently of Kotlin since 2.3.0.
    id("com.google.devtools.ksp") version "2.3.12" apply false
    id("io.appmetrica.analytics") version "2.1.0" apply false
}
