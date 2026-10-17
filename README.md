# FCL Turnip 26.3.0 plugin

Android APK wrapper for the MrPurple T30 Turnip Vulkan driver, based on
[FCL-Team/FCLDriverPlugin](https://github.com/FCL-Team/FCLDriverPlugin).

| Field | Value |
| --- | --- |
| Application ID | `com.mio.plugin.renderer.turnip2630` |
| Android application name | `Turnip 26.3.0` |
| FCL driver name | `Turnip 26.3.0_MrPurple` |
| APK version | `1.0` / versionCode `1` |
| ABI | `arm64-v8a` |
| Minimum Android version | Android 11 / API 30 (required by the bundled driver) |
| Compile / target SDK | 37 / 37 |
| AGP / Gradle | 9.4.0 / 9.6.0 |
| Kotlin | AGP built-in Kotlin (AGP 9.4.0 declares KGP 2.2.10) |
| Build JDK / bytecode target | JDK 21 / Java 17 |

## Driver provenance

Source release:
https://github.com/MrPurple666/purple-turnip/releases/tag/vturnip_mrpurple_T30-toasted.adpkg

The upstream driver is **26.3.0-devel**, T30, git `62ac221a33`, rather than
a Mesa 26.3.0 stable release. The APK display names follow the requested naming.
Upstream metadata is preserved in `app/src/main/assets/driver/upstream-meta.json`.
Upstream identifies the author as `Mr_Purple_666` and the minimum API as 30.
The release notes mention partial withdrawal of A710/A720 support; test on the
intended GPU and FCL version.

The archive's `vulkan.purple.so` is copied byte-for-byte to
`app/src/main/jniLibs/arm64-v8a/libvulkan_freedreno.so` for the existing FCL driver
loading convention. No binary patches or stripping are applied.

Driver SHA-256:
`1d80dfa019659b008e4669311db5b1e4a02af59ff1d5458a98e2f5fe18ed013b`

The application retains `fclPlugin=true` and the `driver` manifest metadata.
Legacy native library packaging keeps the library extracted into the installed
application's native library directory. No driver source compilation is needed.

## Build a signed release

Install Android SDK Platform 37, Build Tools 36.0.0, and JDK 21.
Set `ANDROID_HOME` or configure `sdk.dir` in an ignored `local.properties` file.
From PowerShell:

```powershell
.\build-release.ps1 -KeystorePath 'C:\private\release.jks' -SdkPath 'C:\path\to\android-sdk'
```

The script prompts for the keystore password without displaying it. It assumes
the key password matches the keystore password. Supply your own `-KeystorePath`;
the default alias is `fcl-driver-release`. Override `-KeyAlias` or `-JavaHome` if needed.
`-PasswordFile` optionally reads a local password file without printing its contents.
Passwords are passed in process environment variables and are not saved to source.
Do not commit a password file or keystore.

For other build environments, configure `FCL_KEYSTORE`, `FCL_STORE_PASSWORD`,
`FCL_KEY_ALIAS`, and `FCL_KEY_PASSWORD`. Explicit release tasks fail if signing
configuration is missing. The build script runs `assembleRelease` and `lintRelease`.

APK output: `app/build/outputs/apk/release/app-release.apk`.
Use the same signing key and increase versionCode for future updates to this ID.

## GitHub Actions and downloads

Pushes to `main` or `codex/**`, pull requests to `main`, and manual workflow runs
build and lint a **debug-signed test APK**, downloadable from the workflow's
Artifacts section. CI does not require a private signing key. CI artifacts use
a different certificate from the locally signed release APK and cannot update
an installation signed by that release key.

Distribute the locally signed release APK through GitHub Releases. Include its
SHA-256 checksum, driver provenance, Android 11+ requirement, and relevant GPU
limitations in the release notes. Never add keystores or password files to Git.
The legacy tracked signing key has been removed from the current tree, but it
remains present in old Git commits and must not be treated as a private key.

## Device test

Install the APK and restart FCL. Confirm the driver list contains
`Turnip 26.3.0_MrPurple`, select it with a compatible renderer, and test a game.
Building and verifying the APK does not establish GPU or game compatibility.
