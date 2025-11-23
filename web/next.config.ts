import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    // Allow images from any source (required for dynamic news article images)
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
      },
      {
        protocol: 'http',
        hostname: '**',
      },
    ],
  },
};

export default nextConfig;
