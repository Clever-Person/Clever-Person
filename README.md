# B2 Bound

A native Android study companion for a B1 learner moving toward B2 with American English lessons from the ZAmerican English YouTube channel.

## Project structure

```text
app/src/main/java/com/b2bound/
  data/       Room entities, DAOs, repositories, playlist links, YouTube URL parser
  di/         Hilt database providers and database migration
  ui/         Channel Hub, Study Player, ViewModels, Material 3 theme
app/src/test/ Unit tests for spaced-repetition scheduling
.github/workflows/android.yml  Unit tests and debug APK build
```

## Screens

1. **Channel Hub** — channel playlist links grouped by Grammar, Vocabulary, Listening, and Conversation; filters for topic and B1/B2 study band; add any YouTube video link to a personal study list.
2. **Study Player** — official YouTube IFrame Player embedded in a WebView, playback speed, 10-second replay, and tap-start/tap-end loop controls. The app never downloads videos, captions, or lessons.
3. **My Notes per Video** — save words or sentences in Room with the current playback timestamp; tap a note to seek back to that moment.
4. **Flashcards** — spaced repetition from saved notes, with US English text-to-speech and learner examples.
5. **Shadowing** — record a sentence, compare timing, and use on-device speech recognition feedback.
6. **Quick Quiz** — five B1–B2 self-check questions after a video.
7. **Daily Plan & Progress** — one video, ten words, a shadowing task, a quiz, streaks, reminders, and weekly study metrics.

The current build implements screens 1–3. Screens 4–7 are the next steps. The seeded playlist links are public links referenced from the channel's own study-plan descriptions; CEFR levels are not inferred from the channel's course numbering. Added videos can be assigned B1, B2, or B1–B2 by the learner.

## Run in Android Studio

1. Open this project folder in Android Studio and allow Gradle to sync.
2. Install Android SDK 36 if prompted.
3. Select an Android 8.0+ emulator or device and press **Run**.

YouTube playback requires an internet connection and is always served by YouTube's official embedded player. Personal video links and notes are stored locally in Room.

## Install a debug APK from GitHub Actions

Every push to `main` runs unit tests and builds a debug APK. Open the **Actions** tab, select the latest **Android APK** workflow run, and download its `B2-Bound-debug-apk` artifact. Unzip it and install `app-debug.apk` on your Android device. It is a test build, not a Play Store release.

## Tests

The workflow runs `gradle :app:testDebugUnitTest`. From Android Studio, right-click `app/src/test` and choose **Run tests**. The current unit tests cover first and second successful spaced-repetition reviews, failed recall, and invalid review quality.
