import { NextRequest, NextResponse } from 'next/server';
import { API_CONFIG } from '@/lib/config';
import { apiCache } from '@/lib/api/cache';
import { Event } from '@/lib/types';

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const cacheKey = `event_${id}`;

    // Check cache first
    const cached = apiCache.get<Event>(cacheKey);
    if (cached) {
      return NextResponse.json(cached);
    }

    // Fetch from The Space Devs API
    const url = `${API_CONFIG.SPACE_DEVS.BASE_URL}/event/${id}/`;
    const response = await fetch(url);

    if (!response.ok) {
      if (response.status === 404) {
        return NextResponse.json(
          { error: 'Event not found' },
          { status: 404 }
        );
      }
      throw new Error(`API responded with status: ${response.status}`);
    }

    const data: Event = await response.json();

    // Cache the response
    apiCache.set(cacheKey, data);

    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching event details:', error);
    return NextResponse.json(
      { error: 'Failed to fetch event details' },
      { status: 500 }
    );
  }
}
