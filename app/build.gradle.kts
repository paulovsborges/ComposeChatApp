import com.pvsb.build_src.AndroidX
import com.pvsb.build_src.Compose
import com.pvsb.build_src.Coroutines
import com.pvsb.build_src.Hilt
import com.pvsb.build_src.KotlinX
import com.pvsb.build_src.Ktor

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("kapt")
}

android {
    namespace = "com.pvsb.composechatapp"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.pvsb.composechatapp"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(AndroidX.androidXCore)
    implementation(AndroidX.lifecycle)
    testImplementation(AndroidX.junit)
    androidTestImplementation(AndroidX.junitExt)
    androidTestImplementation(AndroidX.espresso)

    implementation(Compose.composeActivity)
    implementation(Compose.composeUi)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    androidTestImplementation(Compose.composeJunit)
    debugImplementation(Compose.composeDebug)
    debugImplementation(Compose.composeManifest)

    implementation(Ktor.core)
    implementation(Ktor.client)
    implementation(Ktor.serialization)
    implementation(Ktor.webSockets)
    implementation(Ktor.logging)
    implementation(Ktor.logback)

    implementation(KotlinX.serialization)

    implementation(Hilt.android)
    implementation(Hilt.viewModel)
    implementation(Hilt.compose)
    kapt(Hilt.kaptCompiler)
    kapt(Hilt.kaptAndroidCompile)

    implementation(Coroutines.core)
    implementation(Coroutines.lifecycleScope)
    testImplementation(Coroutines.test)
}