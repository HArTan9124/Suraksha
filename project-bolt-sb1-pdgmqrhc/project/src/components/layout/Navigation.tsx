import React from 'react';
import { Link } from 'react-router-dom';
import { Car, Calculator, Users, UserCircle } from 'lucide-react';

export function Navigation() {
  return (
    <nav className="bg-white shadow-lg fixed w-full top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <Link to="/" className="flex items-center space-x-2">
            <Car className="h-8 w-8 text-blue-600" />
            <span className="text-xl font-bold text-gray-900">Car Compare</span>
          </Link>
          
          <div className="flex items-center space-x-6">
            <Link to="/" className="nav-link">
              <Car className="h-5 w-5" />
              <span>Health Check</span>
            </Link>
            <Link to="/compare" className="nav-link">
              <Users className="h-5 w-5" />
              <span>Compare</span>
            </Link>
            <Link to="/calculator" className="nav-link">
              <Calculator className="h-5 w-5" />
              <span>Price Calculator</span>
            </Link>
            <Link to="/profile" className="nav-link">
              <UserCircle className="h-5 w-5" />
              <span>Profile</span>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
}