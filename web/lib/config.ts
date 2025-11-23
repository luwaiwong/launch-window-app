// API Configuration

export const API_CONFIG = {
  // The Space Devs API
  SPACE_DEVS: {
    BASE_URL: process.env.NEXT_PUBLIC_SPACE_DEVS_URL || 'https://ll.thespacedevs.com/2.2.0',
    DEV_URL: 'https://lldev.thespacedevs.com/2.2.0',
  },

  // Spaceflight News API
  SPACEFLIGHT_NEWS: {
    BASE_URL: process.env.NEXT_PUBLIC_SPACEFLIGHT_NEWS_URL || 'https://api.spaceflightnewsapi.net/v4',
  },

  // Cache duration (25 minutes in milliseconds)
  CACHE_DURATION: 1000 * 60 * 25,
};

// Theme configuration matching Expo app
export const THEME = {
  colors: {
    background: '#1e1e1e',
    highlight: '#252627',
    foreground: '#D8DEE9',
    accent: '#5E81AC',
    success: '#A3DF95',
    warning: '#FFDA61',
    error: '#F75D55',
    muted: '#4C566A',
  },
  spacing: {
    topBar: '60px',
    bottomBar: '70px',
  },
};

// Default user settings
export const DEFAULT_SETTINGS = {
  notifications: {
    enabled: false,
    twentyFourHours: false,
    twelveHours: false,
    oneHour: false,
    thirtyMinutes: false,
    tenMinutes: false,
    launches: true,
    events: true,
  },
  forYou: {
    showPastLaunches: false,
    showPastEvents: false,
  },
  developerMode: false,
  favorites: [],
  theme: {
    background: '#1e1e1e',
    highlight: '#252627',
    foreground: '#D8DEE9',
    accent: '#5E81AC',
    success: '#A3DF95',
    warning: '#FFDA61',
    error: '#F75D55',
    muted: '#4C566A',
  },
  useCustomTheme: false,
};
