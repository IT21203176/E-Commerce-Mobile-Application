plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.ecommerce_mobile_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommerce_mobile_app"
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
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.lottie)

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")

    // ViewModel
    implementation("androidx.activity:activity-ktx:1.9.2")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Add Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")

    // Compose View Model
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Compose Live Data
    implementation("androidx.compose.runtime:runtime-livedata:1.7.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

    // OkHttp Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.squareup.picasso:picasso:2.8")

}