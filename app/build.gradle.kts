plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mio.plugin.renderer"
    compileSdk = 34

    signingConfigs {
        create("releaseBuild") {
            storeFile = file("FCL-key.jks")
            storePassword = "666666"
            keyAlias = "FCL-Key"
            keyPassword = "666666"
        }
    }

    defaultConfig {
        applicationId = "com.mio.plugin.renderer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("releaseBuild")
        }
        configureEach {
            //应用名
            //app name
            resValue("string","app_name","Turnip 25.2.0_a710_a720_test")
            //包名后缀
            //package name Suffix
            applicationIdSuffix = ".turnip2520.r27c.sdk29"

            //驱动在启动器内显示的名称
            //The name displayed by the driver in the launcher
            manifestPlaceholders["driver"] = "Turnip 25.2.0_a710_a720_test"
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
}
