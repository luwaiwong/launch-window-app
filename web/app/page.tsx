'use client';

import { useEffect, useState } from 'react';
import { apiClient } from '@/lib/api/client';
import { Launch, Event, Article } from '@/lib/types';
import { LaunchCard } from '@/components/cards/LaunchCard';
import { EventCard } from '@/components/cards/EventCard';
import { ArticleCard } from '@/components/cards/ArticleCard';
import { storage } from '@/lib/storage';

export default function Dashboard() {
  const [highlightLaunch, setHighlightLaunch] = useState<Launch | null>(null);
  const [recentLaunches, setRecentLaunches] = useState<Launch[]>([]);
  const [upcomingLaunches, setUpcomingLaunches] = useState<Launch[]>([]);
  const [upcomingEvents, setUpcomingEvents] = useState<Event[]>([]);
  const [articles, setArticles] = useState<Article[]>([]);
  const [loading, setLoading] = useState(true);
  const [favorites, setFavorites] = useState<string[]>([]);

  const loadData = async () => {
    setLoading(true);
    try {
      const [upcomingRes, previousRes, eventsRes, articlesRes] = await Promise.all([
        apiClient.getUpcomingLaunches(5),
        apiClient.getPreviousLaunches(3),
        apiClient.getUpcomingEvents(2),
        apiClient.getArticles(2),
      ]);

      // Set highlight launch (first upcoming)
      if (upcomingRes.results.length > 0) {
        setHighlightLaunch(upcomingRes.results[0]);
        setUpcomingLaunches(upcomingRes.results.slice(1, 4));
      }

      setRecentLaunches(previousRes.results);
      setUpcomingEvents(eventsRes.results.slice(0, 1));
      setArticles(articlesRes.results);

      // Load favorites from storage
      setFavorites(storage.getFavorites());
    } catch (error) {
      console.error('Error loading dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleFavoriteToggle = (launchId: string) => {
    const newFavorites = storage.toggleFavorite(launchId);
    setFavorites(storage.getFavorites());
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">🚀</div>
          <p className="text-muted">Loading launch data...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-6xl">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Dashboard</h1>
        <p className="text-muted">Your personalized rocket launch feed</p>
      </div>

      {/* Highlight Launch */}
      {highlightLaunch && (
        <section className="mb-8">
          <LaunchCard launch={highlightLaunch} variant="highlight" />
        </section>
      )}

      {/* Recently Launched */}
      {recentLaunches.length > 0 && (
        <section className="mb-8">
          <h2 className="text-xl font-semibold text-foreground mb-4">Recently Launched</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {recentLaunches.map((launch) => (
              <LaunchCard
                key={launch.id}
                launch={launch}
                variant="default"
                isFavorite={favorites.includes(launch.id)}
                onFavoriteToggle={handleFavoriteToggle}
              />
            ))}
          </div>
        </section>
      )}

      {/* Upcoming Launches */}
      {upcomingLaunches.length > 0 && (
        <section className="mb-8">
          <h2 className="text-xl font-semibold text-foreground mb-4">Upcoming Launches</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {upcomingLaunches.map((launch) => (
              <LaunchCard
                key={launch.id}
                launch={launch}
                variant="default"
                isFavorite={favorites.includes(launch.id)}
                onFavoriteToggle={handleFavoriteToggle}
              />
            ))}
          </div>
        </section>
      )}

      {/* Upcoming Events */}
      {upcomingEvents.length > 0 && (
        <section className="mb-8">
          <h2 className="text-xl font-semibold text-foreground mb-4">Upcoming Events</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {upcomingEvents.map((event) => (
              <EventCard key={event.id} event={event} variant="default" />
            ))}
          </div>
        </section>
      )}

      {/* Recent Articles */}
      {articles.length > 0 && (
        <section className="mb-8">
          <h2 className="text-xl font-semibold text-foreground mb-4">Recent News</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {articles.map((article) => (
              <ArticleCard key={article.id} article={article} variant="default" />
            ))}
          </div>
        </section>
      )}

      {/* Refresh Button */}
      <div className="flex justify-center mt-8">
        <button
          onClick={loadData}
          className="px-6 py-3 bg-accent text-white rounded-lg hover:bg-accent/80 transition-colors"
        >
          Refresh Data
        </button>
      </div>
    </div>
  );
}
