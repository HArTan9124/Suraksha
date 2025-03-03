import React from 'react';
import { Slider } from '../ui/Slider';
import { Select } from '../ui/Select';
import { Button } from '../ui/Button';
import { FilterOptions, CarBrand } from '../../types/car';

interface FilterSectionProps {
  filters: FilterOptions;
  onFilterChange: (filters: FilterOptions) => void;
  onBrandChange: (brand: CarBrand) => void;
  onSearch: () => void;
}

const BRANDS: CarBrand[] = ['Maruti Suzuki', 'Tata', 'Hyundai', 'Mahindra'];
const FUEL_TYPES = ['Petrol', 'Diesel', 'EV', 'CNG'];
const CATEGORIES = ['SUV', 'Sedan', 'Hatchback'];

export function FilterSection({ filters, onFilterChange, onBrandChange, onSearch }: FilterSectionProps) {
  return (
    <div className="bg-white rounded-lg shadow-md p-6 space-y-6">
      <Select
        label="Brand"
        options={BRANDS}
        onChange={(value) => onBrandChange(value as CarBrand)}
      />
      
      <Slider
        label="Price Range (₹L)"
        min={5}
        max={50}
        step={1}
        value={filters.priceRange}
        onChange={(value) => onFilterChange({ ...filters, priceRange: value as [number, number] })}
      />
      
      <Slider
        label="Minimum Mileage (km/l)"
        min={10}
        max={30}
        value={filters.mileage}
        onChange={(value) => onFilterChange({ ...filters, mileage: value as number })}
      />
      
      <Select
        label="Fuel Type"
        options={FUEL_TYPES}
        multiple
        value={filters.fuelType}
        onChange={(value) => onFilterChange({ ...filters, fuelType: value as FilterOptions['fuelType'] })}
      />
      
      <Select
        label="Category"
        options={CATEGORIES}
        multiple
        value={filters.category}
        onChange={(value) => onFilterChange({ ...filters, category: value as FilterOptions['category'] })}
      />
      
      <Button onClick={onSearch} className="w-full">
        Find My Match
      </Button>
    </div>
  );
}