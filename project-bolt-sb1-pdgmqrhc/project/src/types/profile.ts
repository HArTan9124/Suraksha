export interface Profile {
  name: string;
  role: string;
  university: string;
  year: number;
  expertise: string[];
  skills: string[];
  achievements: {
    title: string;
    description: string;
  }[];
  certifications: {
    name: string;
    issuer: string;
    date: string;
  }[];
  social: {
    linkedin: string;
    github: string;
    email: string;
  };
  images: string[];
}