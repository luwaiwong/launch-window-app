'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { apiClient } from '@/lib/api/client';
import { Launch } from '@/lib/types';
import { TMinus } from '@/components/ui/TMinus';
import { formatDate, getStatusColor } from '@/lib/utils';
import Image from 'next/image';

export default function LaunchDetail() {
  const params = useParams();
  const router = useRouter();
  const [launch, setLaunch] = useState<Launch | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadLaunch = async () => {
      try {
        const id = params.id as string;
        const data = await apiClient.getLaunchById(id);
        setLaunch(data);
      } catch (error) {
        console.error('Error loading launch:', error);
      } finally {
        setLoading(false);
      }
    };

    loadLaunch();
  }, [params.id]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">🚀</div>
          <p className="text-muted">Loading launch details...</p>
        </div>
      </div>
    );
  }

  if (!launch) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="text-4xl mb-4">❌</div>
          <p className="text-muted mb-4">Launch not found</p>
          <button
            onClick={() => router.push('/launches')}
            className="px-6 py-3 bg-accent text-white rounded-lg hover:bg-accent/80 transition-colors"
          >
            Back to Launches
          </button>
        </div>
      </div>
    );
  }

  const statusColor = getStatusColor(launch.status.name);

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
      {launch.image && (
        <div className="relative w-full h-96 rounded-lg overflow-hidden mb-6">
          <Image
            src={launch.image}
            alt={launch.name}
            fill
            className="object-cover"
            priority
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 80vw, 60vw"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent" />
        </div>
      )}

      {/* Launch Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">{launch.name}</h1>
        <div className="flex items-center gap-4 text-sm text-muted mb-4">
          <span>{launch.launch_service_provider?.name}</span>
          <span>•</span>
          <span>{formatDate(launch.net)}</span>
        </div>

        <div className="flex items-center gap-4">
          <span className={`text-sm font-medium ${statusColor}`}>
            {launch.status.name}
          </span>
          {launch.net && (
            <TMinus targetDate={launch.net} className="text-accent text-lg" showLabel />
          )}
        </div>
      </div>

      {/* Mission Description */}
      {launch.mission?.description && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">Mission</h2>
          <p className="text-muted leading-relaxed">{launch.mission.description}</p>
        </section>
      )}

      {/* Rocket Information */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-3">Rocket</h2>
        <p className="text-foreground">{launch.rocket?.configuration?.full_name}</p>
      </section>

      {/* Launch Location */}
      <section className="mb-8 bg-highlight rounded-lg p-6">
        <h2 className="text-xl font-semibold text-foreground mb-3">Location</h2>
        <div className="space-y-2">
          <p className="text-foreground">{launch.pad?.name}</p>
          <p className="text-muted">{launch.pad?.location?.name}</p>

          {launch.pad?.description && (
            <p className="text-sm text-muted mt-3">{launch.pad.description}</p>
          )}

          {launch.pad?.wiki_url && (
            <a
              href={launch.pad.wiki_url}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-block mt-3 text-accent hover:text-accent/80"
            >
              View on Wikipedia →
            </a>
          )}
        </div>
      </section>

      {/* Mission Details */}
      {launch.mission && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">Mission Details</h2>
          <div className="grid grid-cols-2 gap-4">
            {launch.mission.type && (
              <div>
                <p className="text-sm text-muted">Type</p>
                <p className="text-foreground">{launch.mission.type}</p>
              </div>
            )}
            {launch.mission.orbit?.name && (
              <div>
                <p className="text-sm text-muted">Orbit</p>
                <p className="text-foreground">{launch.mission.orbit.name}</p>
              </div>
            )}
          </div>
        </section>
      )}

      {/* Agencies */}
      {launch.mission?.agencies && launch.mission.agencies.length > 0 && (
        <section className="mb-8 bg-highlight rounded-lg p-6">
          <h2 className="text-xl font-semibold text-foreground mb-3">Agencies</h2>
          <div className="space-y-4">
            {launch.mission.agencies.map((agency) => (
              <div key={agency.id} className="border-l-2 border-accent pl-4">
                <h3 className="font-semibold text-foreground">{agency.name}</h3>
                {agency.type && (
                  <p className="text-sm text-muted">{agency.type}</p>
                )}
                {agency.country_code && (
                  <p className="text-sm text-muted">Country: {agency.country_code}</p>
                )}
                {agency.description && (
                  <p className="text-sm text-muted mt-2">{agency.description}</p>
                )}
              </div>
            ))}
          </div>
        </section>
      )}
    </div>
  );
}
