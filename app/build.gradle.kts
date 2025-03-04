plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.buzzware.temperaturecheck"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.buzzware.temperaturecheck"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        setProperty("archivesBaseName", "Temp Check-$versionName")
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

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.sdp.android)
    implementation (libs.glide)
    implementation (libs.imagepicker)
    implementation (libs.powerspinner)
    implementation (libs.roundedimageview)
    implementation (libs.duo.navigation.drawer)


    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")



    implementation("com.google.android.gms:play-services-auth:20.7.0")


    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")



}