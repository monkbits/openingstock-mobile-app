plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mymonkmindset.openingstock"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.mymonkmindset.openingstock"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.room.runtime)
    // OkHttp core client
    implementation(libs.okhttp)
    // General Image Labeling (detects 400+ common items)
    implementation(libs.image.labeling)
    // OCR for Devanagari (Hindi, Sanskrit, Marathi)
    implementation(libs.text.recognition.devanagari)
    // Or Object Detection & Tracking
    implementation(libs.object1.detection)
    implementation(libs.genai.speech.recognition)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.room.compiler)
}