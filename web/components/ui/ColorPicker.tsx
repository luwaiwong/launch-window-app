'use client';

import { useState } from 'react';

interface ColorPickerProps {
  label: string;
  color: string;
  onChange: (color: string) => void;
  description?: string;
}

export function ColorPicker({ label, color, onChange, description }: ColorPickerProps) {
  const [isPickerOpen, setIsPickerOpen] = useState(false);
  const [inputValue, setInputValue] = useState(color);

  const handleColorChange = (newColor: string) => {
    setInputValue(newColor);
    onChange(newColor);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setInputValue(value);

    // Only call onChange if it's a valid hex color
    if (/^#[0-9A-F]{6}$/i.test(value)) {
      onChange(value);
    }
  };

  return (
    <div className="space-y-2">
      <div className="flex items-center justify-between">
        <div>
          <label className="text-foreground font-medium">{label}</label>
          {description && (
            <p className="text-sm text-muted">{description}</p>
          )}
        </div>

        <div className="flex items-center gap-3">
          {/* Color preview and picker button */}
          <button
            onClick={() => setIsPickerOpen(!isPickerOpen)}
            className="relative w-12 h-12 rounded-lg border-2 border-muted/20 overflow-hidden cursor-pointer hover:border-accent transition-colors"
            style={{ backgroundColor: color }}
            title="Click to pick color"
          >
            <input
              type="color"
              value={color}
              onChange={(e) => handleColorChange(e.target.value)}
              className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
            />
          </button>

          {/* Hex input */}
          <input
            type="text"
            value={inputValue}
            onChange={handleInputChange}
            placeholder="#000000"
            className="w-24 px-3 py-2 bg-background border border-muted/20 rounded text-foreground font-mono text-sm focus:outline-none focus:border-accent transition-colors"
            maxLength={7}
          />
        </div>
      </div>
    </div>
  );
}

interface ColorPresetProps {
  colors: { name: string; value: string }[];
  onSelect: (color: string) => void;
}

export function ColorPresets({ colors, onSelect }: ColorPresetProps) {
  return (
    <div className="flex flex-wrap gap-2">
      {colors.map((preset) => (
        <button
          key={preset.name}
          onClick={() => onSelect(preset.value)}
          className="w-10 h-10 rounded-lg border-2 border-muted/20 hover:border-accent transition-colors"
          style={{ backgroundColor: preset.value }}
          title={preset.name}
        />
      ))}
    </div>
  );
}
