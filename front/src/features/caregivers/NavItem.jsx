import { useState } from 'react';
import { Card, CardContent } from '../../shared/ui/Card';
import { Button } from '../../shared/ui/Button';
import { Badge } from '../../shared/ui/Badge';
import { Avatar, AvatarFallback } from '../../shared/ui/Avatar';
import { Input } from '../../shared/ui/Input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../shared/ui/Select';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../../shared/ui/Tabs';
import { Star, MapPin, Phone, MessageCircle, Filter, Search } from 'lucide-react';



export function CaregiverDirectory({ onNavigate }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedService, setSelectedService] = useState('all');
  const [selectedLocation, setSelectedLocation] = useState('all');

  const caregivers = [
    {
      id: 1,
      name: 'María García',
      rating: 4.9,
      reviews: 127,
      location: 'Zona Norte',
      distance: '1.2 km',
      services: ['Paseos', 'Guardería', 'Cuidado Nocturno'],
      specialties: ['Perros Grandes', 'Entrenamiento Básico'],
      experience: '5 años',
      hourlyRate: 25,
      availability: 'Disponible',
      avatar: '/avatars/maria.jpg',
      verified: true,
      languages: ['Español', 'Inglés'],
      description: 'Amante de los animales con experiencia en el cuidado de perros de todos los tamaños. Especialista en perros grandes y entrenamiento básico.',
      certifications: ['Primeros Auxilios Veterinarios', 'Entrenamiento Canino Certificado'],
      responseTime: '< 2 horas'
    },
    {
      id: 2,
      name: 'Carlos López',
      rating: 4.8,
      reviews: 95,
      location: 'Centro',
      distance: '2.1 km',
      services: ['Grooming', 'Paseos', 'Cuidado Médico'],
      specialties: ['Gatos', 'Cuidados Especiales'],
      experience: '7 años',
      hourlyRate: 30,
      availability: 'Ocupado',
      avatar: '/avatars/carlos.jpg',
      verified: true,
      languages: ['Español'],
      description: 'Veterinario con práctica privada que ofrece servicios de cuidado especializado. Experto en felinos y cuidados médicos.',
      certifications: ['Médico Veterinario', 'Especialista en Felinos'],
      responseTime: '< 1 hora'
    },
    {
      id: 3,
      name: 'Ana Ruiz',
      rating: 5.0,
      reviews: 203,
      location: 'Zona Sur',
      distance: '3.5 km',
      services: ['Paseos', 'Guardería', 'Entrenamiento'],
      specialties: ['Cachorros', 'Perros Pequeños'],
      experience: '3 años',
      hourlyRate: 22,
      availability: 'Disponible',
      avatar: '/avatars/ana.jpg',
      verified: true,
      languages: ['Español', 'Francés'],
      description: 'Especialista en el cuidado de cachorros y perros pequeños. Enfoque en socialización y entrenamiento positivo.',
      certifications: ['Entrenamiento en Refuerzo Positivo', 'Cuidado de Cachorros'],
      responseTime: '< 30 min'
    },
    {
      id: 4,
      name: 'Roberto Silva',
      rating: 4.7,
      reviews: 81,
      location: 'Zona Este',
      distance: '4.2 km',
      services: ['Paseos', 'Cuidado Nocturno'],
      specialties: ['Perros de Rescate', 'Rehabilitación'],
      experience: '10 años',
      hourlyRate: 35,
      availability: 'Disponible',
      avatar: '/avatars/roberto.jpg',
      verified: true,
      languages: ['Español', 'Inglés', 'Italiano'],
      description: 'Experto en rehabilitación de perros rescatados. Voluntario en refugios locales con amplia experiencia.',
      certifications: ['Terapia de Comportamiento Canino', 'Rehabilitación Animal'],
      responseTime: '< 3 horas'
    },
    {
      id: 5,
      name: 'Laura Mendoza',
      rating: 4.9,
      reviews: 156,
      location: 'Zona Oeste',
      distance: '2.8 km',
      services: ['Grooming', 'Spa para Mascotas'],
      specialties: ['Estética Canina', 'Relajación'],
      experience: '6 años',
      hourlyRate: 40,
      availability: 'Disponible',
      avatar: '/avatars/laura.jpg',
      verified: true,
      languages: ['Español'],
      description: 'Especialista en grooming profesional y tratamientos de spa para mascotas. Productos orgánicos y técnicas relajantes.',
      certifications: ['Grooming Profesional Certificado', 'Aromaterapia para Mascotas'],
      responseTime: '< 1 hora'
    }
  ];

  const filteredCaregivers = caregivers.filter(caregiver => {
    const matchesSearch = caregiver.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         caregiver.specialties.some(s => s.toLowerCase().includes(searchTerm.toLowerCase()));
    const matchesService = selectedService === 'all' || caregiver.services.includes(selectedService);
    const matchesLocation = selectedLocation === 'all' || caregiver.location === selectedLocation;
    
    return matchesSearch && matchesService && matchesLocation;
  });

  const CaregiverCard = ({ caregiver }) => (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow">
      <CardContent className="p-6">
        <div className="flex items-start space-x-4">
          <Avatar className="w-16 h-16">
            <AvatarFallback>{caregiver.name.split(' ').map((n) => n[0]).join('')}</AvatarFallback>
          </Avatar>
          
          <div className="flex-1">
            <div className="flex items-start justify-between mb-2">
              <div>
                <h3 className="font-semibold text-lg">{caregiver.name}</h3>
                <div className="flex items-center space-x-2 mb-1">
                  <div className="flex items-center">
                    <Star className="w-4 h-4 text-yellow-400 fill-current" />
                    <span className="ml-1 font-medium">{caregiver.rating}</span>
                    <span className="text-gray-500 ml-1">({caregiver.reviews} reseñas)</span>
                  </div>
                  {caregiver.verified && (
                    <Badge variant="secondary" className="bg-green-100 text-green-800">
                      Verificado
                    </Badge>
                  )}
                </div>
                <div className="flex items-center text-gray-600 text-sm mb-2">
                  <MapPin className="w-4 h-4 mr-1" />
                  <span>{caregiver.location} • {caregiver.distance}</span>
                </div>
              </div>
              
              <div className="text-right">
                <p className="font-semibold text-lg">${caregiver.hourlyRate}/hora</p>
                <Badge variant={caregiver.availability === 'Disponible' ? 'default' : 'secondary'}>
                  {caregiver.availability}
                </Badge>
              </div>
            </div>

            <div className="mb-3">
              <div className="flex flex-wrap gap-2 mb-2">
                {caregiver.services.map((service, index) => (
                  <Badge key={index} variant="outline" className="text-xs">
                    {service}
                  </Badge>
                ))}
              </div>
              <div className="flex flex-wrap gap-2">
                {caregiver.specialties.map((specialty, index) => (
                  <Badge key={index} variant="secondary" className="text-xs bg-blue-100 text-blue-800">
                    {specialty}
                  </Badge>
                ))}
              </div>
            </div>

            <p className="text-gray-600 text-sm mb-3 line-clamp-2">
              {caregiver.description}
            </p>

            <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
              <span>{caregiver.experience} de experiencia</span>
              <span>Responde en {caregiver.responseTime}</span>
            </div>

            <div className="flex space-x-2">
              <Button variant="outline" size="sm" className="flex-1">
                <MessageCircle className="w-4 h-4 mr-2" />
                Mensaje
              </Button>
              <Button variant="outline" size="sm" className="flex-1">
                <Phone className="w-4 h-4 mr-2" />
                Llamar
              </Button>
              <Button 
                size="sm" 
                className="flex-1"
                onClick={() => onNavigate('booking')}
              >
                Reservar
              </Button>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Cuidadores Profesionales</h1>
        <p className="text-gray-600">Encuentra el cuidador perfecto para tu mascota</p>
      </div>

      {/* Search and Filters */}
      <Card className="mb-8">
        <CardContent className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-3 w-4 h-4 text-gray-400" />
              <Input
                placeholder="Buscar por nombre o especialidad..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            
            <Select value={selectedService} onValueChange={setSelectedService}>
              <SelectTrigger>
                <SelectValue placeholder="Tipo de Servicio" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todos los Servicios</SelectItem>
                <SelectItem value="Paseos">Paseos</SelectItem>
                <SelectItem value="Guardería">Guardería</SelectItem>
                <SelectItem value="Grooming">Grooming</SelectItem>
                <SelectItem value="Cuidado Médico">Cuidado Médico</SelectItem>
                <SelectItem value="Entrenamiento">Entrenamiento</SelectItem>
              </SelectContent>
            </Select>
            
            <Select value={selectedLocation} onValueChange={setSelectedLocation}>
              <SelectTrigger>
                <SelectValue placeholder="Ubicación" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todas las Zonas</SelectItem>
                <SelectItem value="Zona Norte">Zona Norte</SelectItem>
                <SelectItem value="Centro">Centro</SelectItem>
                <SelectItem value="Zona Sur">Zona Sur</SelectItem>
                <SelectItem value="Zona Este">Zona Este</SelectItem>
                <SelectItem value="Zona Oeste">Zona Oeste</SelectItem>
              </SelectContent>
            </Select>
            
            <Button variant="outline" className="flex items-center space-x-2">
              <Filter className="w-4 h-4" />
              <span>Más Filtros</span>
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Results */}
      <Tabs defaultValue="list" className="mb-8">
        <TabsList>
          <TabsTrigger value="list">Lista</TabsTrigger>
          <TabsTrigger value="map">Mapa</TabsTrigger>
        </TabsList>
        
        <TabsContent value="list" className="space-y-6">
          <div className="flex justify-between items-center">
            <p className="text-gray-600">
              {filteredCaregivers.length} cuidadores encontrados
            </p>
            <Select defaultValue="rating">
              <SelectTrigger className="w-48">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="rating">Mejor Calificación</SelectItem>
                <SelectItem value="distance">Más Cercano</SelectItem>
                <SelectItem value="price-low">Precio Menor</SelectItem>
                <SelectItem value="price-high">Precio Mayor</SelectItem>
                <SelectItem value="experience">Más Experiencia</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-4">
            {filteredCaregivers.map((caregiver) => (
              <CaregiverCard key={caregiver.id} caregiver={caregiver} />
            ))}
          </div>

          {filteredCaregivers.length === 0 && (
            <div className="text-center py-16">
              <div className="text-6xl mb-4">🔍</div>
              <h3 className="text-xl mb-2">No se encontraron cuidadores</h3>
              <p className="text-gray-600 mb-6">
                Intenta ajustar tus filtros de búsqueda
              </p>
              <Button onClick={() => {
                setSearchTerm('');
                setSelectedService('all');
                setSelectedLocation('all');
              }}>
                Limpiar Filtros
              </Button>
            </div>
          )}
        </TabsContent>
        
        <TabsContent value="map">
          <Card>
            <CardContent className="p-8">
              <div className="bg-gradient-to-br from-green-100 to-blue-100 rounded-lg h-96 flex items-center justify-center">
                <div className="text-center">
                  <MapPin className="w-16 h-16 text-primary mx-auto mb-4" />
                  <h3 className="text-xl mb-2">Vista de Mapa</h3>
                  <p className="text-gray-600">
                    Aquí se mostraría un mapa interactivo con la ubicación de los cuidadores
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}