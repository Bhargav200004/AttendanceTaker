import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

//Reading value from local.property
private val property =  Properties()
property.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.example.attendancetaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.attendancetaker"
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
            buildConfigField("String", "SUPABASE_ANON_KEY" , "\"${property.getProperty("SUPABASE_ANON_KEY")}\"")
            buildConfigField("String", "SECRET" , "\"${property.getProperty("SECRET")}\"")
            buildConfigField("String" , "SUPABASE_URL", "\"${property.getProperty("SUPABASE_URL")}\"")
        }

        debug {
            buildConfigField("String", "SUPABASE_ANON_KEY" , "\"${property.getProperty("SUPABASE_ANON_KEY")}\"")
            buildConfigField("String", "SECRET" , "\"${property.getProperty("SECRET")}\"")
            buildConfigField("String" , "SUPABASE_URL", "\"${property.getProperty("SUPABASE_URL")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)


    // SupaBase Dependency
    implementation(platform(libs.supa.base.bom))
    implementation(libs.supa.base.postgrest)


    //ktor-client
    implementation(libs.androidx.ktor.client)


    // Hilt Dependency
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    //Serialization
    implementation(libs.kotlinx.serialization.json)


    //extra icon
    implementation(libs.androidx.material.icons.extended)


    //DataStore
    implementation(libs.androidx.datastore.preferences)


    //Timber library for logging the error and files
    implementation(libs.timber)


    // Pretty logger
    implementation(libs.logger)


    // Google Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.8")
}