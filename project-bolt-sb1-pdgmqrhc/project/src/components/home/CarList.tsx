import React from 'react';
import { CarCard } from './CarCard';
import { FilterOptions, CarBrand } from '../../types/car';

interface CarListProps {
  filters: FilterOptions;
  selectedBrand: CarBrand | null;
}

export function CarList({ filters, selectedBrand }: CarListProps) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {/* Placeholder cards for demonstration */}
      <CarCard
        car={{
          id: '1',
          brand: 'Tata',
          model: 'Nexon',
          variant: 'XZ+',
          price: { exShowroom: 1200000, onRoad: 1400000 },
          specs: {
            engine: '1.2L Turbo',
            power: '120 PS',
            torque: '170 Nm',
            transmission: '6-Speed MT',
            fuelType: 'Petrol',
            mileage: 17.5
          },
          category: 'SUV',
          features: ['Sunroof', 'Connected Car'],
          safety: ['6 Airbags', 'ESP'],
          rating: 4.5,
          imageUrl: 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800'
        }}
      />
    </div>
  );
}