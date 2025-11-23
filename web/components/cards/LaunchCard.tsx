'use client';

import { Launch } from '@/lib/types';
import { TMinus, TMinusCompact } from '../ui/TMinus';
import { formatDate, getStatusColor } from '@/lib/utils';
import Image from 'next/image';
import Link from 'next/link';

interface LaunchCardProps {
  launch: Launch;
  variant?: 'default' | 'compact' | 'highlight';
  onFavoriteToggle?: (launchId: string) => void;
  isFavorite?: boolean;
}

export function LaunchCard({
  launch,
  variant = 'default',
  onFavoriteToggle,
  isFavorite = false,
}: LaunchCardProps) {
  if (variant === 'highlight') {
    return <HighlightLaunchCard launch={launch} />;
  }

  if (variant === 'compact') {
    return <CompactLaunchCard launch={launch} isFavorite={isFavorite} onFavoriteToggle={onFavoriteToggle} />;
  }

  return <DefaultLaunchCard launch={launch} isFavorite={isFavorite} onFavoriteToggle={onFavoriteToggle} />;
}

function DefaultLaunchCard({
  launch,
  isFavorite,
  onFavoriteToggle,
}: {
  launch: Launch;
  isFavorite: boolean;
  onFavoriteToggle?: (launchId: string) => void;
}) {
  const statusColor = getStatusColor(launch.status.name);

  return (
    <Link href={`/launches/${launch.id}`} className="block">
      <div className="bg-highlight rounded-lg p-4 hover:bg-opacity-80 transition-all cursor-pointer">
        <div className="flex gap-4">
          {launch.image && (
            <div className="relative w-24 h-24 flex-shrink-0 rounded overflow-hidden">
              <Image
                src={launch.image}
                alt={launch.name}
                fill
                className="object-cover"
                sizes="96px"
              />
            </div>
          )}

          <div className="flex-1 min-w-0">
            <h3 className="font-semibold text-foreground truncate">{launch.name}</h3>

            <p className="text-sm text-muted mt-1">
              {launch.launch_service_provider?.name}
            </p>

            <p className="text-sm text-muted">
              {launch.pad?.location?.name}
            </p>

            <div className="flex items-center justify-between mt-2">
              <span className={`text-xs font-medium ${statusColor}`}>
                {launch.status.name}
              </span>

              {launch.net && (
                <TMinusCompact targetDate={launch.net} className="text-accent" />
              )}
            </div>
          </div>

          {onFavoriteToggle && (
            <button
              onClick={(e) => {
                e.preventDefault();
                e.stopPropagation();
                onFavoriteToggle(launch.id);
              }}
              className="flex-shrink-0 w-8 h-8 flex items-center justify-center text-2xl hover:scale-110 transition-transform"
            >
              {isFavorite ? '★' : '☆'}
            </button>
          )}
        </div>
      </div>
    </Link>
  );
}

function CompactLaunchCard({
  launch,
  isFavorite,
  onFavoriteToggle,
}: {
  launch: Launch;
  isFavorite: boolean;
  onFavoriteToggle?: (launchId: string) => void;
}) {
  return (
    <Link href={`/launches/${launch.id}`} className="block">
      <div className="bg-highlight rounded-lg p-3 hover:bg-opacity-80 transition-all cursor-pointer">
        <div className="flex justify-between items-start">
          <div className="flex-1 min-w-0">
            <h4 className="font-medium text-sm text-foreground truncate">{launch.name}</h4>
            <p className="text-xs text-muted mt-0.5">{launch.launch_service_provider?.name}</p>
          </div>

          {launch.net && (
            <TMinusCompact targetDate={launch.net} className="text-accent text-xs ml-2" />
          )}
        </div>
      </div>
    </Link>
  );
}

function HighlightLaunchCard({ launch }: { launch: Launch }) {
  return (
    <Link href={`/launches/${launch.id}`} className="block">
      <div className="relative rounded-lg overflow-hidden h-96 cursor-pointer group">
        {launch.image && (
          <Image
            src={launch.image}
            alt={launch.name}
            fill
            className="object-cover"
            priority
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
          />
        )}

        {/* Gradient overlay */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/50 to-transparent" />

        {/* Content */}
        <div className="absolute bottom-0 left-0 right-0 p-6">
          <p className="text-xs text-accent mb-2">NEXT LAUNCH</p>
          <h2 className="text-2xl font-bold text-white mb-2">{launch.name}</h2>

          <div className="flex items-center gap-4 text-sm text-gray-300 mb-4">
            <span>{launch.launch_service_provider?.name}</span>
            <span>•</span>
            <span>{launch.pad?.location?.name}</span>
          </div>

          {launch.net && (
            <div className="bg-black/50 backdrop-blur-sm rounded-lg p-4 inline-block">
              <TMinus targetDate={launch.net} className="text-3xl text-accent" />
            </div>
          )}

          {launch.mission?.description && (
            <p className="text-sm text-gray-300 mt-4 line-clamp-2">
              {launch.mission.description}
            </p>
          )}
        </div>
      </div>
    </Link>
  );
}
