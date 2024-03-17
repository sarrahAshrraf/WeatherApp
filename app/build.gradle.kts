plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    viewBinding{ enable= true}
    dataBinding{enable = true}
    namespace = "com.example.weatherapppoject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherapppoject"
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //lottifie
    implementation ("com.airbnb.android:lottie:5.2.0")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //viewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    //picasso
    implementation ("com.squareup.picasso:picasso:2.71828")
    //swip to refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //nav bar
    implementation ("nl.joery.animatedbottombar:library:1.1.0")
    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.0")
    //location service
    implementation ("com.google.android.gms:play-services-location:21.1.0")



}