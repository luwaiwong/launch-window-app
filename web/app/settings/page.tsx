'use client';

import { useEffect, useState } from 'react';
import { storage } from '@/lib/storage';
import { UserSettings } from '@/lib/types';

export default function Settings() {
  const [settings, setSettings] = useState<UserSettings | null>(null);
  const [favorites, setFavorites] = useState<string[]>([]);

  useEffect(() => {
    setSettings(storage.getSettings());
    setFavorites(storage.getFavorites());
  }, []);

  const handleToggle = (category: keyof UserSettings, key: string) => {
    if (!settings) return;

    const categoryValue = settings[category];
    const updated = {
      ...settings,
      [category]: {
        ...(categoryValue as any),
        [key]: !(categoryValue as any)[key],
      },
    };

    setSettings(updated);
    storage.saveSettings(updated);
  };

  const handleClearData = () => {
    if (confirm('Are you sure you want to clear all data? This cannot be undone.')) {
      storage.clearAll();
      setSettings(storage.getSettings());
      setFavorites([]);
      alert('All data cleared successfully');
    }
  };

  if (!settings) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">⚙️</div>
          <p className="text-muted">Loading settings...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-4xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">Settings</h1>
        <p className="text-muted">Manage your preferences</p>
      </div>

      {/* Notification Settings */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">Notifications</h2>
        <p className="text-sm text-muted mb-4">
          Note: Web notifications require browser permission and are not currently implemented.
        </p>

        <div className="space-y-3">
          <ToggleItem
            label="Enable Notifications"
            value={settings.notifications.enabled}
            onChange={() => handleToggle('notifications', 'enabled')}
          />
          <ToggleItem
            label="24 Hours Before Launch"
            value={settings.notifications.twentyFourHours}
            onChange={() => handleToggle('notifications', 'twentyFourHours')}
            disabled={!settings.notifications.enabled}
          />
          <ToggleItem
            label="12 Hours Before Launch"
            value={settings.notifications.twelveHours}
            onChange={() => handleToggle('notifications', 'twelveHours')}
            disabled={!settings.notifications.enabled}
          />
          <ToggleItem
            label="1 Hour Before Launch"
            value={settings.notifications.oneHour}
            onChange={() => handleToggle('notifications', 'oneHour')}
            disabled={!settings.notifications.enabled}
          />
          <ToggleItem
            label="30 Minutes Before Launch"
            value={settings.notifications.thirtyMinutes}
            onChange={() => handleToggle('notifications', 'thirtyMinutes')}
            disabled={!settings.notifications.enabled}
          />
          <ToggleItem
            label="10 Minutes Before Launch"
            value={settings.notifications.tenMinutes}
            onChange={() => handleToggle('notifications', 'tenMinutes')}
            disabled={!settings.notifications.enabled}
          />
        </div>
      </section>

      {/* For You Settings */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">For You Feed</h2>

        <div className="space-y-3">
          <ToggleItem
            label="Show Past Launches"
            value={settings.forYou.showPastLaunches}
            onChange={() => handleToggle('forYou', 'showPastLaunches')}
          />
          <ToggleItem
            label="Show Past Events"
            value={settings.forYou.showPastEvents}
            onChange={() => handleToggle('forYou', 'showPastEvents')}
          />
        </div>
      </section>

      {/* Developer Mode */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">Developer</h2>

        <ToggleItem
          label="Developer Mode"
          value={settings.developerMode}
          onChange={() => handleToggle('developerMode', 'developerMode')}
        />
      </section>

      {/* Favorites */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">Favorites</h2>
        <p className="text-muted">
          You have {favorites.length} favorite launch{favorites.length !== 1 ? 'es' : ''}
        </p>
      </section>

      {/* Data Management */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">Data Management</h2>

        <button
          onClick={handleClearData}
          className="px-6 py-3 bg-error text-white rounded-lg hover:bg-error/80 transition-colors"
        >
          Clear All Data
        </button>
      </section>

      {/* App Info */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">About</h2>
        <p className="text-muted mb-2">Nominal - Rocket Launch Tracker</p>
        <p className="text-muted text-sm mb-2">Web Version 1.0.0</p>
        <p className="text-muted text-sm">
          Built with Next.js and The Space Devs API
        </p>
      </section>
    </div>
  );
}

function ToggleItem({
  label,
  value,
  onChange,
  disabled = false,
}: {
  label: string;
  value: boolean;
  onChange: () => void;
  disabled?: boolean;
}) {
  return (
    <div className="flex items-center justify-between py-2">
      <span className={`text-foreground ${disabled ? 'opacity-50' : ''}`}>{label}</span>
      <button
        onClick={onChange}
        disabled={disabled}
        className={`relative w-12 h-6 rounded-full transition-colors ${
          value ? 'bg-accent' : 'bg-muted'
        } ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}`}
      >
        <div
          className={`absolute top-1 left-1 w-4 h-4 bg-white rounded-full transition-transform ${
            value ? 'translate-x-6' : 'translate-x-0'
          }`}
        />
      </button>
    </div>
  );
}
