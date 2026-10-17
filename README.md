# FCL Turnip Plugin

Android APK wrapper for the MrPurple Turnip Vulkan driver, based on
[FCL-Team/FCLDriverPlugin](https://github.com/FCL-Team/FCLDriverPlugin).

| Field | Value |
| --- | --- |
| Application ID | `com.mio.plugin.renderer.turnip26xx` |
| Android application name | `Turnip 26.x.x` |
| FCL driver name | `Turnip 26.x.x` |
| APK version | `1.0` / versionCode `1` |
| ABI | `arm64-v8a` |
| Minimum Android version | Android 11 / API 30 (required by the bundled driver) |
| Compile / target SDK | 37 / 37 |
| AGP / Gradle | 9.4.0 / 9.6.0 |
| Kotlin | AGP built-in Kotlin (AGP 9.4.0 declares KGP 2.2.10) |
| Build JDK / bytecode target | JDK 21 / Java 17 |

## Driver provenance

Source release:
https://github.com/MrPurple666/purple-turnip/releases/

The archive's `vulkan.purple.so` is copied byte-for-byte to
`app/src/main/jniLibs/arm64-v8a/libvulkan_freedreno.so` for the existing FCL driver
loading convention. No binary patches or stripping are applied.

## Device test

Install the APK and restart FCL. Confirm the driver list contains
`Turnip xxxx`, select it with a compatible renderer, and test a game.
Building and verifying the APK does not establish GPU or game compatibility.
