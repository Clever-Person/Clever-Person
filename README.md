# B2 Bound

An offline-first Android English-learning app for a B1 learner working toward B2 American English.

## Project structure

```text
app/src/main/java/com/b2bound/
  data/       Room entities, DAO, repository, spaced-repetition scheduler
  di/         Hilt database providers
  ui/         Compose home, vocabulary trainer, view models, navigation
  ui/theme/   Material 3 light and dark color schemes
```

## Screen-by-screen plan

1. **Home / Daily Plan** — 20-minute routine, streak, B1→B2 progress, four activity cards. Current slice includes the responsive plan and entry point into vocabulary; streak and progress are visual starter values pending persisted study-session tracking.
2. **Vocabulary** — due-first flashcards, definition/example reveal, US text-to-speech, personal word entry, and SM-2 inspired review scheduling. Saved in Room and works offline. Starter database is empty so learners can add personal words; licensed Oxford 3000/AWL data can be imported into the same schema.
3. **Listening** — short licensed US clips, playback speed, subtitle toggle, and comprehension checks.
4. **Speaking** — shadowing prompt, recording, waveform comparison, Android speech recognition feedback.
5. **Grammar** — short B1→B2 lessons and quizzes for conditionals, passive voice, reported speech, and phrasal verbs.
6. **AI Conversation Partner** — text/voice roleplays and gentle correction. Retrofit is included for a backend connection; a secure API endpoint and credentials are needed before this can be enabled.
7. **Level Tests** — placement assessment and fortnightly B2 mini-test with weak-area report.
8. **Progress & Reminders** — study history, charts, daily notification preference, and weekly summary.

## Build and run in Android Studio

1. Open this folder in Android Studio (a recent version with Android SDK 36 support).
2. Let Gradle sync and install the requested Android SDK/build tools if prompted.
3. Select the `app` run configuration and an Android 8.0+ device or emulator, then press **Run**.

The first Gradle sync needs internet to resolve AndroidX and plugin dependencies. The vocabulary screens and local Room data are offline after installation. The Listen, Speak, Grammar, Tests, and Progress tabs are intentionally placeholders for the next implementation steps; AI conversation requires an application backend.

## Download an APK from GitHub Actions

The `Android APK` workflow runs the unit tests and builds a debug APK on every push to `main`, and can also be started from the GitHub **Actions** tab with **Run workflow**. After a successful run, open that workflow run, download the `B2-Bound-debug-apk` artifact, unzip it, and install `app-debug.apk` on an Android 8.0+ device. This is a debug build for personal testing, not a Play Store release.

## Run tests

In Android Studio, right-click `app/src/test` and choose **Run tests**. The GitHub Actions workflow also runs `gradle :app:testDebugUnitTest` before building the APK. The scheduler tests cover first and second successful reviews, failed recall, and invalid quality input.

## Versions

Dependency versions follow the current Android Developers guidance, including the September 2026 Compose BOM and Room 2.8.5. The initial implementation targets SDK 36 and minSdk 26.
