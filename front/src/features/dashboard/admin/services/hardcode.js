export const stats = {
  totalUsers: 1247,
  totalOwners: 892,
  totalSitters: 355,
  totalBookings: 3456,
  totalRevenue: 89750,
  activeBookings: 124,
  pendingIncidents: 8,
  averageRating: 4.7
};

export const recentUsers = [
  {
    id: '1',
    name: 'María García',
    email: 'maria@email.com',
    role: 'sitter',
    status: 'active',
    joinDate: '2024-01-15',
    lastActive: '2 horas ago',
    totalBookings: 45,
    rating: 4.9,
    earnings: 1250
  },
  {
    id: '2',
    name: 'Carlos López',
    email: 'carlos@email.com', 
    role: 'owner',
    status: 'active',
    joinDate: '2024-01-12',
    lastActive: '1 día ago',
    totalBookings: 12
  },
  {
    id: '3',
    name: 'Ana Ruiz',
    email: 'ana@email.com',
    role: 'sitter',
    status: 'pending',
    joinDate: '2024-01-20',
    lastActive: '5 minutos ago',
    totalBookings: 0
  },
  {
    id: '4',
    name: 'Luis Martínez',
    email: 'luis@email.com',
    role: 'owner',
    status: 'suspended',
    joinDate: '2024-01-08',
    lastActive: '3 días ago',
    totalBookings: 8
  }
];

export const pendingIncidents = [
  {
    id: '1',
    title: 'Altercado con otro perro',
    pet: 'Rocky',
    sitter: 'Juan López',
    severity: 'high',
    date: '2024-01-13'
  },
  {
    id: '2',
    title: 'Comportamiento ansioso',
    pet: 'Luna',
    sitter: 'María Rodríguez',
    severity: 'low',
    date: '2024-01-14'
  }
];

export function getStatusColor(status) {
  switch (status) {
    case 'active': return 'bg-green-100 text-green-800';
    case 'suspended': return 'bg-red-100 text-red-800';
    case 'pending': return 'bg-yellow-100 text-yellow-800';
    default: return 'bg-gray-100 text-gray-800';
  }
}

export function getSeverityColor(severity) {
  switch (severity) {
    case 'high': return 'bg-red-100 text-red-800';
    case 'medium': return 'bg-yellow-100 text-yellow-800';
    case 'low': return 'bg-green-100 text-green-800';
    default: return 'bg-gray-100 text-gray-800';
  }
}

export function handleUserAction(userId, action) {
  console.log(`Action ${action} for user ${userId}`);
}
