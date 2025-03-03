import React from 'react';
import { EMICalculator } from '../components/calculator/EMICalculator';
import { CostBreakdown } from '../components/calculator/CostBreakdown';
import { DealerLocator } from '../components/calculator/DealerLocator';

export function CalculatorPage() {
  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Price Calculator</h1>
      <div className="grid md:grid-cols-2 gap-8">
        <div className="space-y-8">
          <EMICalculator />
          <CostBreakdown />
        </div>
        <DealerLocator />
      </div>
    </div>
  );
}