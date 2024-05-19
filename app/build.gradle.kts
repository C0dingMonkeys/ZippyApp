import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}



android {
    namespace = "com.example.zippy0001"
    compileSdk = 34

    android { packagingOptions { resources.excludes.add("META-INF/*") } }

    defaultConfig {
        applicationId = "com.example.zippy0001"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}


dependencies {
    implementation("io.github.vicmikhailau:MaskedEditText:5.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("com.github.Drjacky:ImagePicker:1.8.4")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.koushikdutta.ion:ion:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")

    implementation("com.karumi:dexter:6.2.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}