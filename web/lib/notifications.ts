// Notification utilities for launch alerts

import { Launch } from './types';
import { storage } from './storage';

export interface ScheduledNotification {
  id: string;
  launchId: string;
  launchName: string;
  launchTime: string;
  notificationTime: string;
  type: 'twentyFourHours' | 'twelveHours' | 'oneHour' | 'thirtyMinutes' | 'tenMinutes';
}

// Check if browser supports notifications
export function isNotificationSupported(): boolean {
  return 'Notification' in window;
}

// Check current notification permission
export function getNotificationPermission(): NotificationPermission {
  if (!isNotificationSupported()) {
    return 'denied';
  }
  return Notification.permission;
}

// Request notification permission
export async function requestNotificationPermission(): Promise<NotificationPermission> {
  if (!isNotificationSupported()) {
    return 'denied';
  }

  const permission = await Notification.requestPermission();
  return permission;
}

// Show a notification
export function showNotification(title: string, options?: NotificationOptions): void {
  if (!isNotificationSupported() || Notification.permission !== 'granted') {
    return;
  }

  new Notification(title, {
    icon: '/icon-192x192.png',
    badge: '/icon-192x192.png',
    ...options,
  });
}

// Calculate notification times for a launch
export function calculateNotificationTimes(launchTime: string): {
  twentyFourHours: Date;
  twelveHours: Date;
  oneHour: Date;
  thirtyMinutes: Date;
  tenMinutes: Date;
} {
  const launchDate = new Date(launchTime);

  return {
    twentyFourHours: new Date(launchDate.getTime() - 24 * 60 * 60 * 1000),
    twelveHours: new Date(launchDate.getTime() - 12 * 60 * 60 * 1000),
    oneHour: new Date(launchDate.getTime() - 60 * 60 * 1000),
    thirtyMinutes: new Date(launchDate.getTime() - 30 * 60 * 1000),
    tenMinutes: new Date(launchDate.getTime() - 10 * 60 * 1000),
  };
}

// Schedule notifications for a launch
export function scheduleNotificationsForLaunch(launch: Launch): ScheduledNotification[] {
  const settings = storage.getSettings();

  if (!settings.notifications.enabled) {
    return [];
  }

  const notificationTimes = calculateNotificationTimes(launch.net);
  const scheduled: ScheduledNotification[] = [];

  if (settings.notifications.twentyFourHours) {
    scheduled.push({
      id: `${launch.id}-24h`,
      launchId: launch.id,
      launchName: launch.name,
      launchTime: launch.net,
      notificationTime: notificationTimes.twentyFourHours.toISOString(),
      type: 'twentyFourHours',
    });
  }

  if (settings.notifications.twelveHours) {
    scheduled.push({
      id: `${launch.id}-12h`,
      launchId: launch.id,
      launchName: launch.name,
      launchTime: launch.net,
      notificationTime: notificationTimes.twelveHours.toISOString(),
      type: 'twelveHours',
    });
  }

  if (settings.notifications.oneHour) {
    scheduled.push({
      id: `${launch.id}-1h`,
      launchId: launch.id,
      launchName: launch.name,
      launchTime: launch.net,
      notificationTime: notificationTimes.oneHour.toISOString(),
      type: 'oneHour',
    });
  }

  if (settings.notifications.thirtyMinutes) {
    scheduled.push({
      id: `${launch.id}-30m`,
      launchId: launch.id,
      launchName: launch.name,
      launchTime: launch.net,
      notificationTime: notificationTimes.thirtyMinutes.toISOString(),
      type: 'thirtyMinutes',
    });
  }

  if (settings.notifications.tenMinutes) {
    scheduled.push({
      id: `${launch.id}-10m`,
      launchId: launch.id,
      launchName: launch.name,
      launchTime: launch.net,
      notificationTime: notificationTimes.tenMinutes.toISOString(),
      type: 'tenMinutes',
    });
  }

  return scheduled;
}

// Get human-readable time until launch
export function getTimeUntilText(type: ScheduledNotification['type']): string {
  switch (type) {
    case 'twentyFourHours':
      return '24 hours';
    case 'twelveHours':
      return '12 hours';
    case 'oneHour':
      return '1 hour';
    case 'thirtyMinutes':
      return '30 minutes';
    case 'tenMinutes':
      return '10 minutes';
  }
}

// Send a launch notification
export function sendLaunchNotification(notification: ScheduledNotification): void {
  const timeUntil = getTimeUntilText(notification.type);

  showNotification(`🚀 Launch Alert: ${notification.launchName}`, {
    body: `Launching in ${timeUntil}!`,
    tag: notification.id,
    requireInteraction: false,
    data: {
      launchId: notification.launchId,
      url: `/launches/${notification.launchId}`,
    },
  });
}

// Store scheduled notifications in localStorage
const STORAGE_KEY = 'nominal_scheduled_notifications';

export function saveScheduledNotifications(notifications: ScheduledNotification[]): void {
  if (typeof window === 'undefined') return;
  localStorage.setItem(STORAGE_KEY, JSON.stringify(notifications));
}

export function getScheduledNotifications(): ScheduledNotification[] {
  if (typeof window === 'undefined') return [];

  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (!stored) return [];
    return JSON.parse(stored);
  } catch (error) {
    console.error('Error reading scheduled notifications:', error);
    return [];
  }
}

export function clearScheduledNotifications(): void {
  if (typeof window === 'undefined') return;
  localStorage.removeItem(STORAGE_KEY);
}
