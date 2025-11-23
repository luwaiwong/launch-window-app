'use client';

import { Event } from '@/lib/types';
import { formatDate, getRelativeTime } from '@/lib/utils';
import Image from 'next/image';
import Link from 'next/link';

interface EventCardProps {
  event: Event;
  variant?: 'default' | 'compact';
}

export function EventCard({ event, variant = 'default' }: EventCardProps) {
  if (variant === 'compact') {
    return <CompactEventCard event={event} />;
  }

  return <DefaultEventCard event={event} />;
}

function DefaultEventCard({ event }: { event: Event }) {
  return (
    <Link href={`/events/${event.id}`} className="block">
      <div className="bg-highlight rounded-lg overflow-hidden hover:bg-opacity-80 transition-all cursor-pointer">
        {event.feature_image && (
          <div className="relative w-full h-48">
            <Image
              src={event.feature_image}
              alt={event.name}
              fill
              className="object-cover"
              sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
            />
          </div>
        )}

        <div className="p-4">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-xs bg-accent/20 text-accent px-2 py-1 rounded">
              {event.type.name}
            </span>
            <span className="text-xs text-muted">
              {getRelativeTime(event.date)}
            </span>
          </div>

          <h3 className="font-semibold text-foreground mb-2">{event.name}</h3>

          {event.description && (
            <p className="text-sm text-muted line-clamp-2">{event.description}</p>
          )}

          {event.location && (
            <p className="text-xs text-muted mt-2">
              📍 {event.location}
            </p>
          )}

          {event.launches && event.launches.length > 0 && (
            <p className="text-xs text-accent mt-2">
              {event.launches.length} related launch{event.launches.length !== 1 ? 'es' : ''}
            </p>
          )}
        </div>
      </div>
    </Link>
  );
}

function CompactEventCard({ event }: { event: Event }) {
  return (
    <Link href={`/events/${event.id}`} className="block">
      <div className="bg-highlight rounded-lg p-3 hover:bg-opacity-80 transition-all cursor-pointer">
        <div className="flex justify-between items-start gap-3">
          <div className="flex-1 min-w-0">
            <div className="flex items-center gap-2 mb-1">
              <span className="text-xs bg-accent/20 text-accent px-1.5 py-0.5 rounded text-[10px]">
                {event.type.name}
              </span>
              <span className="text-xs text-muted">
                {getRelativeTime(event.date)}
              </span>
            </div>

            <h4 className="font-medium text-sm text-foreground line-clamp-2">{event.name}</h4>

            {event.location && (
              <p className="text-xs text-muted mt-1">📍 {event.location}</p>
            )}
          </div>

          {event.feature_image && (
            <div className="relative w-16 h-16 flex-shrink-0 rounded overflow-hidden">
              <Image
                src={event.feature_image}
                alt={event.name}
                fill
                className="object-cover"
                sizes="64px"
              />
            </div>
          )}
        </div>
      </div>
    </Link>
  );
}
