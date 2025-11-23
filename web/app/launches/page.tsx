'use client';

import { useEffect, useState } from 'react';
import { apiClient } from '@/lib/api/client';
import { Launch } from '@/lib/types';
import { LaunchCard } from '@/components/cards/LaunchCard';
import { storage } from '@/lib/storage';

export default function Launches() {
  const [activeTab, setActiveTab] = useState<'upcoming' | 'previous'>('upcoming');
  const [upcomingLaunches, setUpcomingLaunches] = useState<Launch[]>([]);
  const [previousLaunches, setPreviousLaunches] = useState<Launch[]>([]);
  const [loading, setLoading] = useState(true);
  const [favorites, setFavorites] = useState<string[]>([]);

  useEffect(() => {
    const loadLaunches = async () => {
      setLoading(true);
      try {
        const [upcoming, previous] = await Promise.all([
          apiClient.getUpcomingLaunches(20),
          apiClient.getPreviousLaunches(20),
        ]);

        setUpcomingLaunches(upcoming.results);
        setPreviousLaunches(previous.results);
        setFavorites(storage.getFavorites());
      } catch (error) {
        console.error('Error loading launches:', error);
      } finally {
        setLoading(false);
      }
    };

    loadLaunches();
  }, []);

  const handleFavoriteToggle = (launchId: string) => {
    storage.toggleFavorite(launchId);
    setFavorites(storage.getFavorites());
  };

  const launches = activeTab === 'upcoming' ? upcomingLaunches : previousLaunches;

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">🚀</div>
          <p className="text-muted">Loading launches...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-6xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Launches</h1>
        <p className="text-muted">Browse all rocket launches</p>
      </div>

      {/* Tab Selector */}
      <div className="flex gap-2 mb-6 bg-highlight rounded-lg p-1">
        <button
          onClick={() => setActiveTab('upcoming')}
          className={`flex-1 py-3 px-6 rounded-md font-medium transition-colors ${
            activeTab === 'upcoming'
              ? 'bg-accent text-white'
              : 'text-muted hover:text-foreground'
          }`}
        >
          Upcoming ({upcomingLaunches.length})
        </button>
        <button
          onClick={() => setActiveTab('previous')}
          className={`flex-1 py-3 px-6 rounded-md font-medium transition-colors ${
            activeTab === 'previous'
              ? 'bg-accent text-white'
              : 'text-muted hover:text-foreground'
          }`}
        >
          Previous ({previousLaunches.length})
        </button>
      </div>

      {/* Launches Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {launches.map((launch) => (
          <LaunchCard
            key={launch.id}
            launch={launch}
            variant="default"
            isFavorite={favorites.includes(launch.id)}
            onFavoriteToggle={handleFavoriteToggle}
          />
        ))}
      </div>
    </div>
  );
}
