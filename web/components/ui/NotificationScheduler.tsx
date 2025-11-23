'use client';

import { useEffect, useRef } from 'react';
import { apiClient } from '@/lib/api/client';
import {
  scheduleNotificationsForLaunch,
  getScheduledNotifications,
  saveScheduledNotifications,
  sendLaunchNotification,
  getNotificationPermission,
  ScheduledNotification,
} from '@/lib/notifications';
import { storage } from '@/lib/storage';

export function NotificationScheduler() {
  const checkIntervalRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    // Only run if notifications are enabled and permission is granted
    const settings = storage.getSettings();
    if (!settings.notifications.enabled || getNotificationPermission() !== 'granted') {
      return;
    }

    // Initial setup: fetch upcoming launches and schedule notifications
    const setupNotifications = async () => {
      try {
        const upcomingLaunches = await apiClient.getUpcomingLaunches(20);

        // Get existing scheduled notifications
        const existingNotifications = getScheduledNotifications();
        const existingLaunchIds = new Set(existingNotifications.map(n => n.launchId));

        // Schedule notifications for new launches
        const newNotifications: ScheduledNotification[] = [];

        for (const launch of upcomingLaunches.results) {
          // Only schedule if not already scheduled
          if (!existingLaunchIds.has(launch.id)) {
            const launchNotifications = scheduleNotificationsForLaunch(launch);
            newNotifications.push(...launchNotifications);
          }
        }

        // Combine with existing and save
        const allNotifications = [...existingNotifications, ...newNotifications];
        saveScheduledNotifications(allNotifications);
      } catch (error) {
        console.error('Error setting up notifications:', error);
      }
    };

    // Check notifications every minute
    const checkNotifications = () => {
      const now = new Date();
      const scheduled = getScheduledNotifications();
      const remaining: ScheduledNotification[] = [];
      const toSend: ScheduledNotification[] = [];

      for (const notification of scheduled) {
        const notificationTime = new Date(notification.notificationTime);

        // Check if it's time to send this notification (within 1 minute window)
        const timeDiff = notificationTime.getTime() - now.getTime();
        if (timeDiff <= 60000 && timeDiff >= 0) {
          toSend.push(notification);
        } else if (timeDiff > 0) {
          // Keep future notifications
          remaining.push(notification);
        }
        // Discard past notifications
      }

      // Send notifications
      toSend.forEach(notification => {
        sendLaunchNotification(notification);
      });

      // Update stored notifications
      if (toSend.length > 0) {
        saveScheduledNotifications(remaining);
      }
    };

    // Initial setup
    setupNotifications();

    // Check every minute
    checkIntervalRef.current = setInterval(() => {
      checkNotifications();
    }, 60000); // Check every minute

    // Also check immediately
    checkNotifications();

    // Cleanup
    return () => {
      if (checkIntervalRef.current) {
        clearInterval(checkIntervalRef.current);
      }
    };
  }, []);

  // This component doesn't render anything
  return null;
}
