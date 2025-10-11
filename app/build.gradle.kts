plugins {
    id("com.android.application")
    id("com.google.gms.google-services")  // ← THIS LINE IS CRITICAL
}


android {
    namespace = "com.mentalhealth.auraapp"
    compileSdk = 35  // Change from 34 to 35

    defaultConfig {
        applicationId = "com.mentalhealth.auraapp"
        minSdk = 26
        targetSdk = 35  // Change from 34 to 35
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

    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    // Core Android
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.activity:activity:1.9.3")

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // MediaPipe for Facial Analysis
    implementation("com.google.mediapipe:tasks-vision:0.10.14")

    // Charts - REMOVED FOR NOW, will add back later
    // implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Networking for Hugging Face API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.11.0")

    // Work Manager for background tasks
    implementation("androidx.work:work-runtime:2.9.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Existing dependencies...

    // ML Kit for Face Detection
    implementation ("com.google.mlkit:face-detection:16.1.5")

    // CameraX for camera functionality
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")

    implementation ("com.google.mlkit:face-detection:16.1.5")

    // CameraX
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")

    // Audio recording
    // (Already have Android SDK support)

}

