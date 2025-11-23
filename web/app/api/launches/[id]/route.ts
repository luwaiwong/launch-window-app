import { NextRequest, NextResponse } from 'next/server';
import { API_CONFIG } from '@/lib/config';
import { apiCache } from '@/lib/api/cache';
import { Launch } from '@/lib/types';

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const cacheKey = `launch_${id}`;

    // Check cache first
    const cached = apiCache.get<Launch>(cacheKey);
    if (cached) {
      return NextResponse.json(cached);
    }

    // Fetch from The Space Devs API
    const url = `${API_CONFIG.SPACE_DEVS.BASE_URL}/launch/${id}/`;
    const response = await fetch(url);

    if (!response.ok) {
      if (response.status === 404) {
        return NextResponse.json(
          { error: 'Launch not found' },
          { status: 404 }
        );
      }
      throw new Error(`API responded with status: ${response.status}`);
    }

    const data: Launch = await response.json();

    // Cache the response
    apiCache.set(cacheKey, data);

    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching launch details:', error);
    return NextResponse.json(
      { error: 'Failed to fetch launch details' },
      { status: 500 }
    );
  }
}
