'use client';

import React, { createContext, useContext, useEffect, useState } from 'react';
import { ThemeColors } from '@/lib/types';
import { storage } from '@/lib/storage';
import { THEME } from '@/lib/config';

interface ThemeContextType {
  colors: ThemeColors;
  updateColors: (colors: Partial<ThemeColors>) => void;
  resetColors: () => void;
  useCustomTheme: boolean;
  setUseCustomTheme: (use: boolean) => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [colors, setColors] = useState<ThemeColors>(THEME.colors);
  const [useCustomTheme, setUseCustomTheme] = useState(false);

  useEffect(() => {
    // Load theme from storage on mount
    const settings = storage.getSettings();
    if (settings.useCustomTheme && settings.theme) {
      setColors(settings.theme);
      setUseCustomTheme(true);
      applyThemeToDOM(settings.theme);
    } else {
      applyThemeToDOM(THEME.colors);
    }
  }, []);

  const applyThemeToDOM = (themeColors: ThemeColors) => {
    const root = document.documentElement;
    root.style.setProperty('--background', themeColors.background);
    root.style.setProperty('--highlight', themeColors.highlight);
    root.style.setProperty('--foreground', themeColors.foreground);
    root.style.setProperty('--accent', themeColors.accent);
    root.style.setProperty('--success', themeColors.success);
    root.style.setProperty('--warning', themeColors.warning);
    root.style.setProperty('--error', themeColors.error);
    root.style.setProperty('--muted', themeColors.muted);
  };

  const updateColors = (newColors: Partial<ThemeColors>) => {
    const updatedColors = { ...colors, ...newColors };
    setColors(updatedColors);
    applyThemeToDOM(updatedColors);

    // Save to storage
    const settings = storage.getSettings();
    storage.saveSettings({
      ...settings,
      theme: updatedColors,
      useCustomTheme: true,
    });
    setUseCustomTheme(true);
  };

  const resetColors = () => {
    setColors(THEME.colors);
    applyThemeToDOM(THEME.colors);
    setUseCustomTheme(false);

    // Save to storage
    const settings = storage.getSettings();
    storage.saveSettings({
      ...settings,
      theme: THEME.colors,
      useCustomTheme: false,
    });
  };

  const handleUseCustomTheme = (use: boolean) => {
    setUseCustomTheme(use);
    const themeToApply = use ? colors : THEME.colors;
    applyThemeToDOM(themeToApply);

    // Save to storage
    const settings = storage.getSettings();
    storage.saveSettings({
      ...settings,
      useCustomTheme: use,
    });
  };

  return (
    <ThemeContext.Provider
      value={{
        colors,
        updateColors,
        resetColors,
        useCustomTheme,
        setUseCustomTheme: handleUseCustomTheme,
      }}
    >
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }
  return context;
}
