'use client';

import { useEffect, useState } from 'react';
import { apiClient } from '@/lib/api/client';
import { Launch, Event } from '@/lib/types';
import { LaunchCard } from '@/components/cards/LaunchCard';
import { EventCard } from '@/components/cards/EventCard';

export default function ForYou() {
  const [feedItems, setFeedItems] = useState<Array<Launch | Event>>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadFeed = async () => {
      try {
        const items = await apiClient.getFeedItems(20);
        setFeedItems(items);
      } catch (error) {
        console.error('Error loading feed:', error);
      } finally {
        setLoading(false);
      }
    };

    loadFeed();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">🚀</div>
          <p className="text-muted">Loading your feed...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-4xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">For You</h1>
        <p className="text-muted">Personalized feed of upcoming launches and events</p>
      </div>

      <div className="space-y-4">
        {feedItems.map((item) => {
          if ('net' in item) {
            // It's a Launch
            return <LaunchCard key={item.id} launch={item} variant="default" />;
          } else {
            // It's an Event
            return <EventCard key={item.id} event={item} variant="default" />;
          }
        })}
      </div>
    </div>
  );
}
