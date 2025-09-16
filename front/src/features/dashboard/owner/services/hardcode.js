export const pets = [
  { id: 1, name: 'Luna', type: 'Perro', breed: 'Golden Retriever', age: '3 años', avatar: '🐕' },
  { id: 2, name: 'Milo', type: 'Gato', breed: 'Siamés', age: '2 años', avatar: '🐱' },
];

export const upcomingServices = [
  {
    id: 1,
    service: 'Paseo Matutino',
    pet: 'Luna',
    caregiver: 'María García',
    time: 'Hoy 9:00 AM',
    status: 'Programado',
    price: '$25'
  },
  {
    id: 2,
    service: 'Sesión de Grooming',
    pet: 'Milo',
    caregiver: 'Carlos López',
    time: 'Mañana 2:00 PM',
    status: 'Confirmado',
    price: '$45'
  },
];

export const recentActivity = [
  {
    id: 1,
    activity: 'Paseo completado',
    pet: 'Luna',
    time: 'Hace 2 horas',
    caregiver: 'Ana Ruiz',
    rating: 5
  },
  {
    id: 2,
    activity: 'Visita veterinaria',
    pet: 'Milo',
    time: 'Ayer',
    caregiver: 'Dr. Fernández',
    rating: 5
  },
];

export const ownerStats = {
  petsCount: pets.length,
  activeServices: 2,
  pendingIncidents: 2,
  rating: 4.9
};
