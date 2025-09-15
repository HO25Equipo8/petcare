import { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '../../shared/ui/Card';
import { Button } from '../../shared/ui/Button';
import { Badge } from '../../shared/ui/Badge';
import { Avatar, AvatarFallback } from '../../shared/ui/Avatar';
import { MapPin, Navigation, Phone, MessageCircle, Camera, Play, Pause } from 'lucide-react';

export function LiveTracking({ onNavigate }) {
  const [isTracking, setIsTracking] = useState(true);
  const [currentTime, setCurrentTime] = useState(new Date());

  // Simular actualizaciones en tiempo real
  useEffect(() => {
    if (isTracking) {
      const interval = setInterval(() => {
        setCurrentTime(new Date());
      }, 1000);
      return () => clearInterval(interval);
    }
  }, [isTracking]);

  const activeWalk = {
    id: 1,
    pet: { name: 'Luna', breed: 'Golden Retriever', avatar: '🐕' },
    caregiver: { name: 'María García', phone: '+1 (555) 123-4567', rating: 4.9 },
    startTime: '9:00 AM',
    estimatedDuration: '45 min',
    currentLocation: 'Parque Central',
    route: [
      { lat: 40.7829, lng: -73.9654, time: '9:00 AM', activity: 'Inicio del paseo' },
      { lat: 40.7831, lng: -73.9652, time: '9:15 AM', activity: 'Jugando en el parque' },
      { lat: 40.7835, lng: -73.9648, time: '9:30 AM', activity: 'Caminando por el sendero' },
    ],
    status: 'En progreso',
    distance: '1.2 km',
    photos: ['📸', '📸', '📸']
  };

  const recentUpdates = [
    { time: '9:32 AM', message: 'Luna está disfrutando mucho del parque', type: 'update' },
    { time: '9:25 AM', message: 'Nueva foto subida', type: 'photo' },
    { time: '9:15 AM', message: 'Llegamos al parque central', type: 'location' },
    { time: '9:00 AM', message: 'Paseo iniciado', type: 'start' },
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Seguimiento en Tiempo Real</h1>
        <p className="text-gray-600">Monitorea el paseo de tus mascotas</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Map and Tracking */}
        <div className="lg:col-span-2 space-y-6">
          {/* Current Walk Status */}
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="flex items-center space-x-2">
                  <div className="text-2xl">{activeWalk.pet.avatar}</div>
                  <div>
                    <h3>Paseo de {activeWalk.pet.name}</h3>
                    <p className="text-sm text-gray-600">{activeWalk.pet.breed}</p>
                  </div>
                </CardTitle>
                <Badge variant={isTracking ? 'default' : 'secondary'} className="flex items-center space-x-1">
                  <div className={`w-2 h-2 rounded-full ${isTracking ? 'bg-green-500' : 'bg-gray-500'}`}></div>
                  <span>{activeWalk.status}</span>
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                <div className="text-center">
                  <p className="text-sm text-gray-600">Inicio</p>
                  <p className="font-semibold">{activeWalk.startTime}</p>
                </div>
                <div className="text-center">
                  <p className="text-sm text-gray-600">Duración</p>
                  <p className="font-semibold">{currentTime.toLocaleTimeString()}</p>
                </div>
                <div className="text-center">
                  <p className="text-sm text-gray-600">Distancia</p>
                  <p className="font-semibold">{activeWalk.distance}</p>
                </div>
                <div className="text-center">
                  <p className="text-sm text-gray-600">Ubicación</p>
                  <p className="font-semibold">{activeWalk.currentLocation}</p>
                </div>
              </div>

              {/* Simulated Map */}
              <div className="bg-gradient-to-br from-green-100 to-blue-100 rounded-lg h-64 flex items-center justify-center relative overflow-hidden">
                <div className="absolute inset-0 opacity-20">
                  <div className="grid grid-cols-8 grid-rows-8 h-full">
                    {[...Array(64)].map((_, i) => (
                      <div key={i} className="border border-gray-300"></div>
                    ))}
                  </div>
                </div>
                
                <div className="z-10 text-center">
                  <MapPin className="w-16 h-16 text-red-500 mx-auto mb-2 animate-bounce" />
                  <p className="font-semibold">Ubicación Actual de Luna</p>
                  <p className="text-sm text-gray-600">{activeWalk.currentLocation}</p>
                </div>

                {/* Route points */}
                <div className="absolute top-4 left-4 w-3 h-3 bg-green-500 rounded-full"></div>
                <div className="absolute top-8 left-12 w-3 h-3 bg-blue-500 rounded-full"></div>
                <div className="absolute top-16 left-20 w-3 h-3 bg-yellow-500 rounded-full"></div>
                
                {/* Route line */}
                <svg className="absolute inset-0 w-full h-full">
                  <path
                    d="M 20 20 Q 60 40 80 80 T 160 120"
                    stroke="#3B82F6"
                    strokeWidth="3"
                    fill="none"
                    strokeDasharray="5,5"
                    className="animate-pulse"
                  />
                </svg>
              </div>

              <div className="flex justify-center space-x-4 mt-4">
                <Button 
                  variant={isTracking ? "destructive" : "default"}
                  onClick={() => setIsTracking(!isTracking)}
                  className="flex items-center space-x-2"
                >
                  {isTracking ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4" />}
                  <span>{isTracking ? 'Pausar' : 'Reanudar'} Seguimiento</span>
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Route Timeline */}
          <Card>
            <CardHeader>
              <CardTitle>Ruta del Paseo</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {activeWalk.route.map((point, index) => (
                  <div key={index} className="flex items-center space-x-4">
                    <div className="w-4 h-4 bg-primary rounded-full flex-shrink-0"></div>
                    <div className="flex-1">
                      <p className="font-medium">{point.activity}</p>
                      <p className="text-sm text-gray-600">{point.time}</p>
                    </div>
                    <Navigation className="w-5 h-5 text-gray-400" />
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Caregiver Info */}
          <Card>
            <CardHeader>
              <CardTitle>Cuidador</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center space-x-3 mb-4">
                <Avatar>
                  <AvatarFallback>{activeWalk.caregiver.name.split(' ').map(n => n[0]).join('')}</AvatarFallback>
                </Avatar>
                <div>
                  <h4 className="font-semibold">{activeWalk.caregiver.name}</h4>
                  <div className="flex items-center">
                    <span className="text-sm text-gray-600 mr-2">★ {activeWalk.caregiver.rating}</span>
                    <Badge variant="secondary">Verificado</Badge>
                  </div>
                </div>
              </div>
              
              <div className="space-y-2">
                <Button variant="outline" className="w-full flex items-center space-x-2">
                  <Phone className="w-4 h-4" />
                  <span>Llamar</span>
                </Button>
                <Button variant="outline" className="w-full flex items-center space-x-2">
                  <MessageCircle className="w-4 h-4" />
                  <span>Mensaje</span>
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Recent Updates */}
          <Card>
            <CardHeader>
              <CardTitle>Actualizaciones</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {recentUpdates.map((update, index) => (
                  <div key={index} className="flex items-start space-x-3">
                    <div className={`w-2 h-2 rounded-full mt-2 ${
                      update.type === 'photo' ? 'bg-blue-500' :
                      update.type === 'location' ? 'bg-green-500' :
                      update.type === 'start' ? 'bg-purple-500' :
                      'bg-gray-500'
                    }`}></div>
                    <div className="flex-1">
                      <p className="text-sm">{update.message}</p>
                      <p className="text-xs text-gray-500">{update.time}</p>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Photos */}
          <Card>
            <CardHeader>
              <CardTitle>Fotos del Paseo</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-3 gap-2">
                {activeWalk.photos.map((photo, index) => (
                  <div key={index} className="aspect-square bg-gray-100 rounded-lg flex items-center justify-center text-2xl">
                    {photo}
                  </div>
                ))}
              </div>
              <Button variant="outline" className="w-full mt-4 flex items-center space-x-2">
                <Camera className="w-4 h-4" />
                <span>Ver Todas</span>
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}