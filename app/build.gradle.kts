plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android") // ✅ הפעלת Hilt
    kotlin("kapt") // ✅ הפעלת KAPT
    id("com.google.gms.google-services") // ✅ הפעלת Firebase
}


android {
    namespace = "com.example.soccergamesfinder" // ✅ ודא שתואם ל-AndroidManifest.xml
    compileSdk = 34 // 🔹 עדיף להשתמש ב-34, כי 35 עדיין לא יציב רשמית

    defaultConfig {
        applicationId = "com.example.soccergamesfinder"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34 // 🔹 עדיף להתאים ל-compileSdk
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
        sourceCompatibility = JavaVersion.VERSION_17 // 🔹 שדרג ל-Java 17 אם אפשר
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" // 🔹 עדיף 17 ליציבות וביצועים
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // ✅ חובה כדי למנוע בעיות בקומפילציה של Jetpack Compose
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }

}


dependencies {
    // AndroidX Core
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.navigation)
    implementation (libs.google.accompanist.navigation.animation)


    // Hilt Dependency Injection
    implementation(libs.hilt)
    implementation(libs.play.services.location)
    kapt(libs.hiltCompiler)
    implementation(libs.hiltNavigationCompose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Coroutines
    implementation(libs.coroutines)
    implementation(libs.coroutines.android)

    implementation(libs.kotlinStdLib)
    implementation(libs.googleSignIn) // ✅ שימוש ב-Google Sign-In מהקטלוג


}

