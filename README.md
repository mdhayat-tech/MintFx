# MintFx

A cloud-buildable currency converter for Android using Kotlin, Jetpack Compose, Retrofit, and SharedPreferences.

## Offline behavior

The app fetches the latest USD-based rates from the free ExchangeRate-API endpoint. A successful response replaces the JSON rates snapshot in SharedPreferences. If a request fails, the most recently cached rates are used and the UI displays `Using Cached Rates`.

## GitHub Actions

The workflow in `.github/workflows/android-ci.yml` runs lint, unit tests, and a debug APK build on `ubuntu-latest` with JDK 17. The generated APK is uploaded as the `mintfx-debug-apk` artifact.

## Local-equivalent commands

```bash
chmod +x gradlew
./gradlew lintDebug
./gradlew testDebugUnitTest
./gradlew assembleDebug
```
