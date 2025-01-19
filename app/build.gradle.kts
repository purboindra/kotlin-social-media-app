plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.socialmedia"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.socialmedia"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildFeatures {
        buildConfig = true
    }
    
    packaging {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
    
    buildTypes {
        
        debug {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "PROJECT_ID",
                "\"${project.findProperty("PROJECT_ID")}\""
            )
            buildConfigField(
                "String",
                "PROJECT_URL",
                "\"${project.findProperty("PROJECT_URL")}\""
            )
            buildConfigField(
                "String",
                "PROJECT_API_KEY",
                "\"${project.findProperty("PROJECT_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "SERVER_CLIENT_ID",
                "\"${project.findProperty("SERVER_CLIENT_ID")}\""
            )
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)
    
    // NAVIGATION
    implementation(libs.androidx.navigation.compose)
    
    // SERIALIZATION
    implementation(libs.androidx.serialization.json)
    
    // SUPABASE
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation(libs.postgrest.kt)
    implementation(libs.storage.kt)
    
    // KTOR
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    
    // HILT
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.compiler)
    ksp(libs.hilt.compiler)
    
    // COIL
    implementation(libs.coil)
    implementation(libs.coil.network)
    implementation(libs.coil.svg)
    
    // Auth0Java
    implementation(libs.auth0)
    
    // JBCRYPT
    implementation(libs.jbcrypt)
    
    implementation(libs.credentials)
    implementation(libs.googleid)
    implementation(libs.play.service.auth)
}
