// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.13.2" apply false
    // Keep Kotlin on 2.0.21 while Realm Kotlin 3.0.0 remains in the project:
    // Realm's compiler plugin breaks on Kotlin 2.1+.
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("io.realm.kotlin") version "3.0.0" apply false
    id("io.appmetrica.analytics") version "2.1.0" apply false
}
