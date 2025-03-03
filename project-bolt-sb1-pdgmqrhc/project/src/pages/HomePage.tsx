import React, { useState } from 'react';
import { FilterSection } from '../components/home/FilterSection';
import { CarList } from '../components/home/CarList';
import { FilterOptions, CarBrand } from '../types/car';

export function HomePage() {
  const [filters, setFilters] = useState<FilterOptions>({
    priceRange: [5, 50],
    mileage: 15,
    fuelType: [],
    category: []
  });
  const [selectedBrand, setSelectedBrand] = useState<CarBrand | null>(null);

  const handleSearch = () => {
    // TODO: Implement search logic
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="grid md:grid-cols-[320px,1fr] gap-8">
        <FilterSection
          filters={filters}
          onFilterChange={setFilters}
          onBrandChange={setSelectedBrand}
          onSearch={handleSearch}
        />
        <CarList filters={filters} selectedBrand={selectedBrand} />
      </div>
    </div>
  );
}