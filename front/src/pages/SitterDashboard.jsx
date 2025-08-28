import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/ui/Tabs';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../components/ui/Dialog';
import { Input } from '../components/ui/Input';
import { Label } from '../components/ui/Label';
import { Switch } from '../components/ui/Switch';
import { Textarea } from '../components/ui/Textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/Select';
import { 
  Calendar, 
  Clock, 
  MapPin, 
  DollarSign, 
  Star, 
  Settings, 
  Plus,
  CheckCircle,
  Heart
} from 'lucide-react';



export function SitterDashboard({ user, onNavigate }) {
  const [schedule, setSchedule] = useState([
    { day: 'Lunes', enabled: true, startTime: '08:00', endTime: '18:00' },
    { day: 'Martes', enabled: true, startTime: '08:00', endTime: '18:00' },
    { day: 'Miércoles', enabled: true, startTime: '08:00', endTime: '18:00' },
    { day: 'Jueves', enabled: true, startTime: '08:00', endTime: '18:00' },
    { day: 'Viernes', enabled: true, startTime: '08:00', endTime: '18:00' },
    { day: 'Sábado', enabled: false, startTime: '09:00', endTime: '15:00' },
    { day: 'Domingo', enabled: false, startTime: '09:00', endTime: '15:00' },
  ]);

  const [serviceZones, setServiceZones] = useState([
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
  ]);

  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const [isZoneModalOpen, setIsZoneModalOpen] = useState(false);
  const [newZone, setNewZone] = useState({ name: '', radius: 5, centerAddress: '' });

  const upcomingBookings = [
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

  const stats = {
    totalBookings: 24,
    thisWeekBookings: 6,
    earnings: 480,
    rating: 4.9,
    completedServices: 18
  };

  const handleScheduleUpdate = (dayIndex, field, value) => {
    const updatedSchedule = [...schedule];
    updatedSchedule[dayIndex] = { ...updatedSchedule[dayIndex], [field]: value };
    setSchedule(updatedSchedule);
  };

  const handleAddServiceZone = () => {
    if (newZone.name && newZone.centerAddress) {
  const zone = {
        id: Math.random().toString(),
        ...newZone,
        active: true
      };
      setServiceZones([...serviceZones, zone]);
      setNewZone({ name: '', radius: 5, centerAddress: '' });
      setIsZoneModalOpen(false);
    }
  };

  const toggleZoneStatus = (zoneId) => {
    setServiceZones(serviceZones.map(zone => 
      zone.id === zoneId ? { ...zone, active: !zone.active } : zone
    ));
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Dashboard de Cuidador</h1>
        <p className="text-muted-foreground">Bienvenido, {user.name}</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-6 mb-8">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Reservas Totales</p>
                <p className="text-2xl">{stats.totalBookings}</p>
              </div>
              <Calendar className="w-8 h-8 text-blue-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Esta Semana</p>
                <p className="text-2xl">{stats.thisWeekBookings}</p>
              </div>
              <Clock className="w-8 h-8 text-green-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Ganancias</p>
                <p className="text-2xl">${stats.earnings}</p>
              </div>
              <DollarSign className="w-8 h-8 text-green-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Calificación</p>
                <p className="text-2xl">{stats.rating}</p>
              </div>
              <Star className="w-8 h-8 text-yellow-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Completadas</p>
                <p className="text-2xl">{stats.completedServices}</p>
              </div>
              <CheckCircle className="w-8 h-8 text-primary" />
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="bookings" className="space-y-6">
        <TabsList>
          <TabsTrigger value="bookings">Reservas</TabsTrigger>
          <TabsTrigger value="schedule">Horarios</TabsTrigger>
          <TabsTrigger value="zones">Zonas de Servicio</TabsTrigger>
          <TabsTrigger value="profile">Perfil</TabsTrigger>
        </TabsList>

        <TabsContent value="bookings" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Próximas Reservas</CardTitle>
              <CardDescription>Gestiona tus servicios programados</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {upcomingBookings.map((booking) => (
                  <Card key={booking.id} className="p-4">
                    <div className="flex items-center justify-between mb-3">
                      <div className="flex items-center gap-3">
                        <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
                          <Heart className="w-5 h-5 text-primary" />
                        </div>
                        <div>
                          <h4 className="font-medium">{booking.service}</h4>
                          <p className="text-sm text-muted-foreground">
                            {booking.pet} • {booking.owner}
                          </p>
                        </div>
                      </div>
                      <Badge variant={booking.status === 'confirmado' ? 'default' : 'secondary'}>
                        {booking.status === 'confirmado' ? 'Confirmado' : 'Pendiente'}
                      </Badge>
                    </div>
                    
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                      <div>
                        <p className="text-muted-foreground">Horario</p>
                        <p className="font-medium">{booking.time}</p>
                      </div>
                      <div>
                        <p className="text-muted-foreground">Duración</p>
                        <p className="font-medium">{booking.duration}</p>
                      </div>
                      <div>
                        <p className="text-muted-foreground">Pago</p>
                        <p className="font-medium">{booking.payment}</p>
                      </div>
                      <div>
                        <p className="text-muted-foreground">Ubicación</p>
                        <p className="font-medium">{booking.address}</p>
                      </div>
                    </div>
                    
                    <div className="flex gap-2 mt-4">
                      <Button size="sm">Ver Detalles</Button>
                      <Button size="sm" variant="outline">Contactar Dueño</Button>
                      {booking.status === 'pendiente' && (
                        <Button size="sm" variant="outline">Confirmar</Button>
                      )}
                    </div>
                  </Card>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="schedule" className="space-y-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <div>
                <CardTitle>Configuración de Horarios</CardTitle>
                <CardDescription>Define tu disponibilidad semanal</CardDescription>
              </div>
              <Dialog open={isScheduleModalOpen} onOpenChange={setIsScheduleModalOpen}>
                <DialogTrigger asChild>
                  <Button>
                    <Settings className="w-4 h-4 mr-2" />
                    Configuración Masiva
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Configuración Masiva de Horarios</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <Label>Hora de Inicio</Label>
                        <Input type="time" defaultValue="08:00" />
                      </div>
                      <div>
                        <Label>Hora de Fin</Label>
                        <Input type="time" defaultValue="18:00" />
                      </div>
                    </div>
                    <div className="space-y-2">
                      <Label>Aplicar a:</Label>
                      <div className="space-y-2">
                        <div className="flex items-center space-x-2">
                          <input type="checkbox" id="weekdays" defaultChecked />
                          <Label htmlFor="weekdays">Días de semana (L-V)</Label>
                        </div>
                        <div className="flex items-center space-x-2">
                          <input type="checkbox" id="weekends" />
                          <Label htmlFor="weekends">Fines de semana (S-D)</Label>
                        </div>
                      </div>
                    </div>
                    <Button onClick={() => setIsScheduleModalOpen(false)}>
                      Aplicar Cambios
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {schedule.map((day, index) => (
                  <div key={day.day} className="flex items-center justify-between p-4 border rounded-lg">
                    <div className="flex items-center space-x-4">
                      <Switch
                        checked={day.enabled}
                        onCheckedChange={(checked) => handleScheduleUpdate(index, 'enabled', checked)}
                      />
                      <span className="font-medium min-w-[80px]">{day.day}</span>
                    </div>
                    
                    {day.enabled && (
                      <div className="flex items-center space-x-4">
                        <div className="flex items-center space-x-2">
                          <Input
                            type="time"
                            value={day.startTime}
                            onChange={(e) => handleScheduleUpdate(index, 'startTime', e.target.value)}
                            className="w-32"
                          />
                          <span className="text-muted-foreground">a</span>
                          <Input
                            type="time"
                            value={day.endTime}
                            onChange={(e) => handleScheduleUpdate(index, 'endTime', e.target.value)}
                            className="w-32"
                          />
                        </div>
                      </div>
                    )}
                    
                    {!day.enabled && (
                      <span className="text-muted-foreground">No disponible</span>
                    )}
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="zones" className="space-y-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <div>
                <CardTitle>Zonas de Servicio</CardTitle>
                <CardDescription>Define las áreas donde ofreces tus servicios</CardDescription>
              </div>
              <Dialog open={isZoneModalOpen} onOpenChange={setIsZoneModalOpen}>
                <DialogTrigger asChild>
                  <Button>
                    <Plus className="w-4 h-4 mr-2" />
                    Agregar Zona
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Nueva Zona de Servicio</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div>
                      <Label htmlFor="zoneName">Nombre de la Zona</Label>
                      <Input
                        id="zoneName"
                        placeholder="ej. Centro de la Ciudad"
                        value={newZone.name}
                        onChange={(e) => setNewZone({ ...newZone, name: e.target.value })}
                      />
                    </div>
                    <div>
                      <Label htmlFor="zoneAddress">Dirección Central</Label>
                      <Input
                        id="zoneAddress"
                        placeholder="ej. Plaza Principal, Centro"
                        value={newZone.centerAddress}
                        onChange={(e) => setNewZone({ ...newZone, centerAddress: e.target.value })}
                      />
                    </div>
                    <div>
                      <Label htmlFor="zoneRadius">Radio de Servicio (km)</Label>
                      <Select value={newZone.radius.toString()} onValueChange={(value) => setNewZone({ ...newZone, radius: parseInt(value) })}>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="1">1 km</SelectItem>
                          <SelectItem value="3">3 km</SelectItem>
                          <SelectItem value="5">5 km</SelectItem>
                          <SelectItem value="10">10 km</SelectItem>
                          <SelectItem value="15">15 km</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    <Button onClick={handleAddServiceZone}>
                      Agregar Zona
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {serviceZones.map((zone) => (
                  <Card key={zone.id} className="p-4">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-4">
                        <div className={`w-4 h-4 rounded-full ${zone.active ? 'bg-green-500' : 'bg-gray-400'}`} />
                        <div>
                          <h4 className="font-medium">{zone.name}</h4>
                          <p className="text-sm text-muted-foreground">
                            <MapPin className="w-4 h-4 inline mr-1" />
                            {zone.centerAddress} • Radio: {zone.radius}km
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Badge variant={zone.active ? 'default' : 'secondary'}>
                          {zone.active ? 'Activa' : 'Inactiva'}
                        </Badge>
                        <Button
                          variant="outline" 
                          size="sm"
                          onClick={() => toggleZoneStatus(zone.id)}
                        >
                          {zone.active ? 'Desactivar' : 'Activar'}
                        </Button>
                      </div>
                    </div>
                  </Card>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="profile" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Información del Perfil</CardTitle>
              <CardDescription>Gestiona tu información como cuidador</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="profileName">Nombre</Label>
                  <Input id="profileName" defaultValue={user.name} />
                </div>
                <div>
                  <Label htmlFor="profileEmail">Email</Label>
                  <Input id="profileEmail" defaultValue={user.email} disabled />
                </div>
                <div>
                  <Label htmlFor="profilePhone">Teléfono</Label>
                  <Input id="profilePhone" defaultValue={user.phone} />
                </div>
                <div>
                  <Label htmlFor="profileAddress">Dirección</Label>
                  <Input id="profileAddress" defaultValue={user.address} />
                </div>
              </div>
              
              <div>
                <Label htmlFor="profileBio">Descripción</Label>
                <Textarea 
                  id="profileBio" 
                  placeholder="Cuéntanos sobre tu experiencia cuidando mascotas..."
                  rows={4}
                />
              </div>
              
              <Button>Guardar Cambios</Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}