// Utility functions

import { Launch, LaunchStatus } from './types';

/**
 * Calculate time difference and format as DD:HH:MM:SS
 */
export function calculateTMinus(targetDate: string): {
  isNegative: boolean;
  days: number;
  hours: number;
  minutes: number;
  seconds: number;
  formatted: string;
} {
  const now = new Date().getTime();
  const target = new Date(targetDate).getTime();
  const diff = target - now;

  const isNegative = diff < 0;
  const absDiff = Math.abs(diff);

  const days = Math.floor(absDiff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((absDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((absDiff % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((absDiff % (1000 * 60)) / 1000);

  const formatted = `${String(days).padStart(2, '0')}:${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

  return {
    isNegative,
    days,
    hours,
    minutes,
    seconds,
    formatted: isNegative ? `-${formatted}` : `T-${formatted}`,
  };
}

/**
 * Format a date string to a readable format
 */
export function formatDate(dateString: string, includTime: boolean = true): string {
  const date = new Date(dateString);
  const options: Intl.DateTimeFormatOptions = {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  };

  if (includTime) {
    options.hour = 'numeric';
    options.minute = '2-digit';
    options.timeZoneName = 'short';
  }

  return date.toLocaleDateString('en-US', options);
}

/**
 * Get status color based on launch status
 */
export function getStatusColor(status: string): string {
  const statusLower = status.toLowerCase();

  if (statusLower.includes('success')) {
    return 'text-success';
  }

  if (statusLower.includes('failure') || statusLower.includes('failed')) {
    return 'text-error';
  }

  if (statusLower.includes('go') || statusLower.includes('green')) {
    return 'text-success';
  }

  if (statusLower.includes('hold') || statusLower.includes('tbd')) {
    return 'text-warning';
  }

  return 'text-foreground';
}

/**
 * Truncate text to a maximum length
 */
export function truncate(text: string, maxLength: number): string {
  if (text.length <= maxLength) {
    return text;
  }
  return text.substring(0, maxLength) + '...';
}

/**
 * Check if a launch has day precision or better
 * (Filter out month-precision launches)
 */
export function hasDayPrecision(launch: Launch): boolean {
  const precision = launch.net_precision?.name.toLowerCase();
  return precision !== 'month' && precision !== undefined;
}

/**
 * Get relative time (e.g., "2 hours ago", "in 3 days")
 */
export function getRelativeTime(dateString: string): string {
  const now = new Date().getTime();
  const target = new Date(dateString).getTime();
  const diff = target - now;

  const absDiff = Math.abs(diff);
  const isPast = diff < 0;

  const minutes = Math.floor(absDiff / (1000 * 60));
  const hours = Math.floor(absDiff / (1000 * 60 * 60));
  const days = Math.floor(absDiff / (1000 * 60 * 60 * 24));

  if (minutes < 60) {
    return isPast ? `${minutes}m ago` : `in ${minutes}m`;
  }

  if (hours < 24) {
    return isPast ? `${hours}h ago` : `in ${hours}h`;
  }

  return isPast ? `${days}d ago` : `in ${days}d`;
}

/**
 * Class name utility for conditional classes
 */
export function cn(...classes: (string | undefined | null | false)[]): string {
  return classes.filter(Boolean).join(' ');
}
