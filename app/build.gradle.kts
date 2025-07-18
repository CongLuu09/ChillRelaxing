plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chillrelaxing"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chillrelaxing"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.room.common.jvm)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.google.android.gms:play-services-ads:24.3.0")



    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}