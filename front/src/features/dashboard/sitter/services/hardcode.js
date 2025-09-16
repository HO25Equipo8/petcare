export const initialSchedule = [
  { day: 'Lunes', enabled: true, startTime: '08:00', endTime: '18:00' },
  { day: 'Martes', enabled: true, startTime: '08:00', endTime: '18:00' },
  { day: 'Miércoles', enabled: true, startTime: '08:00', endTime: '18:00' },
  { day: 'Jueves', enabled: true, startTime: '08:00', endTime: '18:00' },
  { day: 'Viernes', enabled: true, startTime: '08:00', endTime: '18:00' },
  { day: 'Sábado', enabled: false, startTime: '09:00', endTime: '15:00' },
  { day: 'Domingo', enabled: false, startTime: '09:00', endTime: '15:00' },
];

export const initialServiceZones = [
  {
    id: '1',
    name: 'Centro de la Ciudad',
    radius: 5,
    centerAddress: 'Plaza Principal, Centro',
    active: true
  },
  {
    id: '2', 
    name: 'Zona Norte',
    radius: 3,
    centerAddress: 'Av. Norte 1234',
    active: true
  }
];

export const initialNewZone = { name: '', radius: 5, centerAddress: '' };

export const upcomingBookings = [
  {
    id: '1',
    service: 'Paseo Matutino',
    pet: 'Luna',
    owner: 'Juan Pérez',
    time: 'Hoy 9:00 AM',
    duration: '1 hora',
    payment: '$25',
    status: 'confirmado',
    address: 'Calle Principal 123'
  },
  {
    id: '2',
    service: 'Cuidado en Casa',
    pet: 'Max',
    owner: 'Ana García',
    time: 'Mañana 2:00 PM',
    duration: '3 horas',
    payment: '$75',
    status: 'pendiente',
    address: 'Av. Central 456'
  }
];

export const stats = {
  totalBookings: 24,
  thisWeekBookings: 6,
  earnings: 480,
  rating: 4.9,
  completedServices: 18
};
