plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vsple.loginwithgoogle"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vsple.loginwithgoogle"
        minSdk = 29
        targetSdk = 33
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.credentials:credentials:1.3.0-alpha01")

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.google.api-client:google-api-client:1.32.1")
    implementation(" 'com.google.oauth-client:google-oauth-client-jetty:1.32.1'")
    implementation ("com.google.apis:google-api-services-drive:v3-rev305-1.25.0")

    /*    implementation ("libs/google-api-services-drive-v2-rev1-1.7.2-beta.jar")
        implementation ("libs/google-http-client-1.10.3-beta.jar")
        implementation ("libs/google-http-client-android2-1.10.3-beta.jar")
        implementation ("libs/google-oauth-client-1.10.1-beta.jar")
        implementation ("libs/google-api-client-android2-1.10.3-beta.jar")
        implementation ("libs/google-api-client-1.10.3-beta.jar")*/


}