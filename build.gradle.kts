// Top-level build file where you can add configuration options common to all sub-projects/modules.

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import dagger.hilt.android.plugin.HiltExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        google()
    }
}

subprojects {

    plugins.withId(rootProject.libs.plugins.hilt.get().pluginId) {
        extensions.getByType<HiltExtension>().enableAggregatingTask = true
    }
    plugins.withId(rootProject.libs.plugins.kotlin.kapt.get().pluginId) {
        extensions.getByType<KaptExtension>().correctErrorTypes = true
    }
    plugins.withType<BasePlugin>().configureEach {
        extensions.configure<BaseExtension> {
            compileSdkVersion(libs.versions.compileSdk.get().toInt())
            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                targetSdk = libs.versions.targetSdk.get().toInt()
            }
            compileOptions {
//                sourceCompatibility = JavaVersion.VERSION_11
//                targetCompatibility = JavaVersion.VERSION_11
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }
}