import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      // Space Devs API images
      {
        protocol: 'https',
        hostname: 'thespacedevs-prod.nyc3.digitaloceanspaces.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com',
        pathname: '/**',
      },
      // Common image hosts
      {
        protocol: 'https',
        hostname: 'i.imgur.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: '*.spaceflightnewsapi.net',
        pathname: '/**',
      },
      // Space news websites (for article images) - HTTPS
      {
        protocol: 'https',
        hostname: 'spacepolicyonline.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'spacenews.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'arstechnica.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'cdn.arstechnica.net',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'www.nasaspaceflight.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'spaceflightnow.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'www.space.com',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'cdn.mos.cms.futurecdn.net',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'www.planetary.org',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'europeanspaceflight.com',
        pathname: '/**',
      },
      // Space news websites - HTTP (for legacy/non-HTTPS sources)
      {
        protocol: 'http',
        hostname: 'spaceflightnow.com',
        pathname: '/**',
      },
      {
        protocol: 'http',
        hostname: 'spacepolicyonline.com',
        pathname: '/**',
      },
      {
        protocol: 'http',
        hostname: 'spacenews.com',
        pathname: '/**',
      },
    ],
  },
};

export default nextConfig;
