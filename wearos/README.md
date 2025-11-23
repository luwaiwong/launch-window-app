# Nominal WearOS App

A native WearOS application for tracking rocket launches, built with Kotlin and Jetpack Compose for Wear OS.

![WearOS App](https://img.shields.io/badge/WearOS-Compose-4CAF50)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue)
![Min SDK](https://img.shields.io/badge/Min%20SDK-30-green)

## Features

### 📱 Compose UI for Wear OS
- **Launch List** - Scrollable list of upcoming rocket launches
- **Launch Details** - Comprehensive information about each launch
- **Beautiful Design** - Dark theme optimized for OLED displays
- **Swipe Navigation** - Intuitive swipe gestures to navigate

### ⌚ Wear OS Tiles
- **Quick Launch Info** - View next launch directly on your watch face
- **Live Countdown** - Real-time countdown to next launch
- **Auto-updating** - Automatically refreshes with latest data

### 🚀 Launch Information
- Launch name and mission details
- Real-time countdown timers
- Launch provider information
- Rocket specifications
- Launch pad location
- Mission descriptions
- Launch status

## Architecture

The app follows modern Android development practices with:

- **MVVM Architecture** - Separation of concerns with ViewModel
- **Kotlin Coroutines** - Async operations and data flow
- **StateFlow** - Reactive state management
- **Repository Pattern** - Data layer abstraction
- **Compose Navigation** - Type-safe navigation

### Project Structure

```
wearos/
├── src/main/kotlin/com/luwaiwong/nominal/wear/
│   ├── data/
│   │   ├── LaunchData.kt          # Data models
│   │   ├── LaunchApiService.kt    # API client
│   │   └── LaunchRepository.kt    # Data repository
│   ├── presentation/
│   │   ├── MainActivity.kt        # Main activity & navigation
│   │   ├── LaunchViewModel.kt     # ViewModel
│   │   ├── LaunchListScreen.kt    # List screen composables
│   │   └── LaunchDetailScreen.kt  # Detail screen composables
│   ├── tile/
│   │   └── LaunchTileService.kt   # Wear OS tile service
│   └── ui/theme/
│       └── Theme.kt               # Material theme
└── src/main/
    ├── AndroidManifest.xml
    └── res/                       # Resources (strings, colors, etc.)
```

## Technology Stack

### Core Technologies
- **Kotlin** 1.9.22
- **Jetpack Compose** for Wear OS 1.3.0
- **Compose Navigation** for Wear OS 1.3.0
- **Wear Tiles** 1.3.0 (for watch face tiles)

### Libraries
- **Lifecycle & ViewModel** - Android Architecture Components
- **Coroutines** - Async programming
- **OkHttp** - HTTP client
- **Gson** - JSON serialization
- **Material Design** for Wear OS

### API
- **TheSpaceDevs Launch Library 2** - https://ll.thespacedevs.com/2.2.0/

## Building the App

### Prerequisites
- Android Studio Hedgehog or later
- WearOS emulator or physical device
- JDK 17+
- Kotlin 1.9.22+

### Build Steps

1. **Open in Android Studio**
```bash
cd wearos
# Open this directory in Android Studio
```

2. **Sync Gradle**
```bash
./gradlew sync
```

3. **Build Debug APK**
```bash
./gradlew assembleDebug
```

4. **Build Release APK**
```bash
./gradlew assembleRelease
```

### Running on WearOS Emulator

1. Create a WearOS emulator in Android Studio (API 30+)
2. Start the emulator
3. Click "Run" in Android Studio

### Installing on Physical Device

1. Enable Developer Options on your WearOS device
2. Enable ADB debugging
3. Connect via ADB over WiFi or Bluetooth
4. Run: `./gradlew installDebug`

## Using the App

### Main Features

**Launch List**
- Scroll through upcoming launches
- See countdown timers for each launch
- Tap any launch to view details

**Launch Details**
- View comprehensive launch information
- See mission descriptions
- Check launch provider and rocket details
- Swipe right to go back

**Tiles**
- Long press on watch face
- Swipe to "Add tile"
- Select "Next Launch" tile
- View upcoming launch info at a glance

## Customization

### Changing Colors

Edit `ui/theme/Theme.kt`:
```kotlin
object NominalColors {
    val Primary = Color(0xFF4CAF50) // Change primary color
    val Background = Color(0xFF1E1E1E) // Change background
    // ... other colors
}
```

### Adjusting Update Frequency

The tile updates automatically. To customize refresh behavior, modify the tile service implementation in `tile/LaunchTileService.kt`.

### API Configuration

To switch to development API, edit `data/LaunchApiService.kt`:
```kotlin
private val baseUrl = "https://lldev.thespacedevs.com/2.2.0" // Dev API
```

## Performance Optimization

### Battery Life
- Dark OLED theme reduces power consumption
- Efficient API calls with caching
- Minimal background processing

### Data Usage
- Fetches only necessary data
- Configurable API limits
- Efficient JSON parsing

### Memory
- Lazy loading with Compose
- State management with StateFlow
- Proper lifecycle handling

## Troubleshooting

### App Won't Install
- Check minimum SDK is 30 (Android 11)
- Verify WearOS device has enough storage
- Ensure developer mode is enabled

### No Data Showing
- Check internet connection on watch
- Verify API endpoint is accessible
- Check logcat for errors: `adb logcat | grep Nominal`

### Tile Not Updating
- Remove and re-add the tile
- Force stop and restart the app
- Check network connectivity

### Build Errors
- Clean project: `./gradlew clean`
- Invalidate caches in Android Studio
- Ensure all dependencies are synced

## API Integration

### TheSpaceDevs API
The app uses the TheSpaceDevs Launch Library 2.2.0 API:

**Endpoints Used:**
- `/launch/upcoming/` - Fetch upcoming launches
- `/launch/previous/` - Fetch past launches

**Rate Limiting:**
- Development API: 15 requests/hour
- Production API: More generous limits
- App implements smart caching to minimize requests

**Data Processing:**
- Custom Gson deserializer for nested JSON
- Error handling for network failures
- Graceful degradation when API is unavailable

## Future Enhancements

Planned features for future releases:

- [ ] Launch notifications with configurable alerts
- [ ] Favorite launches and providers
- [ ] Offline mode with caching
- [ ] Multiple tile designs
- [ ] Complication support for watch faces
- [ ] Voice search for launches
- [ ] Timeline view of launches
- [ ] Share launch information
- [ ] Watch face with launch countdown
- [ ] Launch filtering by provider/location

## Contributing

Contributions are welcome! Please ensure:

1. Code follows Kotlin style guide
2. Compose best practices are followed
3. All features are tested on WearOS emulator
4. Documentation is updated

## License

This project shares the same license as the main Nominal app.

## Support

For issues or questions:
- Check the main README.md
- Review Android logs: `adb logcat`
- Visit TheSpaceDevs API docs: https://thespacedevs.com

## Acknowledgments

- **TheSpaceDevs** - For providing the amazing Launch Library API
- **Google** - For Jetpack Compose for Wear OS
- **Kotlin Team** - For the excellent programming language

---

Built with ❤️ for space enthusiasts and WearOS users

**Watch the skies from your wrist! 🚀⌚**
