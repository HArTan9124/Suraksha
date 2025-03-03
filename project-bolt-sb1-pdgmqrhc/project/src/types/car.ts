export interface Car {
  id: string;
  brand: string;
  model: string;
  variant: string;
  price: {
    exShowroom: number;
    onRoad: number;
  };
  specs: {
    engine: string;
    power: string;
    torque: string;
    transmission: string;
    fuelType: 'Petrol' | 'Diesel' | 'EV' | 'CNG';
    mileage: number;
  };
  category: 'SUV' | 'Sedan' | 'Hatchback';
  features: string[];
  safety: string[];
  rating: number;
  imageUrl: string;
}

export type CarBrand = 'Maruti Suzuki' | 'Tata' | 'Hyundai' | 'Mahindra';

export interface FilterOptions {
  priceRange: [number, number];
  mileage: number;
  fuelType: Car['specs']['fuelType'][];
  category: Car['category'][];
}