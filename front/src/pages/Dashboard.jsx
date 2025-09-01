import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card.jsx';
import { Button } from '../components/ui/Button.jsx';
import { Badge } from '../components/ui/Badge.jsx';
import { Heart, MapPin, Calendar, Clock, Star, Plus, AlertTriangle } from 'lucide-react';



export function Dashboard({ onNavigate }) {
  const pets = [
    { id: 1, name: 'Luna', type: 'Perro', breed: 'Golden Retriever', age: '3 años', avatar: '🐕' },
    { id: 2, name: 'Milo', type: 'Gato', breed: 'Siamés', age: '2 años', avatar: '🐱' },
  ];

  const upcomingServices = [
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

  const recentActivity = [
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

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Bienvenido, Juan</h1>
        <p className="text-gray-600">Gestiona el cuidado de tus mascotas desde aquí</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Mascotas</p>
                <p className="text-2xl">{pets.length}</p>
              </div>
              <Heart className="w-8 h-8 text-primary" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Servicios Activos</p>
                <p className="text-2xl">2</p>
              </div>
              <Clock className="w-8 h-8 text-blue-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Incidentes Pendientes</p>
                <p className="text-2xl text-orange-600">2</p>
              </div>
              <AlertTriangle className="w-8 h-8 text-orange-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Calificación</p>
                <p className="text-2xl">4.9</p>
              </div>
              <Star className="w-8 h-8 text-yellow-500" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Alert for pending incidents */}
      <div className="mb-8">
        <Card className="border-orange-200 bg-orange-50">
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <AlertTriangle className="w-5 h-5 text-orange-600" />
                <div>
                  <h3 className="font-medium text-orange-800">Incidentes Pendientes</h3>
                  <p className="text-sm text-orange-700">Tienes 2 incidentes que requieren atención</p>
                </div>
              </div>
              <Button 
                variant="outline" 
                size="sm"
                onClick={() => onNavigate('incidents')}
                className="border-orange-300 text-orange-700 hover:bg-orange-100"
              >
                Ver Incidentes
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* My Pets */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle>Mis Mascotas</CardTitle>
            <Button variant="ghost" size="sm" onClick={() => onNavigate('pets')}>
              <Plus className="w-4 h-4" />
            </Button>
          </CardHeader>
          <CardContent className="space-y-4">
            {pets.map((pet) => (
              <div key={pet.id} className="flex items-center space-x-4 p-3 rounded-lg bg-gray-50">
                <div className="text-2xl">{pet.avatar}</div>
                <div className="flex-1">
                  <h3 className="font-medium">{pet.name}</h3>
                  <p className="text-sm text-gray-600">{pet.breed} • {pet.age}</p>
                </div>
                <Badge variant="secondary">{pet.type}</Badge>
              </div>
            ))}
            <Button 
              variant="outline" 
              className="w-full" 
              onClick={() => onNavigate('pets')}
            >
              Ver Todas las Mascotas
            </Button>
          </CardContent>
        </Card>

        {/* Upcoming Services */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle>Próximos Servicios</CardTitle>
            <Button variant="ghost" size="sm" onClick={() => onNavigate('booking')}>
              <Plus className="w-4 h-4" />
            </Button>
          </CardHeader>
          <CardContent className="space-y-4">
            {upcomingServices.map((service) => (
              <div key={service.id} className="p-4 border rounded-lg">
                <div className="flex justify-between items-start mb-2">
                  <h4 className="font-medium">{service.service}</h4>
                  <Badge variant={service.status === 'Programado' ? 'default' : 'secondary'}>
                    {service.status}
                  </Badge>
                </div>
                <p className="text-sm text-gray-600 mb-1">Para: {service.pet}</p>
                <p className="text-sm text-gray-600 mb-1">Cuidador: {service.caregiver}</p>
                <div className="flex justify-between items-center">
                  <span className="text-sm text-gray-500">{service.time}</span>
                  <span className="font-medium">{service.price}</span>
                </div>
              </div>
            ))}
            <Button 
              variant="outline" 
              className="w-full"
              onClick={() => onNavigate('booking')}
            >
              Ver Todas las Reservas
            </Button>
          </CardContent>
        </Card>

        {/* Recent Activity */}
        <Card>
          <CardHeader>
            <CardTitle>Actividad Reciente</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {recentActivity.map((activity) => (
              <div key={activity.id} className="flex items-start space-x-3">
                <div className="w-2 h-2 bg-primary rounded-full mt-2"></div>
                <div className="flex-1">
                  <p className="font-medium">{activity.activity}</p>
                  <p className="text-sm text-gray-600">
                    {activity.pet} • {activity.caregiver}
                  </p>
                  <div className="flex items-center justify-between mt-1">
                    <span className="text-xs text-gray-500">{activity.time}</span>
                    <div className="flex items-center">
                      {[...Array(activity.rating)].map((_, i) => (
                        <Star key={i} className="w-3 h-3 text-yellow-400 fill-current" />
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      <div className="mt-8">
        <h2 className="text-xl mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
          <Button 
            variant="outline" 
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate('tracking')}
          >
            <MapPin className="w-6 h-6" />
            <span>Ver Tracking</span>
          </Button>
          <Button 
            variant="outline" 
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate('booking')}
          >
            <Calendar className="w-6 h-6" />
            <span>Nueva Reserva</span>
          </Button>
          <Button 
            variant="outline" 
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate('caregivers')}
          >
            <Star className="w-6 h-6" />
            <span>Buscar Cuidador</span>
          </Button>
          <Button 
            variant="outline" 
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate('pets')}
          >
            <Heart className="w-6 h-6" />
            <span>Agregar Mascota</span>
          </Button>
          <Button 
            variant="outline" 
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate('incidents')}
          >
            <AlertTriangle className="w-6 h-6" />
            <span>Gestionar Incidentes</span>
          </Button>
        </div>
      </div>
    </div>
  );
}