import React from 'react';
import { ProfileHeader } from '../components/profile/ProfileHeader';
import { SkillsSection } from '../components/profile/SkillsSection';
import { ProjectsGrid } from '../components/profile/ProjectsGrid';
import { ContactSection } from '../components/profile/ContactSection';

const profile = {
  name: "Harshit Tandon",
  role: "Developer and Designer",
  university: "Chandigarh University",
  year: 2,
  expertise: ["Android Development", "Web Development", "Blockchain"],
  skills: ["DSA", "C++", "Java"],
  social: {
    linkedin: "https://www.linkedin.com/in/hartan9124/",
    github: "https://github.com/HArTan9124",
    instagram: "https://www.instagram.com/tandon._.2895/"
  },
  images: [
    "https://example.com/profile1.jpg",
    "https://example.com/profile2.jpg"
  ]
};

export function ProfilePage() {
  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <ProfileHeader profile={profile} />
      <div className="mt-12 grid md:grid-cols-2 gap-8">
        <SkillsSection skills={profile.skills} expertise={profile.expertise} />
        <ProjectsGrid />
      </div>
      <ContactSection social={profile.social} />
    </div>
  );
}