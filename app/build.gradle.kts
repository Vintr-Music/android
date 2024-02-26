plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("io.realm.kotlin")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("D:\\Android KEYS\\VintrMusic\\VMKey.jks")
            storePassword = "Ruslan99"
            keyAlias = "vmusic"
            keyPassword = "Ruslan99"
        }
    }
    namespace = "pw.vintr.music"
    compileSdk = 34

    defaultConfig {
        applicationId = "pw.vintr.music"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "0.1 Preview"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"http://188.225.9.157:4001/\"")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val navigationVersion = "2.7.7"
    val accompanistVersion = "0.32.0"
    val ktorVersion = "2.3.7"
    val koinVersion = "3.5.0"
    val preferenceVersion = "1.2.1"
    val coilVersion = "2.4.0"
    val media3Version = "1.2.1"
    val realmVersion = "1.11.0"
    val realmCoroutinesVersion = "1.7.3"
    val paletteVersion = "1.0.0"
    val cameraxVersion = "1.3.1"
    val zxingVersion = "3.5.2"

    // Common Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.6.2")
    implementation("androidx.preference:preference-ktx:$preferenceVersion")
    implementation("androidx.palette:palette-ktx:$paletteVersion")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.2")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
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
    implementation("io.coil-kt:coil-compose:$coilVersion")

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
}