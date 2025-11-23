'use client';

import { useState, useEffect } from 'react';
import {
  isNotificationSupported,
  getNotificationPermission,
  requestNotificationPermission,
  showNotification,
} from '@/lib/notifications';

export function NotificationPermission() {
  const [permission, setPermission] = useState<NotificationPermission>('default');
  const [isSupported, setIsSupported] = useState(false);

  useEffect(() => {
    setIsSupported(isNotificationSupported());
    setPermission(getNotificationPermission());
  }, []);

  const handleRequestPermission = async () => {
    const newPermission = await requestNotificationPermission();
    setPermission(newPermission);

    if (newPermission === 'granted') {
      // Show a test notification
      showNotification('🚀 Notifications Enabled!', {
        body: 'You will now receive launch alerts based on your preferences.',
      });
    }
  };

  const handleTestNotification = () => {
    if (permission === 'granted') {
      showNotification('🧪 Test Notification', {
        body: 'This is a test notification. Launch alerts will look like this!',
      });
    }
  };

  if (!isSupported) {
    return (
      <div className="bg-warning/20 border border-warning/40 rounded-lg p-4">
        <p className="text-sm text-warning">
          ⚠️ Your browser doesn't support notifications.
        </p>
      </div>
    );
  }

  if (permission === 'denied') {
    return (
      <div className="bg-error/20 border border-error/40 rounded-lg p-4">
        <p className="text-sm text-error mb-2">
          ❌ Notifications are blocked. To enable:
        </p>
        <ol className="text-sm text-error/80 list-decimal list-inside space-y-1">
          <li>Click the lock/info icon in your browser's address bar</li>
          <li>Find "Notifications" and select "Allow"</li>
          <li>Refresh this page</li>
        </ol>
      </div>
    );
  }

  if (permission === 'granted') {
    return (
      <div className="bg-success/20 border border-success/40 rounded-lg p-4">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-success mb-1">
              ✅ Notifications are enabled
            </p>
            <p className="text-xs text-success/80">
              You'll receive alerts based on your preferences below
            </p>
          </div>
          <button
            onClick={handleTestNotification}
            className="px-4 py-2 bg-success/20 text-success rounded-lg hover:bg-success/30 transition-colors text-sm"
          >
            Test Notification
          </button>
        </div>
      </div>
    );
  }

  // Default state - permission not requested
  return (
    <div className="bg-accent/20 border border-accent/40 rounded-lg p-4">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-accent mb-1">
            🔔 Enable notifications to get launch alerts
          </p>
          <p className="text-xs text-accent/80">
            Get notified before launches based on your preferences
          </p>
        </div>
        <button
          onClick={handleRequestPermission}
          className="px-4 py-2 bg-accent text-white rounded-lg hover:bg-accent/80 transition-colors text-sm whitespace-nowrap"
        >
          Enable Notifications
        </button>
      </div>
    </div>
  );
}
