# Nominal, a spaceflight and rocket launch tracking app

<img src="https://github.com/user-attachments/assets/a00f1a67-e20a-402c-9bd2-fcf11624ea65" width="200" />

There are a couple rocket launch tracking apps out there, but I want one thats more customizable and with a prettier UI.
That's why I'm working on Nominal


<img src="https://github.com/user-attachments/assets/73702b95-cd84-4014-9a9c-b32a340b8724" width="300" />
<img src="https://github.com/user-attachments/assets/fb8a4a98-4703-4d7b-b2f2-6e88a7489a01" width="300" />

## Platforms

- **Mobile** - Built with React Native
- **WearOS** - Native Kotlin app with Jetpack Compose ⌚

Using [TheSpaceDevs](https://thespacedevs.com/llapi) API

## Features

- 🚀 Track upcoming and past rocket launches
- 📱 Beautiful, customizable mobile UI
- ⌚ **Native WearOS app** with Compose
- 🔔 Launch notifications
- 📰 Space news integration
- 🎯 Event tracking
- 🏷️ WearOS Tiles for quick launch info

## Commands

### Mobile App (React Native)
```bash
# To run expo preview
npm start

# To build the app using expo to apk
eas build -p android --profile preview
```

### WearOS App (Kotlin + Compose)
```bash
# Navigate to wearos directory
cd wearos

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

## WearOS App

A companion WearOS app built with Kotlin and Jetpack Compose! View upcoming launches on your smartwatch with:

- 📋 Scrollable launch list with live countdowns
- 📝 Detailed launch information
- 🏷️ Wear OS tiles for watch face integration
- 🎨 Beautiful dark theme optimized for OLED
- 🔄 Auto-refreshing data

See [wearos/README.md](wearos/README.md) for detailed documentation.
