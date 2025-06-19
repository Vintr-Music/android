import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("io.realm.kotlin")
    id("appmetrica-plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}

appmetrica {
    postApiKey = { "5a659821-03aa-4fca-8812-c61df4750f68" }
    setMappingBuildTypes(listOf("release"))
    setOffline(true)
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
    compileSdk = 35

    defaultConfig {
        applicationId = "pw.vintr.music"
        minSdk = 28
        targetSdk = 35
        versionCode = 6
        versionName = "0.5 Preview"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"http://188.225.9.157:4001/\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    val materialVersion = "1.8.2"
    val navigationVersion = "2.9.0"
    val accompanistVersion = "0.36.0"
    val ktorVersion = "3.1.3"
    val koinVersion = "4.0.4"
    val preferenceVersion = "1.2.1"
    val coilVersion = "3.2.0"
    val media3Version = "1.7.1"
    val realmVersion = "2.3.0"
    val realmCoroutinesVersion = "1.10.2"
    val paletteVersion = "1.0.0"
    val cameraxVersion = "1.4.2"
    val zxingVersion = "3.5.3"
    val reordableVersion = "2.4.3"
    val metricaVersion = "7.2.0"

    // Common Android
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.preference:preference-ktx:$preferenceVersion")
    implementation("androidx.palette:palette-ktx:$paletteVersion")
    implementation("androidx.compose.runtime:runtime-livedata:1.8.2")

    // Material
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:$materialVersion")
    implementation("androidx.compose.material:material-navigation:$materialVersion")

    // Compose external
    implementation("sh.calvin.reorderable:reorderable:$reordableVersion")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation | Accompanist
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    implementation("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")
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

    // Realm
    implementation("io.realm.kotlin:library-base:$realmVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$realmCoroutinesVersion")

    // Camera X
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // ZXing Scanner
    implementation("com.google.zxing:core:$zxingVersion")

    // Metrica
    implementation("io.appmetrica.analytics:analytics:$metricaVersion")
}