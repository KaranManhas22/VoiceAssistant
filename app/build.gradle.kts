import org.gradle.internal.impldep.org.eclipse.jgit.transport.NetRCCredentialsProvider.install

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.chaquo.python")
}

android {
    namespace = "com.karan.voiceassistant"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.karan.voiceassistant"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //Integeration of Python

        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
        manifestPlaceholders["chaquopy.python.version"] = "3.12"
        manifestPlaceholders["chaquopy.python.buildPython"] = "/Library/Frameworks/Python.framework/Versions/3.12/bin/python3"

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
    sourceSets {
        getByName("main").resources.srcDirs("src/main/python3")
    }


    buildFeatures.viewBinding=true
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.airbnb.android:lottie:6.4.1")
    implementation("com.chaquo.python:gradle:12.0.1")

}
