/*
 *    Copyright 2020- Network Revolution Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"
    ndkVersion = "22.1.7171670"

    defaultConfig {
        applicationId = "com.dokoden.dotlocalfinder"
        minSdk = 14
        targetSdk = 30
        versionCode = 16
        versionName = "1.9.20200619"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        javaCompileOptions {
            annotationProcessorOptions {
                mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    buildFeatures {
        dataBinding = true
        // viewBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
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
    // Local File
    implementation(fileTree(mapOf("dir" to "libs", "include" to arrayOf("*.jar"))))
    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    // Core
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
//    implementation("com.google.android.gms:play-services-instantapps:17.0.0")
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    // Material Design and Preferences
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.preference:preference-ktx:1.1.1")
//    implementation("androidx.security:security-crypto:1.0.0")
    // Layout
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // Futures
    implementation("androidx.concurrent:concurrent-futures:1.1.0")
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.3.1")
    implementation("androidx.lifecycle:lifecycle-service:2.3.1")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    // Room
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    testImplementation("androidx.room:room-testing:2.3.0")
    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.0.0")
    testImplementation("androidx.paging:paging-common-ktx:3.0.0")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.5.0")
    androidTestImplementation("androidx.work:work-testing:2.5.0")
    // kotlinx.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
    // 3rd Party(mavenCentral)
    implementation("xyz.gianlu.mdnsjava:mdnsjava:2.2.1")
//    implementation("dnsjava:dnsjava:3.2.2")
}
