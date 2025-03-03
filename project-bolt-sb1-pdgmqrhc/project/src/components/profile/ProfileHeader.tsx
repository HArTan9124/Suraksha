import React from 'react';
import { Github, Linkedin, Instagram } from 'lucide-react';
import { Profile } from '../../types/profile';

interface ProfileHeaderProps {
  profile: Profile;
}

export function ProfileHeader({ profile }: ProfileHeaderProps) {
  return (
    <div className="relative">
      <div className="bg-gradient-to-r from-blue-600 to-indigo-600 h-48 rounded-t-lg"></div>
      <div className="absolute -bottom-16 left-8">
        <div className="relative">
          <img
            src={profile.images[0]}
            alt={profile.name}
            className="w-32 h-32 rounded-full border-4 border-white shadow-lg"
          />
        </div>
      </div>
      <div className="bg-white rounded-b-lg shadow-md pt-20 pb-8 px-8">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-2xl font-bold">{profile.name}</h1>
            <p className="text-gray-600">{profile.role}</p>
            <p className="text-sm text-gray-500">
              {profile.year}nd Year Student at {profile.university}
            </p>
          </div>
          <div className="flex space-x-4">
            <a href={profile.social.github} target="_blank" rel="noopener noreferrer">
              <Github className="w-6 h-6 text-gray-600 hover:text-blue-600" />
            </a>
            <a href={profile.social.linkedin} target="_blank" rel="noopener noreferrer">
              <Linkedin className="w-6 h-6 text-gray-600 hover:text-blue-600" />
            </a>
            <a href={profile.social.instagram} target="_blank" rel="noopener noreferrer">
              <Instagram className="w-6 h-6 text-gray-600 hover:text-blue-600" />
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}