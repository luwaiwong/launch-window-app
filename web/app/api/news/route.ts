import { NextRequest, NextResponse } from 'next/server';
import { API_CONFIG } from '@/lib/config';
import { apiCache } from '@/lib/api/cache';
import { ApiResponse, Article } from '@/lib/types';

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const limit = searchParams.get('limit') || '10';
    const offset = searchParams.get('offset') || '0';

    const cacheKey = `news_${limit}_${offset}`;

    // Check cache first
    const cached = apiCache.get<ApiResponse<Article>>(cacheKey);
    if (cached) {
      return NextResponse.json(cached);
    }

    // Fetch from Spaceflight News API
    const url = `${API_CONFIG.SPACEFLIGHT_NEWS.BASE_URL}/articles/?limit=${limit}&offset=${offset}`;
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`API responded with status: ${response.status}`);
    }

    const data: ApiResponse<Article> = await response.json();

    // Cache the response
    apiCache.set(cacheKey, data);

    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching news articles:', error);
    return NextResponse.json(
      { error: 'Failed to fetch news articles' },
      { status: 500 }
    );
  }
}
