# Android XR Basic

A modern Android application showcasing Formula 1 tracks in both 2D and Spatial (XR) modes, built with Jetpack Compose and Android XR Compose.

### What is Android XR?

![image](https://github.com/user-attachments/assets/f7c6d67f-b8ba-4686-a488-3cfbb0f2b67a)

Android XR (Extended Reality) is Google's innovative platform that brings spatial computing capabilities to Android devices. It enables developers to create immersive experiences that blend digital content with the physical world. This technology encompasses both augmented reality (AR) and virtual reality (VR) features, allowing applications to create rich, interactive 3D environments that users can naturally interact with in space.

Android XR provides a powerful set of APIs and tools that make it possible to develop applications that can understand and interact with the physical space around the device, track motion, and render 3D content in a way that feels natural and responsive. Whether you're building applications for entertainment, education, or professional use, Android XR opens up new possibilities for spatial computing.

[Learn more about Android XR →](https://developer.android.com/xr)

## Features

### UI Components
- **AnimatedContent**: Smooth transitions between F1 track displays
- **Spatial Panel**: XR-enabled panel for immersive viewing
- **Orbiter**: Floating action buttons in XR mode
- **Custom Icon Buttons**: 
  - Home Space Mode Switch
  - Full Space Mode Switch
  - Navigation Buttons (Back/Next)

### Core Features
- **Dual Mode Support**:
  - 2D Mode: Traditional mobile interface
  - Spatial Mode: Immersive XR experience
- **F1 Track Showcase**:
  - High-quality track images
  - Track information display
  - Smooth navigation between tracks
- **Responsive Design**:
  - Edge-to-edge display
  - Adaptive layouts for different modes
- **Gesture Controls**:
  - Touch navigation in 2D mode
  - Spatial interaction in XR mode

## App Content


https://github.com/user-attachments/assets/1c504342-dbfc-417a-89cf-8659b2e63558


## Technologies Used

### Core Technologies
- Kotlin
- Jetpack Compose
- Android XR Compose

### UI/UX
- Material Design 3
- Compose Animation APIs
- XR Spatial UI Components

### Architecture & Patterns
- MVVM Architecture
- Compose State Management
- Repository Pattern

### Development Tools
- Android Studio
- Gradle Build System
- XR Development Kit

## Installation

Clone the repository
```kotlin
https://github.com/kaaneneskpc/AndroidXRF1Tracks.git
```
Open In Android Studio, Click on File > Open and select the cloned project directory.

Add the dependency to your `libs.versions.toml` file:

```kotlin
[versions]
agp = "8.9.0-alpha07"
kotlin = "2.0.0"
coreKtx = "1.15.0"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
lifecycleRuntimeKtx = "2.8.7"
lifecycleRuntimeCompose = "2.8.7"
lifecycleViewmodelCompose = "2.8.7"
activityCompose = "1.9.3"
runtime = "1.8.0-alpha06"
composeBom = "2024.09.00"
ui = "1.8.0-alpha06"
compose = "1.0.0-alpha01"
runtimeVersion = "1.0.0-alpha01"
scenecore = "1.0.0-alpha01"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-runtime = { group = "androidx.compose.runtime", name = "runtime", version.ref = "runtime" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui", version.ref = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose = { group = "androidx.xr.compose", name = "compose", version.ref = "compose" }
runtime = { group = "androidx.xr.runtime", name = "runtime", version.ref = "runtimeVersion" }
androidx-scenecore = { group = "androidx.xr.scenecore", name = "scenecore", version.ref = "scenecore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```
</br>

Add the dependency to your `build.gradle.kts` file:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.androidxrbasic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidxrbasic"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose)
    implementation(libs.runtime)
    implementation(libs.androidx.scenecore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

```
</br>

## Acknowledgments

- Formula 1 for track inspiration
- Android XR team for the spatial computing capabilities
- Jetpack Compose team for the modern UI toolkit
- Material Design team for design guidelines

## Contact

**Kaan Enes Kapıcı**
- LinkedIn: [Kaan Enes Kapıcı](https://www.linkedin.com/in/kaaneneskpc/)
- GitHub: [@kaaneneskpc](https://github.com/kaaneneskpc)
- Email: kaaneneskpc1@gmail.com

Feel free to reach out for any questions, suggestions, or collaboration opportunities! 
