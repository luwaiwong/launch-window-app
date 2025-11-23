# Nominal Web - Rocket Launch Tracker

A modern web application for tracking rocket launches, space events, and space news. Built with Next.js 16, TypeScript, and Tailwind CSS.

## Features

### 🚀 Launch Tracking
- Browse upcoming and previous rocket launches
- Real-time countdown timers (T-minus)
- Detailed launch information including:
  - Mission details and descriptions
  - Rocket specifications
  - Launch location and pad info
  - Launch service providers
  - Mission agencies
- Favorite/pin launches for quick access

### 📅 Space Events
- Browse upcoming space events
- Event details with images and descriptions
- Related launches for each event

### 📰 Space News
- Latest space industry news articles
- Articles from multiple news sources
- Direct links to full articles

### 🏠 Dashboard
- Personalized feed with:
  - Highlighted next launch
  - Recently completed launches
  - Upcoming launches
  - Upcoming events
  - Recent news articles

### ⚙️ Settings
- Notification preferences (UI ready, implementation pending)
- Feed customization
- Data management
- Developer mode

## Tech Stack

- **Framework**: Next.js 16 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS v4
- **Font**: Space Grotesk (Google Fonts)
- **APIs**:
  - The Space Devs API (launches and events)
  - Spaceflight News API (news articles)

## Architecture

### Backend API Routes
The application includes a backend API layer that:
- Proxies requests to external APIs
- Implements caching (25-minute TTL)
- Can obscure API keys (for future use)
- Is accessible by both web and mobile apps

API Routes:
- `/api/launches/upcoming` - Upcoming launches
- `/api/launches/previous` - Previous launches
- `/api/launches/[id]` - Launch details
- `/api/events/upcoming` - Upcoming events
- `/api/events/previous` - Previous events
- `/api/events/[id]` - Event details
- `/api/news` - News articles

### Frontend Architecture
- **Client Components**: All pages use client-side rendering for interactivity
- **State Management**: React hooks (useState, useEffect)
- **Local Storage**: User settings and favorites persistence
- **Responsive Design**: Mobile-first with desktop optimization

### Project Structure
```
web/
├── app/                    # Next.js app directory
│   ├── api/               # API routes
│   │   ├── launches/      # Launch endpoints
│   │   ├── events/        # Event endpoints
│   │   └── news/          # News endpoints
│   ├── launches/          # Launches page and detail
│   ├── events/            # Events detail pages
│   ├── for-you/           # For You feed page
│   ├── news/              # News & Events page
│   ├── settings/          # Settings page
│   ├── layout.tsx         # Root layout
│   ├── page.tsx           # Dashboard (home)
│   └── globals.css        # Global styles
├── components/            # React components
│   ├── cards/            # Card components
│   │   ├── LaunchCard.tsx
│   │   ├── EventCard.tsx
│   │   └── ArticleCard.tsx
│   └── ui/               # UI components
│       ├── Navigation.tsx
│       └── TMinus.tsx
└── lib/                  # Utilities and types
    ├── api/             # API client and cache
    │   ├── client.ts
    │   └── cache.ts
    ├── types.ts         # TypeScript types
    ├── config.ts        # Configuration
    ├── utils.ts         # Utility functions
    └── storage.ts       # Local storage utilities
```

## Getting Started

### Prerequisites
- Node.js 18+
- npm or yarn

### Installation

1. Navigate to the web directory:
```bash
cd web
```

2. Install dependencies:
```bash
npm install
```

3. Run the development server:
```bash
npm run dev
```

4. Open [http://localhost:3000](http://localhost:3000) in your browser

### Building for Production

```bash
npm run build
npm start
```

## Environment Variables

Create a `.env.local` file in the web directory (optional):

```env
# Optional: Override API URLs
NEXT_PUBLIC_SPACE_DEVS_URL=https://ll.thespacedevs.com/2.2.0
NEXT_PUBLIC_SPACEFLIGHT_NEWS_URL=https://api.spaceflightnewsapi.net/v4
```

## API Caching

The backend implements a 25-minute cache for all API responses to:
- Reduce load on external APIs
- Improve response times
- Provide fallback data if APIs are unavailable

## Mobile App Integration

The backend API routes can be consumed by the React Native mobile app:

```typescript
// Example: Fetch upcoming launches from web API
const response = await fetch('https://your-domain.com/api/launches/upcoming?limit=10');
const data = await response.json();
```

This allows the mobile app to:
- Use the same data sources
- Benefit from server-side caching
- Access obscured API keys (when implemented)

## Theme

The application uses a dark theme matching the Expo mobile app:
- Background: `#1e1e1e`
- Highlight: `#252627`
- Foreground: `#D8DEE9`
- Accent: `#5E81AC`
- Success: `#A3DF95`
- Warning: `#FFDA61`
- Error: `#F75D55`

## Features Parity with Expo App

✅ Dashboard with highlighted launch
✅ For You personalized feed
✅ Launches browser (upcoming/previous)
✅ News & Events page
✅ Settings page
✅ Launch detail pages
✅ Event detail pages
✅ T-minus countdown timers
✅ Dark theme
✅ Space Grotesk font
✅ Favorite/pin functionality
✅ Local storage persistence
✅ Backend API for mobile app access

## Future Enhancements

- [ ] Web push notifications
- [ ] Infinite scroll for launch lists
- [ ] Search and filter functionality
- [ ] User accounts and cloud sync
- [ ] Live webcast integration
- [ ] Calendar export
- [ ] Share functionality

## License

See the root LICENSE file.

## Credits

- Launch data: [The Space Devs API](https://thespacedevs.com/)
- News data: [Spaceflight News API](https://www.spaceflightnewsapi.net/)
- Font: [Space Grotesk](https://fonts.google.com/specimen/Space+Grotesk) by Florian Karsten
