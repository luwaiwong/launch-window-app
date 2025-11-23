'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { apiClient } from '@/lib/api/client';
import { Event } from '@/lib/types';
import { formatDate, getRelativeTime } from '@/lib/utils';
import Image from 'next/image';
import Link from 'next/link';

export default function EventDetail() {
  const params = useParams();
  const router = useRouter();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadEvent = async () => {
      try {
        const id = params.id as string;
        const data = await apiClient.getEventById(id);
        setEvent(data);
      } catch (error) {
        console.error('Error loading event:', error);
      } finally {
        setLoading(false);
      }
    };

    loadEvent();
  }, [params.id]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">📅</div>
          <p className="text-muted">Loading event details...</p>
        </div>
      </div>
    );
  }

  if (!event) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">❌</div>
          <p className="text-muted mb-4">Event not found</p>
          <button
            onClick={() => router.push('/news')}
            className="px-6 py-3 bg-accent text-white rounded-lg hover:bg-accent/80 transition-colors"
          >
            Back to News
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6 max-w-4xl">
      {/* Back Button */}
      <button
        onClick={() => router.back()}
        className="mb-6 text-accent hover:text-accent/80 flex items-center gap-2"
      >
        ← Back
      </button>

      {/* Hero Image */}
      {event.feature_image && (
        <div className="relative w-full h-96 rounded-lg overflow-hidden mb-6">
          <Image
            src={event.feature_image}
            alt={event.name}
            fill
            className="object-cover"
            priority
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 80vw, 60vw"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent" />
        </div>
      )}

      {/* Event Header */}
      <div className="mb-8">
        <div className="flex items-center gap-2 mb-3">
          <span className="text-sm bg-accent/20 text-accent px-3 py-1 rounded">
            {event.type.name}
          </span>
          <span className="text-sm text-muted">{getRelativeTime(event.date)}</span>
        </div>

        <h1 className="text-3xl font-bold text-foreground mb-2">{event.name}</h1>
        <p className="text-muted">{formatDate(event.date)}</p>
      </div>

      {/* Event Description */}
      {event.description && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">About</h2>
          <p className="text-muted leading-relaxed">{event.description}</p>
        </section>
      )}

      {/* Location */}
      {event.location && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">Location</h2>
          <p className="text-muted">📍 {event.location}</p>
        </section>
      )}

      {/* Related Launches */}
      {event.launches && event.launches.length > 0 && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">Related Launches</h2>
          <div className="space-y-2">
            {event.launches.map((launch) => (
              <Link
                key={launch.id}
                href={`/launches/${launch.id}`}
                className="block p-3 bg-background rounded hover:bg-muted/10 transition-colors"
              >
                <p className="text-foreground">{launch.name}</p>
                {launch.net && (
                  <p className="text-sm text-muted">{formatDate(launch.net)}</p>
                )}
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Links */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-3">Links</h2>
        <div className="space-y-2">
          {event.news_url && (
            <a
              href={event.news_url}
              target="_blank"
              rel="noopener noreferrer"
              className="block text-accent hover:text-accent/80"
            >
              Read News Article →
            </a>
          )}
          {event.video_url && (
            <a
              href={event.video_url}
              target="_blank"
              rel="noopener noreferrer"
              className="block text-accent hover:text-accent/80"
            >
              Watch Video →
            </a>
          )}
        </div>
      </section>
    </div>
  );
}
