import React from 'react';
import { Car } from '../../types/car';
import { Star, Fuel, Gauge } from 'lucide-react';

interface CarCardProps {
  car: Car;
}

export function CarCard({ car }: CarCardProps) {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
      <img 
        src={car.imageUrl} 
        alt={`${car.brand} ${car.model}`}
        className="w-full h-48 object-cover"
      />
      <div className="p-4 space-y-4">
        <div className="flex justify-between items-start">
          <div>
            <h3 className="text-lg font-semibold">{car.brand} {car.model}</h3>
            <p className="text-sm text-gray-600">{car.variant}</p>
          </div>
          <div className="flex items-center">
            <Star className="w-4 h-4 text-yellow-400 fill-current" />
            <span className="ml-1 text-sm">{car.rating}</span>
          </div>
        </div>
        
        <div className="flex justify-between text-sm">
          <div className="flex items-center">
            <Fuel className="w-4 h-4 mr-1" />
            <span>{car.specs.fuelType}</span>
          </div>
          <div className="flex items-center">
            <Gauge className="w-4 h-4 mr-1" />
            <span>{car.specs.mileage} km/l</span>
          </div>
        </div>
        
        <div className="border-t pt-4">
          <div className="flex justify-between">
            <div>
              <p className="text-xs text-gray-600">Ex-Showroom</p>
              <p className="font-semibold">₹{(car.price.exShowroom / 100000).toFixed(2)}L</p>
            </div>
            <div>
              <p className="text-xs text-gray-600">On-Road</p>
              <p className="font-semibold">₹{(car.price.onRoad / 100000).toFixed(2)}L</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}