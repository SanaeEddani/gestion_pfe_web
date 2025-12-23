plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.frontend"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.frontend"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
        }

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
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Note: buildToolsVersion n'est plus nécessaire à partir d'AGP 7.0.0, mais si vous voulez le spécifier, utilisez une version existante.
    // buildToolsVersion = "34.0.0"
}

dependencies {

    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 🔴 FORCER FRAGMENT (clé du problème)
    implementation(libs.fragment)

    // 🔴 FORCER LIFECYCLE
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // 🔴 NAVIGATION (versions cohérentes)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Réseau
    implementation(libs.volley)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
