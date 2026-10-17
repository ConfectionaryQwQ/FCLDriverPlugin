plugins {
    alias(libs.plugins.android.application)
}

val signingValues = listOf("FCL_KEYSTORE", "FCL_STORE_PASSWORD", "FCL_KEY_ALIAS", "FCL_KEY_PASSWORD")
    .associateWith { providers.environmentVariable(it).orNull }
val hasReleaseSigning = signingValues.values.all { !it.isNullOrBlank() }
if (gradle.startParameter.taskNames.any { it.contains("release", ignoreCase = true) } && !hasReleaseSigning) {
    throw GradleException("Release signing is required. Use build-release.ps1 or set all FCL signing environment variables.")
}

android {
    namespace = "com.mio.plugin.renderer"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.mio.plugin.renderer"
        // MrPurple T30 requires Android 11 / API 30 (see upstream meta.json).
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        resValues = true
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(signingValues.getValue("FCL_KEYSTORE")!!)
                storePassword = signingValues.getValue("FCL_STORE_PASSWORD")
                keyAlias = signingValues.getValue("FCL_KEY_ALIAS")
                keyPassword = signingValues.getValue("FCL_KEY_PASSWORD")
            }
        }
    }

    packaging {
        jniLibs {
            // FCL needs the installed nativeLibraryDir, so keep libraries extracted.
            useLegacyPackaging = true
            // Preserve the upstream prebuilt binary exactly; no NDK stripping.
            keepDebugSymbols += "**/libvulkan_freedreno.so"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (hasReleaseSigning) signingConfig = signingConfigs.getByName("release")
        }
        configureEach {
            //应用名
            //app name
            resValue("string", "app_name", "Turnip 26.3.0")
            //包名后缀
            //package name Suffix
            applicationIdSuffix = ".turnip2630"

            //驱动在启动器内显示的名称
            //The name displayed by the driver in the launcher
            manifestPlaceholders["driver"] = "Turnip 26.3.0_MrPurple"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
}
