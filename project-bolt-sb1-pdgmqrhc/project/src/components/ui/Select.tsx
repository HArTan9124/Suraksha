import React from 'react';
import { Listbox } from '@headlessui/react';
import { ChevronDown } from 'lucide-react';

interface SelectProps {
  label: string;
  options: string[];
  value?: string | string[];
  multiple?: boolean;
  onChange: (value: string | string[]) => void;
}

export function Select({ label, options, value, multiple, onChange }: SelectProps) {
  return (
    <Listbox value={value} onChange={onChange} multiple={multiple}>
      <div className="relative">
        <Listbox.Label className="block text-sm font-medium text-gray-700 mb-1">
          {label}
        </Listbox.Label>
        <Listbox.Button className="relative w-full py-2 pl-3 pr-10 text-left bg-white border border-gray-300 rounded-lg cursor-default focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500">
          <span className="block truncate">
            {multiple 
              ? (value as string[])?.join(', ') || 'Select options'
              : value || 'Select an option'}
          </span>
          <span className="absolute inset-y-0 right-0 flex items-center pr-2">
            <ChevronDown className="h-5 w-5 text-gray-400" />
          </span>
        </Listbox.Button>
        <Listbox.Options className="absolute z-10 w-full py-1 mt-1 overflow-auto bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none">
          {options.map((option) => (
            <Listbox.Option
              key={option}
              value={option}
              className={({ active }) =>
                `${active ? 'text-white bg-blue-600' : 'text-gray-900'}
                cursor-default select-none relative py-2 pl-3 pr-9`
              }
            >
              {option}
            </Listbox.Option>
          ))}
        </Listbox.Options>
      </div>
    </Listbox>
  );
}