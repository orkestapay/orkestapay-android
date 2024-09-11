import com.vanniktech.maven.publish.SonatypeHost

val groupId by extra { "com.orkestapay" }
val artifactId by extra { "orkestapay" }
val libraryVersion by extra { "0.0.9" }

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "1.9.23"
    id("com.vanniktech.maven.publish") version "0.28.0"

}

android {
    namespace = "com.orkestapay"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
        coordinates(groupId, artifactId, libraryVersion)

        pom {
            name.set("Orkestapay library")
            description.set("Orkestapay Android create payment methods and get promotions")
            inceptionYear.set("2024")
            url.set("https://github.com/orkestapay/orkestapay-android")
            licenses {
                license {
                    name.set("GPL-3.0 license")
                    url.set("https://github.com/orkestapay/orkestapay-android/blob/main/LICENSE")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("hector-ork")
                    name.set("Hector Rodriguez")
                    url.set("https://github.com/hector-ork")
                }
            }
            scm {
                url.set("https://github.com/orkestapay/orkestapay-android.git")
                connection.set("scm:git:git@github.com:orkestapay/orkestapay-android.git")
                //developerConnection.set("scm:git:ssh://git@github.com:orkestapay/orkestapay-android.git")
            }
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.wallet)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.wallet)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.awaitility.kotlin)


}