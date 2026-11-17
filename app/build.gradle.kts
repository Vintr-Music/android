import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.devtools.ksp")
    id("io.appmetrica.analytics")
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
    compileSdk = 37

    defaultConfig {
        applicationId = "pw.vintr.music"
        minSdk = 28
        targetSdk = 37
        versionCode = 9
        versionName = "0.6.1 Preview"

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
    val materialVersion = "1.12.1"
    val navigationVersion = "2.9.8"
    val accompanistVersion = "0.37.3"
    val accompanistNavigationVersion = "0.36.0"
    val ktorVersion = "3.6.1"
    val koinVersion = "4.2.2"
    val preferenceVersion = "1.2.1"
    val coilVersion = "3.6.3"
    // Intentionally pinned — media session migration is a separate task.
    val media3Version = "1.5.1"
    val roomVersion = "2.8.5"
    val coroutinesVersion = "1.11.0"
    val paletteVersion = "1.1.0"
    val cameraxVersion = "1.6.2"
    val zxingVersion = "3.5.4"
    val reordableVersion = "3.1.0"
    val metricaVersion = "8.6.0"
    val immutableCollectionsVersion = "0.5.2"
    val composeBomVersion = "2026.09.00"

    // Common Android
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.11.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.preference:preference-ktx:$preferenceVersion")
    implementation("androidx.palette:palette-ktx:$paletteVersion")

    // Material
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:$materialVersion")
    implementation("androidx.compose.material:material-navigation:$materialVersion")

    // Compose external
    implementation("sh.calvin.reorderable:reorderable:$reordableVersion")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation | Accompanist
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    implementation("com.google.accompanist:accompanist-navigation-material:$accompanistNavigationVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    // Ktor | HTTP
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
    implementation("io.ktor:ktor-client-resources:$ktorVersion")

    // Koin
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")

    // Coil
    implementation("io.coil-kt.coil3:coil-compose:$coilVersion")
    implementation("io.coil-kt.coil3:coil-network-okhttp:$coilVersion")

    // Media 3
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-datasource-okhttp:$media3Version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Room cache (SearchHistory, PlayerSession, Equalizer)
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Camera X
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // ZXing Scanner
    implementation("com.google.zxing:core:$zxingVersion")

    // Metrica
    implementation("io.appmetrica.analytics:analytics:$metricaVersion")

    // Immutable collections
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:${immutableCollectionsVersion}")
}
