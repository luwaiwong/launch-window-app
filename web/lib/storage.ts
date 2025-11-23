// Local storage utilities for user settings and favorites

import { UserSettings } from './types';
import { DEFAULT_SETTINGS } from './config';

const STORAGE_KEYS = {
  SETTINGS: 'nominal_settings',
  FAVORITES: 'nominal_favorites',
} as const;

export class Storage {
  // Check if we're running in the browser
  private isBrowser(): boolean {
    return typeof window !== 'undefined';
  }

  // Get user settings
  getSettings(): UserSettings {
    if (!this.isBrowser()) {
      return DEFAULT_SETTINGS;
    }

    try {
      const stored = localStorage.getItem(STORAGE_KEYS.SETTINGS);
      if (!stored) {
        return DEFAULT_SETTINGS;
      }
      return { ...DEFAULT_SETTINGS, ...JSON.parse(stored) };
    } catch (error) {
      console.error('Error reading settings from localStorage:', error);
      return DEFAULT_SETTINGS;
    }
  }

  // Save user settings
  saveSettings(settings: Partial<UserSettings>): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      const current = this.getSettings();
      const updated = { ...current, ...settings };
      localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(updated));
    } catch (error) {
      console.error('Error saving settings to localStorage:', error);
    }
  }

  // Get favorites
  getFavorites(): string[] {
    if (!this.isBrowser()) {
      return [];
    }

    try {
      const stored = localStorage.getItem(STORAGE_KEYS.FAVORITES);
      if (!stored) {
        return [];
      }
      return JSON.parse(stored);
    } catch (error) {
      console.error('Error reading favorites from localStorage:', error);
      return [];
    }
  }

  // Add to favorites
  addFavorite(launchId: string): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      const favorites = this.getFavorites();
      if (!favorites.includes(launchId)) {
        favorites.push(launchId);
        localStorage.setItem(STORAGE_KEYS.FAVORITES, JSON.stringify(favorites));
      }
    } catch (error) {
      console.error('Error adding favorite:', error);
    }
  }

  // Remove from favorites
  removeFavorite(launchId: string): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      const favorites = this.getFavorites();
      const updated = favorites.filter(id => id !== launchId);
      localStorage.setItem(STORAGE_KEYS.FAVORITES, JSON.stringify(updated));
    } catch (error) {
      console.error('Error removing favorite:', error);
    }
  }

  // Toggle favorite
  toggleFavorite(launchId: string): boolean {
    const favorites = this.getFavorites();
    const isFavorite = favorites.includes(launchId);

    if (isFavorite) {
      this.removeFavorite(launchId);
    } else {
      this.addFavorite(launchId);
    }

    return !isFavorite;
  }

  // Check if launch is favorited
  isFavorite(launchId: string): boolean {
    return this.getFavorites().includes(launchId);
  }

  // Clear all data
  clearAll(): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      localStorage.removeItem(STORAGE_KEYS.SETTINGS);
      localStorage.removeItem(STORAGE_KEYS.FAVORITES);
    } catch (error) {
      console.error('Error clearing localStorage:', error);
    }
  }
}

// Singleton instance
export const storage = new Storage();
