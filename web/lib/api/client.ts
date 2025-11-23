// Client-side API client
// This file is used by the frontend to call the Next.js API routes

import { ApiResponse, Launch, Event, Article } from '../types';

const API_BASE = '/api';

export class ApiClient {
  // Launch endpoints
  async getUpcomingLaunches(limit: number = 10, offset: number = 0): Promise<ApiResponse<Launch>> {
    const response = await fetch(`${API_BASE}/launches/upcoming?limit=${limit}&offset=${offset}`);
    if (!response.ok) {
      throw new Error('Failed to fetch upcoming launches');
    }
    return response.json();
  }

  async getPreviousLaunches(limit: number = 10, offset: number = 0): Promise<ApiResponse<Launch>> {
    const response = await fetch(`${API_BASE}/launches/previous?limit=${limit}&offset=${offset}`);
    if (!response.ok) {
      throw new Error('Failed to fetch previous launches');
    }
    return response.json();
  }

  async getLaunchById(id: string): Promise<Launch> {
    const response = await fetch(`${API_BASE}/launches/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch launch details');
    }
    return response.json();
  }

  // Event endpoints
  async getUpcomingEvents(limit: number = 10, offset: number = 0): Promise<ApiResponse<Event>> {
    const response = await fetch(`${API_BASE}/events/upcoming?limit=${limit}&offset=${offset}`);
    if (!response.ok) {
      throw new Error('Failed to fetch upcoming events');
    }
    return response.json();
  }

  async getPreviousEvents(limit: number = 10, offset: number = 0): Promise<ApiResponse<Event>> {
    const response = await fetch(`${API_BASE}/events/previous?limit=${limit}&offset=${offset}`);
    if (!response.ok) {
      throw new Error('Failed to fetch previous events');
    }
    return response.json();
  }

  async getEventById(id: string): Promise<Event> {
    const response = await fetch(`${API_BASE}/events/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch event details');
    }
    return response.json();
  }

  // News endpoints
  async getArticles(limit: number = 10, offset: number = 0): Promise<ApiResponse<Article>> {
    const response = await fetch(`${API_BASE}/news?limit=${limit}&offset=${offset}`);
    if (!response.ok) {
      throw new Error('Failed to fetch articles');
    }
    return response.json();
  }

  // Combined feed for "For You" page
  async getFeedItems(limit: number = 10): Promise<Array<Launch | Event>> {
    const [launches, events] = await Promise.all([
      this.getUpcomingLaunches(limit),
      this.getUpcomingEvents(limit),
    ]);

    // Combine and sort by date
    const combined = [
      ...launches.results.map(l => ({ ...l, itemType: 'launch' as const })),
      ...events.results.map(e => ({ ...e, itemType: 'event' as const })),
    ];

    combined.sort((a, b) => {
      const dateA = new Date('net' in a ? a.net : a.date).getTime();
      const dateB = new Date('net' in b ? b.net : b.date).getTime();
      return dateA - dateB;
    });

    return combined.slice(0, limit);
  }
}

// Singleton instance
export const apiClient = new ApiClient();
