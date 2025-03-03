import React from 'react';

interface SliderProps {
  label: string;
  min: number;
  max: number;
  step?: number;
  value: number | [number, number];
  onChange: (value: number | [number, number]) => void;
}

export function Slider({ label, min, max, step = 1, value, onChange }: SliderProps) {
  const isRange = Array.isArray(value);
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = parseFloat(e.target.value);
    if (isRange) {
      const [start, end] = value as [number, number];
      const index = parseInt(e.target.dataset.index || '0');
      onChange(index === 0 ? [newValue, end] : [start, newValue]);
    } else {
      onChange(newValue);
    }
  };

  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-gray-700">
        {label}
      </label>
      <div className="relative pt-1">
        {isRange ? (
          <div className="relative">
            <input
              type="range"
              min={min}
              max={max}
              step={step}
              value={(value as [number, number])[0]}
              data-index="0"
              onChange={handleChange}
              className="absolute w-full"
            />
            <input
              type="range"
              min={min}
              max={max}
              step={step}
              value={(value as [number, number])[1]}
              data-index="1"
              onChange={handleChange}
              className="absolute w-full"
            />
          </div>
        ) : (
          <input
            type="range"
            min={min}
            max={max}
            step={step}
            value={value as number}
            onChange={handleChange}
            className="w-full"
          />
        )}
        <div className="flex justify-between text-xs text-gray-600">
          <span>₹{min}L</span>
          <span>₹{max}L</span>
        </div>
      </div>
    </div>
  );
}