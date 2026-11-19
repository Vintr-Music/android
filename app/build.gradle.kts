import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.appmetrica)
}

appmetrica {
    // Offline locally — mapping is collected but not uploaded (same as previous setOffline(true)).
    postApiKey.set("5a659821-03aa-4fca-8812-c61df4750f68")
    offline.set(true)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }
    namespace = "pw.vintr.music"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pw.vintr.music"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 10
        versionName = "0.6.2 Preview"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"http://85.92.110.117:4001/\"")
        buildConfigField("String", "METRICA_API_KEY", "\"5a659821-03aa-4fca-8812-c61df4750f68\"")

        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))

    // Common Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.palette.ktx)

    // Material
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.navigation)

    // Compose external
    implementation(libs.reorderable)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation | Accompanist
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.accompanist.permissions)

    // Ktor | HTTP
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.resources)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Media 3
    implementation(libs.media3.session)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.datasource.okhttp)

    implementation(libs.kotlinx.coroutines.core)

    // Room cache (SearchHistory, PlayerSession, Equalizer)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Camera X
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.view)
    implementation(libs.camera.lifecycle)

    // ZXing Scanner
    implementation(libs.zxing.core)

    // Metrica
    implementation(libs.appmetrica.analytics)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)
}
