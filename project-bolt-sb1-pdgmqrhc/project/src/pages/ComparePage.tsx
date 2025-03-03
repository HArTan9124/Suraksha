import React, { useState } from 'react';
import { ComparisonTable } from '../components/compare/ComparisonTable';
import { CarSelector } from '../components/compare/CarSelector';
import { Car } from '../types/car';

export function ComparePage() {
  const [selectedCars, setSelectedCars] = useState<Car[]>([]);

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Compare Cars</h1>
      <CarSelector 
        onSelect={(car) => setSelectedCars([...selectedCars, car])}
        onRemove={(carId) => setSelectedCars(selectedCars.filter(car => car.id !== carId))}
        selectedCars={selectedCars}
      />
      <ComparisonTable cars={selectedCars} />
    </div>
  );
}