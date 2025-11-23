'use client';

import { useEffect, useState } from 'react';
import { apiClient } from '@/lib/api/client';
import { Event, Article } from '@/lib/types';
import { EventCard } from '@/components/cards/EventCard';
import { ArticleCard } from '@/components/cards/ArticleCard';

export default function News() {
  const [events, setEvents] = useState<Event[]>([]);
  const [articles, setArticles] = useState<Article[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [eventsRes, articlesRes] = await Promise.all([
          apiClient.getUpcomingEvents(10),
          apiClient.getArticles(20),
        ]);

        setEvents(eventsRes.results);
        setArticles(articlesRes.results);
      } catch (error) {
        console.error('Error loading news and events:', error);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">📰</div>
          <p className="text-muted">Loading news...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-6xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">News & Events</h1>
        <p className="text-muted">Latest space news and upcoming events</p>
      </div>

      {/* Upcoming Events */}
      {events.length > 0 && (
        <section className="mb-8">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold text-foreground">Upcoming Events</h2>
            <span className="text-sm text-muted">{events.length} events</span>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {events.slice(0, 6).map((event) => (
              <EventCard key={event.id} event={event} variant="default" />
            ))}
          </div>
        </section>
      )}

      {/* News Articles */}
      {articles.length > 0 && (
        <section>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold text-foreground">Recent Articles</h2>
            <span className="text-sm text-muted">{articles.length} articles</span>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {articles.map((article) => (
              <ArticleCard key={article.id} article={article} variant="default" />
            ))}
          </div>
        </section>
      )}
    </div>
  );
}
