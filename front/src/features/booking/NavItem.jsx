import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '../../shared/ui/Card';
import { Button } from '../../shared/ui/Button';
import { Badge } from '../../shared/ui/Badge';
import { Input } from '../../shared/ui/Input';
import { Label } from '../../shared/ui/Label';
import { Textarea } from '../../shared/ui/Textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../shared/ui/Select';
import { Calendar } from '../../shared/ui/Calendar';
import { Popover, PopoverContent, PopoverTrigger } from '../../shared/ui/Popover';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../../shared/ui/Tabs';
import { Calendar as CalendarIcon, Clock, MapPin, Heart, Check, Plus, Edit, Trash2, Star } from 'lucide-react';



export function BookingSystem({ onNavigate }) {
  const [selectedDate, setSelectedDate] = useState();
  const [bookingStep, setBookingStep] = useState(1);
  const [newBooking, setNewBooking] = useState({
    service: '',
    pet: '',
    caregiver: '',
    date: '',
    time: '',
    duration: '',
    specialInstructions: '',
    pickupLocation: '',
    totalCost: 0
  });
  
  // Función para formato de fecha
  const formatDate = (date) => {
    return date.toLocaleDateString('es-ES', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatShortDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const pets = [
    { id: 1, name: 'Luna', type: 'Perro', breed: 'Golden Retriever' },
    { id: 2, name: 'Milo', type: 'Gato', breed: 'Siamés' }
  ];

  const services = [
    { id: 'walk', name: 'Paseo', basePrice: 25, duration: '45 min', icon: '🚶' },
    { id: 'daycare', name: 'Guardería', basePrice: 40, duration: '8 horas', icon: '🏠' },
    { id: 'grooming', name: 'Grooming', basePrice: 60, duration: '2 horas', icon: '✂️' },
    { id: 'vet', name: 'Consulta Veterinaria', basePrice: 80, duration: '1 hora', icon: '🩺' },
    { id: 'training', name: 'Entrenamiento', basePrice: 50, duration: '1 hora', icon: '🎾' },
    { id: 'overnight', name: 'Cuidado Nocturno', basePrice: 100, duration: '12 horas', icon: '🌙' }
  ];

  const caregivers = [
    { id: 1, name: 'María García', rating: 4.9, specialties: ['Paseos', 'Guardería'] },
    { id: 2, name: 'Carlos López', rating: 4.8, specialties: ['Grooming', 'Veterinaria'] },
    { id: 3, name: 'Ana Ruiz', rating: 5.0, specialties: ['Entrenamiento', 'Cachorros'] }
  ];

  const upcomingBookings = [
    {
      id: 1,
      service: 'Paseo Matutino',
      pet: 'Luna',
      caregiver: 'María García',
      date: '2025-01-15',
      time: '09:00',
      duration: '45 min',
      status: 'Confirmado',
      cost: 25,
      location: 'Parque Central'
    },
    {
      id: 2,
      service: 'Sesión de Grooming',
      pet: 'Milo',
      caregiver: 'Carlos López',
      date: '2025-01-16',
      time: '14:00',
      duration: '2 horas',
      status: 'Pendiente',
      cost: 60,
      location: 'Spa Canino Premium'
    }
  ];

  const bookingHistory = [
    {
      id: 3,
      service: 'Paseo',
      pet: 'Luna',
      caregiver: 'Ana Ruiz',
      date: '2025-01-10',
      time: '16:00',
      status: 'Completado',
      cost: 25,
      rating: 5
    },
    {
      id: 4,
      service: 'Consulta Veterinaria',
      pet: 'Milo',
      caregiver: 'Dr. Fernández',
      date: '2025-01-08',
      time: '10:00',
      status: 'Completado',
      cost: 80,
      rating: 5
    }
  ];

  const timeSlots = [
    '08:00', '09:00', '10:00', '11:00', '12:00',
    '13:00', '14:00', '15:00', '16:00', '17:00', '18:00'
  ];

  const handleBookingComplete = () => {
    setBookingStep(1);
    setNewBooking({
      service: '',
      pet: '',
      caregiver: '',
      date: '',
      time: '',
      duration: '',
      specialInstructions: '',
      pickupLocation: '',
      totalCost: 0
    });
  };

  const BookingSteps = () => {
    switch (bookingStep) {
      case 1:
        return (
          <div className="space-y-6">
            <h3 className="text-lg font-semibold">Selecciona el Servicio</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {services.map((service) => (
                <Card 
                  key={service.id} 
                  className={`cursor-pointer transition-all ${newBooking.service === service.id ? 'ring-2 ring-primary' : 'hover:shadow-md'}`}
                  onClick={() => setNewBooking({...newBooking, service: service.id, totalCost: service.basePrice})}
                >
                  <CardContent className="p-4 text-center">
                    <div className="text-3xl mb-2">{service.icon}</div>
                    <h4 className="font-semibold">{service.name}</h4>
                    <p className="text-sm text-gray-600">{service.duration}</p>
                    <p className="font-semibold text-primary mt-2">${service.basePrice}</p>
                  </CardContent>
                </Card>
              ))}
            </div>
            <Button 
              onClick={() => setBookingStep(2)} 
              disabled={!newBooking.service}
              className="w-full"
            >
              Continuar
            </Button>
          </div>
        );

      case 2:
        return (
          <div className="space-y-6">
            <h3 className="text-lg font-semibold">Selecciona tu Mascota</h3>
            <div className="space-y-3">
              {pets.map((pet) => (
                <Card 
                  key={pet.id}
                  className={`cursor-pointer transition-all ${newBooking.pet === pet.name ? 'ring-2 ring-primary' : 'hover:shadow-md'}`}
                  onClick={() => setNewBooking({...newBooking, pet: pet.name})}
                >
                  <CardContent className="p-4 flex items-center space-x-4">
                    <div className="text-2xl">{pet.type === 'Perro' ? '🐕' : '🐱'}</div>
                    <div>
                      <h4 className="font-semibold">{pet.name}</h4>
                      <p className="text-gray-600">{pet.breed}</p>
                    </div>
                    {newBooking.pet === pet.name && (
                      <Check className="w-5 h-5 text-primary ml-auto" />
                    )}
                  </CardContent>
                </Card>
              ))}
            </div>
            <div className="flex space-x-3">
              <Button variant="outline" onClick={() => setBookingStep(1)} className="flex-1">
                Anterior
              </Button>
              <Button 
                onClick={() => setBookingStep(3)} 
                disabled={!newBooking.pet}
                className="flex-1"
              >
                Continuar
              </Button>
            </div>
          </div>
        );

      case 3:
        return (
          <div className="space-y-6">
            <h3 className="text-lg font-semibold">Fecha y Hora</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <Label>Fecha</Label>
                <Popover>
                  <PopoverTrigger asChild>
                    <Button variant="outline" className="w-full justify-start text-left">
                      <CalendarIcon className="mr-2 h-4 w-4" />
                      {selectedDate ? formatDate(selectedDate) : 'Seleccionar fecha'}
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0">
                    <Calendar
                      mode="single"
                      selected={selectedDate}
                      onSelect={setSelectedDate}
                      initialFocus
                    />
                  </PopoverContent>
                </Popover>
              </div>

              <div>
                <Label>Hora</Label>
                <Select value={newBooking.time} onValueChange={(value) => setNewBooking({...newBooking, time: value})}>
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar hora" />
                  </SelectTrigger>
                  <SelectContent>
                    {timeSlots.map((time) => (
                      <SelectItem key={time} value={time}>{time}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div>
              <Label>Ubicación de Recogida</Label>
              <Input
                value={newBooking.pickupLocation}
                onChange={(e) => setNewBooking({...newBooking, pickupLocation: e.target.value})}
                placeholder="Dirección donde recoger a tu mascota"
              />
            </div>

            <div>
              <Label>Instrucciones Especiales</Label>
              <Textarea
                value={newBooking.specialInstructions}
                onChange={(e) => setNewBooking({...newBooking, specialInstructions: e.target.value})}
                placeholder="Cualquier información importante sobre tu mascota o el servicio..."
                rows={3}
              />
            </div>

            <div className="flex space-x-3">
              <Button variant="outline" onClick={() => setBookingStep(2)} className="flex-1">
                Anterior
              </Button>
              <Button 
                onClick={() => setBookingStep(4)} 
                disabled={!selectedDate || !newBooking.time}
                className="flex-1"
              >
                Continuar
              </Button>
            </div>
          </div>
        );

      case 4:
        return (
          <div className="space-y-6">
            <h3 className="text-lg font-semibold">Confirmar Reserva</h3>
            
            <Card>
              <CardHeader>
                <CardTitle>Resumen de la Reserva</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex justify-between">
                  <span>Servicio:</span>
                  <span className="font-semibold">
                    {services.find(s => s.id === newBooking.service)?.name}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span>Mascota:</span>
                  <span className="font-semibold">{newBooking.pet}</span>
                </div>
                <div className="flex justify-between">
                  <span>Fecha:</span>
                  <span className="font-semibold">
                    {selectedDate ? formatDate(selectedDate) : ''}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span>Hora:</span>
                  <span className="font-semibold">{newBooking.time}</span>
                </div>
                <div className="flex justify-between">
                  <span>Ubicación:</span>
                  <span className="font-semibold">{newBooking.pickupLocation}</span>
                </div>
                <div className="border-t pt-4 flex justify-between text-lg">
                  <span className="font-semibold">Total:</span>
                  <span className="font-bold text-primary">${newBooking.totalCost}</span>
                </div>
              </CardContent>
            </Card>

            <div className="flex space-x-3">
              <Button variant="outline" onClick={() => setBookingStep(3)} className="flex-1">
                Anterior
              </Button>
              <Button onClick={handleBookingComplete} className="flex-1">
                Confirmar Reserva
              </Button>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Sistema de Reservas</h1>
        <p className="text-gray-600">Agenda servicios para tus mascotas</p>
      </div>

      <Tabs defaultValue="new" className="space-y-6">
        <TabsList className="grid grid-cols-3 w-full max-w-md">
          <TabsTrigger value="new">Nueva Reserva</TabsTrigger>
          <TabsTrigger value="upcoming">Próximas</TabsTrigger>
          <TabsTrigger value="history">Historial</TabsTrigger>
        </TabsList>

        <TabsContent value="new">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>Nueva Reserva</CardTitle>
                <div className="flex space-x-2">
                  {[1, 2, 3, 4].map((step) => (
                    <div
                      key={step}
                      className={`w-8 h-8 rounded-full flex items-center justify-center text-sm ${
                        step <= bookingStep
                          ? 'bg-primary text-white'
                          : 'bg-gray-200 text-gray-600'
                      }`}
                    >
                      {step}
                    </div>
                  ))}
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <BookingSteps />
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="upcoming">
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-xl">Próximas Reservas</h2>
              <Button onClick={() => setBookingStep(1)}>
                <Plus className="w-4 h-4 mr-2" />
                Nueva Reserva
              </Button>
            </div>

            {upcomingBookings.map((booking) => (
              <Card key={booking.id}>
                <CardContent className="p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
                        <Heart className="w-6 h-6 text-primary" />
                      </div>
                      <div>
                        <h3 className="font-semibold">{booking.service}</h3>
                        <p className="text-gray-600">Para: {booking.pet}</p>
                        <p className="text-sm text-gray-500">
                          Cuidador: {booking.caregiver}
                        </p>
                      </div>
                    </div>

                    <div className="text-right">
                      <div className="flex items-center text-gray-600 mb-1">
                        <CalendarIcon className="w-4 h-4 mr-1" />
                        <span>{formatShortDate(booking.date)}</span>
                      </div>
                      <div className="flex items-center text-gray-600 mb-1">
                        <Clock className="w-4 h-4 mr-1" />
                        <span>{booking.time} • {booking.duration}</span>
                      </div>
                      <div className="flex items-center text-gray-600 mb-2">
                        <MapPin className="w-4 h-4 mr-1" />
                        <span className="text-sm">{booking.location}</span>
                      </div>
                      <Badge variant={booking.status === 'Confirmado' ? 'default' : 'secondary'}>
                        {booking.status}
                      </Badge>
                    </div>

                    <div className="text-right">
                      <p className="font-semibold text-lg">${booking.cost}</p>
                      <div className="flex space-x-2 mt-2">
                        <Button variant="outline" size="sm">
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button variant="outline" size="sm">
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}

            {upcomingBookings.length === 0 && (
              <div className="text-center py-16">
                <div className="text-6xl mb-4">📅</div>
                <h3 className="text-xl mb-2">No tienes reservas próximas</h3>
                <p className="text-gray-600 mb-6">
                  Agenda un nuevo servicio para tu mascota
                </p>
                <Button onClick={() => setBookingStep(1)}>
                  Crear Primera Reserva
                </Button>
              </div>
            )}
          </div>
        </TabsContent>

        <TabsContent value="history">
          <div className="space-y-4">
            <h2 className="text-xl">Historial de Servicios</h2>

            {bookingHistory.map((booking) => (
              <Card key={booking.id}>
                <CardContent className="p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                        <Check className="w-6 h-6 text-green-600" />
                      </div>
                      <div>
                        <h3 className="font-semibold">{booking.service}</h3>
                        <p className="text-gray-600">Para: {booking.pet}</p>
                        <p className="text-sm text-gray-500">
                          Cuidador: {booking.caregiver}
                        </p>
                      </div>
                    </div>

                    <div className="text-center">
                      <p className="text-sm text-gray-600">
                        {formatShortDate(booking.date)} • {booking.time}
                      </p>
                      <Badge variant="secondary" className="bg-green-100 text-green-800">
                        {booking.status}
                      </Badge>
                    </div>

                    <div className="text-right">
                      <p className="font-semibold">${booking.cost}</p>
                      <div className="flex items-center mt-1">
                        {[...Array(5)].map((_, i) => (
                          <Star
                            key={i}
                            className={`w-4 h-4 ${
                              i < booking.rating
                                ? 'text-yellow-400 fill-current'
                                : 'text-gray-300'
                            }`}
                          />
                        ))}
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
}