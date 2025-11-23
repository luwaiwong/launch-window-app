'use client';

import { useEffect, useState } from 'react';
import { calculateTMinus } from '@/lib/utils';

interface TMinusProps {
  targetDate: string;
  className?: string;
  showLabel?: boolean;
}

export function TMinus({ targetDate, className = '', showLabel = false }: TMinusProps) {
  const [countdown, setCountdown] = useState(calculateTMinus(targetDate));

  useEffect(() => {
    // Update countdown every second
    const interval = setInterval(() => {
      setCountdown(calculateTMinus(targetDate));
    }, 1000);

    return () => clearInterval(interval);
  }, [targetDate]);

  return (
    <div className={`font-mono ${className}`}>
      {showLabel && (
        <span className="text-sm text-muted mr-2">
          {countdown.isNegative ? 'T+' : 'T-'}
        </span>
      )}
      <span className="font-semibold">{countdown.formatted}</span>
    </div>
  );
}

interface TMinusCompactProps {
  targetDate: string;
  className?: string;
}

export function TMinusCompact({ targetDate, className = '' }: TMinusCompactProps) {
  const [countdown, setCountdown] = useState(calculateTMinus(targetDate));

  useEffect(() => {
    const interval = setInterval(() => {
      setCountdown(calculateTMinus(targetDate));
    }, 1000);

    return () => clearInterval(interval);
  }, [targetDate]);

  // Show compact format: "2d 4h" or "4h 23m" or "23m 15s"
  let display = '';
  if (countdown.days > 0) {
    display = `${countdown.days}d ${countdown.hours}h`;
  } else if (countdown.hours > 0) {
    display = `${countdown.hours}h ${countdown.minutes}m`;
  } else {
    display = `${countdown.minutes}m ${countdown.seconds}s`;
  }

  return (
    <div className={`text-sm font-mono ${className}`}>
      {countdown.isNegative ? '+' : '-'}{display}
    </div>
  );
}
