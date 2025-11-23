'use client';

import { useEffect, useState } from 'react';
import { storage } from '@/lib/storage';
import { UserSettings } from '@/lib/types';
import { useTheme } from '@/components/ui/ThemeProvider';
import { ColorPicker, ColorPresets } from '@/components/ui/ColorPicker';
import { NotificationPermission } from '@/components/ui/NotificationPermission';
import { THEME } from '@/lib/config';

export default function Settings() {
  const [settings, setSettings] = useState<UserSettings | null>(null);
  const [favorites, setFavorites] = useState<string[]>([]);
  const { colors, updateColors, resetColors, useCustomTheme, setUseCustomTheme } = useTheme();

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

      {/* Theme Customization */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-foreground">Theme Customization</h2>
          <div className="flex items-center gap-3">
            <ToggleItem
              label="Use Custom Theme"
              value={useCustomTheme}
              onChange={() => setUseCustomTheme(!useCustomTheme)}
            />
          </div>
        </div>

        <p className="text-sm text-muted mb-6">
          Customize the app's color scheme to your liking. Changes apply instantly across all pages.
        </p>

        <div className="space-y-6">
          <ColorPicker
            label="Background"
            description="Main background color"
            color={colors.background}
            onChange={(color) => updateColors({ background: color })}
          />

          <ColorPicker
            label="Highlight"
            description="Card and section backgrounds"
            color={colors.highlight}
            onChange={(color) => updateColors({ highlight: color })}
          />

          <ColorPicker
            label="Foreground"
            description="Primary text color"
            color={colors.foreground}
            onChange={(color) => updateColors({ foreground: color })}
          />

          <ColorPicker
            label="Accent"
            description="Primary action and highlight color"
            color={colors.accent}
            onChange={(color) => updateColors({ accent: color })}
          />

          <ColorPicker
            label="Success"
            description="Success states and indicators"
            color={colors.success}
            onChange={(color) => updateColors({ success: color })}
          />

          <ColorPicker
            label="Warning"
            description="Warning states and indicators"
            color={colors.warning}
            onChange={(color) => updateColors({ warning: color })}
          />

          <ColorPicker
            label="Error"
            description="Error states and indicators"
            color={colors.error}
            onChange={(color) => updateColors({ error: color })}
          />

          <ColorPicker
            label="Muted"
            description="Secondary text and subtle elements"
            color={colors.muted}
            onChange={(color) => updateColors({ muted: color })}
          />
        </div>

        {/* Color Presets */}
        <div className="mt-6 pt-6 border-t border-muted/20">
          <h3 className="text-sm font-semibold text-foreground mb-3">Quick Presets</h3>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            <button
              onClick={() => {
                updateColors(THEME.colors);
                setUseCustomTheme(true);
              }}
              className="px-4 py-2 bg-background border border-muted/20 rounded-lg hover:border-accent transition-colors text-sm"
            >
              Default Dark
            </button>
            <button
              onClick={() => {
                updateColors({
                  background: '#0a0e27',
                  highlight: '#1a1f3a',
                  foreground: '#e2e8f0',
                  accent: '#818cf8',
                  success: '#34d399',
                  warning: '#fbbf24',
                  error: '#f87171',
                  muted: '#64748b',
                });
                setUseCustomTheme(true);
              }}
              className="px-4 py-2 bg-background border border-muted/20 rounded-lg hover:border-accent transition-colors text-sm"
            >
              Midnight Blue
            </button>
            <button
              onClick={() => {
                updateColors({
                  background: '#18181b',
                  highlight: '#27272a',
                  foreground: '#fafafa',
                  accent: '#a855f7',
                  success: '#4ade80',
                  warning: '#facc15',
                  error: '#ef4444',
                  muted: '#71717a',
                });
                setUseCustomTheme(true);
              }}
              className="px-4 py-2 bg-background border border-muted/20 rounded-lg hover:border-accent transition-colors text-sm"
            >
              Purple Haze
            </button>
            <button
              onClick={() => {
                updateColors({
                  background: '#1c1917',
                  highlight: '#292524',
                  foreground: '#fafaf9',
                  accent: '#f97316',
                  success: '#22c55e',
                  warning: '#eab308',
                  error: '#dc2626',
                  muted: '#78716c',
                });
                setUseCustomTheme(true);
              }}
              className="px-4 py-2 bg-background border border-muted/20 rounded-lg hover:border-accent transition-colors text-sm"
            >
              Warm Earth
            </button>
          </div>
        </div>

        {/* Reset Button */}
        <div className="mt-6 pt-6 border-t border-muted/20">
          <button
            onClick={() => {
              resetColors();
              if (confirm('Theme reset to default. Refresh the page to see all changes.')) {
                window.location.reload();
              }
            }}
            className="px-6 py-3 bg-muted/20 text-foreground rounded-lg hover:bg-muted/30 transition-colors"
          >
            Reset to Default Theme
          </button>
        </div>
      </section>

      {/* Notification Settings */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-4">Notifications</h2>

        {/* Notification Permission Status */}
        <div className="mb-6">
          <NotificationPermission />
        </div>

        <p className="text-sm text-muted mb-4">
          Configure when you want to receive launch notifications. Make sure notifications are enabled above.
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
