// Simple in-memory cache for API responses
// In production, consider using Redis or similar

interface CacheEntry<T> {
  data: T;
  timestamp: number;
}

class ApiCache {
  private cache: Map<string, CacheEntry<any>>;
  private cacheDuration: number;

  constructor(cacheDuration: number = 1000 * 60 * 25) {
    this.cache = new Map();
    this.cacheDuration = cacheDuration;
  }

  get<T>(key: string): T | null {
    const entry = this.cache.get(key);

    if (!entry) {
      return null;
    }

    const now = Date.now();
    const isExpired = now - entry.timestamp > this.cacheDuration;

    if (isExpired) {
      this.cache.delete(key);
      return null;
    }

    return entry.data as T;
  }

  set<T>(key: string, data: T): void {
    this.cache.set(key, {
      data,
      timestamp: Date.now(),
    });
  }

  clear(): void {
    this.cache.clear();
  }

  delete(key: string): void {
    this.cache.delete(key);
  }

  // Clear expired entries
  cleanup(): void {
    const now = Date.now();
    for (const [key, entry] of this.cache.entries()) {
      if (now - entry.timestamp > this.cacheDuration) {
        this.cache.delete(key);
      }
    }
  }
}

// Singleton instance
export const apiCache = new ApiCache();

// Run cleanup every 30 minutes
if (typeof window === 'undefined') {
  // Only run cleanup on server
  setInterval(() => {
    apiCache.cleanup();
  }, 1000 * 60 * 30);
}
