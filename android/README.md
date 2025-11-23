# Nominal - Jetpack Compose Android App

A native Android reimplementation of the Nominal rocket launch tracking app using Jetpack Compose and Kotlin.

## Overview

Nominal is a spaceflight and rocket launch tracking application that allows users to discover, monitor, and get notifications about upcoming rocket launches, space events, and related news. This version is built with modern Android development practices using Jetpack Compose.

## Features

### Core Features
- **Upcoming Launch Tracking** - Browse upcoming rocket launches with countdown timers, status, rocket info, launch provider, and location
- **Past Launch History** - View recently launched missions with status information (successful, failed, partial failure)
- **Live Countdown Timer (T-Minus)** - Real-time countdown display showing days, hours, minutes, and seconds until launch
- **Event Management** - Track space events (tests, conferences, etc.) with descriptions, locations, and associated launches
- **News/Articles Feed** - Browse space-related articles from multiple news sources
- **"For You" Personalized Feed** - Vertical scrolling feed showing upcoming launches and events chronologically
- **Dashboard** - Curated view showing next highlight launch, upcoming launches, featured events, and articles
- **Push Notifications** - Customizable notifications for launches and events (24h, 12h, 1h, 30m, 10m before)
- **Settings Management** - Control notifications, For You feed display options, developer mode
- **Data Persistence** - Local caching with 25-minute cache expiry
- **Pull-to-Refresh** - Reload data manually from dashboard

### Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit + OkHttp
- **Data Storage:** DataStore Preferences + File Cache
- **Image Loading:** Coil
- **Navigation:** Jetpack Navigation Compose
- **Coroutines:** For asynchronous operations
- **Material Design 3:** Dark theme with custom colors

## Architecture

```
com.nominal/
├── data/
│   ├── models/          # Data models (Launch, Event, Article, etc.)
│   ├── api/             # Retrofit API interfaces
│   └── repository/      # Data repositories with caching logic
├── ui/
│   ├── screens/         # Screen composables (Dashboard, Launches, etc.)
│   ├── components/      # Reusable UI components
│   ├── theme/           # Material Theme configuration
│   ├── navigation/      # Navigation setup
│   └── viewmodels/      # ViewModels for state management
├── utils/               # Utility functions
└── notifications/       # Notification handling
```

## Data Sources

The app integrates with two external APIs:

1. **TheSpaceDevs Launch Library API**
   - Endpoint: `https://ll.thespacedevs.com/2.2.0/`
   - Provides launch and event data

2. **Spaceflight News API**
   - Endpoint: `https://api.spaceflightnewsapi.net/v4/`
   - Provides space-related news articles

## Requirements

- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34
- **Kotlin:** 1.9.20
- **Gradle:** 8.2
- **Java:** 17

## Building the Project

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd launch-window-app/android
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android` directory

3. **Sync Gradle**
   - Wait for Gradle to sync dependencies
   - If prompted, accept any SDK licenses

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10
   - Select your target device

## Project Structure

### Key Files

- `MainActivity.kt` - Entry point, sets up Compose and navigation
- `NominalApplication.kt` - Application class, initializes notification channels
- `MainViewModel.kt` - Main ViewModel managing app state
- `NominalRepository.kt` - Data repository with API calls and caching
- `SettingsRepository.kt` - User preferences management
- `Navigation.kt` - Navigation graph setup

### Screens

1. **Dashboard** - Home screen with highlights and curated content
2. **For You** - Personalized feed of launches and events
3. **Launches** - Tabbed view of upcoming and previous launches
4. **Settings** - User preferences and app settings
5. **Launch Detail** - Detailed view of a single launch

### UI Components

- `LaunchCard` - Display launch information in a card
- `EventCard` - Display event information
- `ArticleCard` - Display news article
- `HighlightLaunch` - Featured launch with full-screen image
- `TMinus` - Real-time countdown timer

## Features Comparison with Expo App

| Feature | Expo App | Jetpack Compose App |
|---------|----------|---------------------|
| Launch tracking | ✅ | ✅ |
| Event tracking | ✅ | ✅ |
| News articles | ✅ | ✅ |
| Countdown timer | ✅ | ✅ |
| For You feed | ✅ | ✅ |
| Dashboard | ✅ | ✅ |
| Settings | ✅ | ✅ |
| Notifications | ✅ | ✅ (Framework ready) |
| Pull-to-refresh | ✅ | ✅ |
| Data caching | ✅ (25 min) | ✅ (25 min) |
| Dark theme | ✅ | ✅ |
| Developer mode | ✅ | ✅ |
| Auto-scrolling carousel | ✅ | ⏳ (Can be added) |
| Deep linking | ⏳ | ⏳ (Framework ready) |

✅ = Implemented | ⏳ = Framework ready, easy to implement

## Customization

### Changing Theme Colors

Edit `ui/theme/Color.kt` to customize the color scheme:

```kotlin
val Background = Color(0xFF1E1E1E)
val Accent = Color(0xFF5E81AC)
// ... etc
```

### Modifying Cache Duration

Edit `NominalRepository.kt`:

```kotlin
private const val CACHE_EXPIRY_MS = 25 * 60 * 1000L // 25 minutes
```

### Adding Notification Intervals

Edit `SettingsRepository.kt` to add new notification timing options.

## Known Limitations

1. **Font**: Space Grotesk font not included - using system default. To add:
   - Download Space Grotesk font files
   - Place in `app/src/main/res/font/`
   - Update `ui/theme/Type.kt`

2. **Notifications**: Notification scheduling framework is ready but not fully implemented
3. **Auto-carousel**: Can be added using LaunchedEffect with auto-scroll logic
4. **Deep linking**: Navigation framework supports it, needs URL scheme configuration

## Contributing

When contributing to this project:

1. Follow Kotlin coding conventions
2. Use Jetpack Compose best practices
3. Maintain the MVVM architecture pattern
4. Add documentation for new features
5. Test on multiple screen sizes

## License

This project follows the same license as the parent repository.

## Credits

- **Original Expo App**: See parent repository
- **APIs**: TheSpaceDevs & Spaceflight News API
- **Icons**: Material Icons
- **Image Loading**: Coil library

## Support

For issues specific to the Android version, please file an issue in the repository with the `android` tag.

---

**Built with ❤️ using Jetpack Compose**
