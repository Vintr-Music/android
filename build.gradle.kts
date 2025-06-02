// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("io.appmetrica.analytics:gradle:0.9.0")
    }
}

plugins {
    id("com.android.application") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("io.realm.kotlin") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}