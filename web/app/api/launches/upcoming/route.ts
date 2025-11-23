import { NextRequest, NextResponse } from 'next/server';
import { API_CONFIG } from '@/lib/config';
import { apiCache } from '@/lib/api/cache';
import { ApiResponse, Launch } from '@/lib/types';

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const limit = searchParams.get('limit') || '10';
    const offset = searchParams.get('offset') || '0';

    const cacheKey = `launches_upcoming_${limit}_${offset}`;

    // Check cache first
    const cached = apiCache.get<ApiResponse<Launch>>(cacheKey);
    if (cached) {
      return NextResponse.json(cached);
    }

    // Fetch from The Space Devs API
    const url = `${API_CONFIG.SPACE_DEVS.BASE_URL}/launch/upcoming/?limit=${limit}&offset=${offset}`;
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`API responded with status: ${response.status}`);
    }

    const data: ApiResponse<Launch> = await response.json();

    // Cache the response
    apiCache.set(cacheKey, data);

    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching upcoming launches:', error);
    return NextResponse.json(
      { error: 'Failed to fetch upcoming launches' },
      { status: 500 }
    );
  }
}
