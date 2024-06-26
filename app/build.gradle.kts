plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id ("kotlin-kapt")
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
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
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
    //maps google
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
//    implementation ("com.google.android.libraries.places:places:2.4.0")
    //room database
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    //kotlin corotine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    //testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("com.google.truth:truth:1.0.1")
    androidTestImplementation ("com.google.truth:truth:1.0.1")

    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")

    testImplementation ("org.robolectric:robolectric:4.8")


}