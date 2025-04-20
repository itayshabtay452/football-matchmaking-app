plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.gradle)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)

}

android {
    namespace = "com.example.soccergamesfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.soccergamesfinder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    // 🔹 AndroidX Core
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // 🔹 Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.navigation)

    // 🔹 Hilt Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // 🔹 Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    // 🔹 Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // 🔹 Google Services
    implementation(libs.google.signin)
    implementation(libs.play.services.location)

    // 🔹 Dagger
    implementation(libs.dagger)

    // 🔹 Metadata
    implementation(libs.kotlinx.metadata)

    // 🔹 KSP
    ksp(libs.ksp.symbol.processing)

    implementation(libs.compose.compiler)

    implementation(libs.coil.compose)

    implementation(libs.androidx.animation)

    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)

}
