plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    // Добавил
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")

    // Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.talki"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.talki"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    // Добавил
    kapt {
        correctErrorTypes =  true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Добавил сам Android viewmodel.compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    //Agora
    implementation("io.agora.rtc:voice-sdk:4.2.6")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    //Coroutine Lifecycle Scoopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.activity:activity-compose:1.8.2")

    //Glider
    implementation("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")

}