plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    kotlin("kapt")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.sahil.bmi_calculator"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sahil.bmi_calculator"
        minSdk = 24
        targetSdk = 36
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
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")

    implementation("androidx.navigation:navigation-compose:2.7.2")

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    // Hilt + Jetpack Compose Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.compose.material:material-icons-core:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    implementation("com.google.android.gms:play-services-auth:20.7.0")


    //Graph

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.patrykandpatrick.vico:compose:1.14.0")
    implementation("com.patrykandpatrick.vico:core:1.14.0")
    implementation("com.patrykandpatrick.vico:views:1.14.0")





}