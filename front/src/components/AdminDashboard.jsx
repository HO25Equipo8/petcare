import { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './ui/table';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from './ui/dialog';
import { 
  Users, 
  Heart, 
  UserCheck, 
  AlertTriangle, 
  DollarSign, 
  TrendingUp,
  Shield,
  Eye,
  Edit,
  Ban,
  CheckCircle,
  XCircle,
  Star,
  MapPin
} from 'lucide-react';



export function AdminDashboard({ user, onNavigate }) {
  const [selectedUser, setSelectedUser] = useState(null);
  const [isUserModalOpen, setIsUserModalOpen] = useState(false);

  const stats = {
    totalUsers: 1247,
    totalOwners: 892,
    totalSitters: 355,
    totalBookings: 3456,
    totalRevenue: 89750,
    activeBookings: 124,
    pendingIncidents: 8,
    averageRating: 4.7
  };

  const recentUsers = [
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

  const pendingIncidents = [
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

  const getStatusColor = (status) => {
    switch (status) {
      case 'active': return 'bg-green-100 text-green-800';
      case 'suspended': return 'bg-red-100 text-red-800';
      case 'pending': return 'bg-yellow-100 text-yellow-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'high': return 'bg-red-100 text-red-800';
      case 'medium': return 'bg-yellow-100 text-yellow-800';
      case 'low': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const handleUserAction = (userId, action) => {
    console.log(`Action ${action} for user ${userId}`);
    // Aquí iría la lógica para manejar las acciones de usuario
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Panel de Administración</h1>
        <p className="text-muted-foreground">Bienvenido, {user.name} - Gestiona la plataforma PetCare</p>
      </div>

  {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Total Usuarios</p>
                <p className="text-2xl">{stats.totalUsers.toLocaleString()}</p>
                <p className="text-xs text-green-600">+12% este mes</p>
              </div>
              <Users className="w-8 h-8 text-blue-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Dueños / Cuidadores</p>
                <p className="text-2xl">{stats.totalOwners} / {stats.totalSitters}</p>
                <p className="text-xs text-blue-600">Ratio 2.5:1</p>
              </div>
              <div className="flex space-x-1">
                <Heart className="w-4 h-4 text-primary" />
                <UserCheck className="w-4 h-4 text-green-500" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Ingresos Totales</p>
                <p className="text-2xl">${stats.totalRevenue.toLocaleString()}</p>
                <p className="text-xs text-green-600">+8% este mes</p>
              </div>
              <DollarSign className="w-8 h-8 text-green-500" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Incidentes Pendientes</p>
                <p className="text-2xl text-orange-600">{stats.pendingIncidents}</p>
                <p className="text-xs text-orange-600">Requiere atención</p>
              </div>
              <AlertTriangle className="w-8 h-8 text-orange-500" />
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="users" className="space-y-6">
        <TabsList>
          <TabsTrigger value="users">Gestión de Usuarios</TabsTrigger>
          <TabsTrigger value="incidents">Incidentes</TabsTrigger>
          <TabsTrigger value="analytics">Analytics</TabsTrigger>
          <TabsTrigger value="settings">Configuración</TabsTrigger>
        </TabsList>

        <TabsContent value="users" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Usuarios del Sistema</CardTitle>
              <CardDescription>Gestiona todos los usuarios registrados en la plataforma</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="mb-4 flex justify-between items-center">
                <div className="flex gap-4">
                  <Input placeholder="Buscar usuarios..." className="w-64" />
                  <Select>
                    <SelectTrigger className="w-40">
                      <SelectValue placeholder="Filtrar por rol" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">Todos</SelectItem>
                      <SelectItem value="owner">Dueños</SelectItem>
                      <SelectItem value="sitter">Cuidadores</SelectItem>
                    </SelectContent>
                  </Select>
                  <Select>
                    <SelectTrigger className="w-40">
                      <SelectValue placeholder="Estado" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">Todos</SelectItem>
                      <SelectItem value="active">Activos</SelectItem>
                      <SelectItem value="pending">Pendientes</SelectItem>
                      <SelectItem value="suspended">Suspendidos</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Usuario</TableHead>
                    <TableHead>Rol</TableHead>
                    <TableHead>Estado</TableHead>
                    <TableHead>Fecha de Registro</TableHead>
                    <TableHead>Última Actividad</TableHead>
                    <TableHead>Stats</TableHead>
                    <TableHead>Acciones</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {recentUsers.map((systemUser) => (
                    <TableRow key={systemUser.id}>
                      <TableCell>
                        <div>
                          <p className="font-medium">{systemUser.name}</p>
                          <p className="text-sm text-muted-foreground">{systemUser.email}</p>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">
                          {systemUser.role === 'owner' ? 'Dueño' : 'Cuidador'}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge className={getStatusColor(systemUser.status)}>
                          {systemUser.status === 'active' ? 'Activo' : 
                           systemUser.status === 'pending' ? 'Pendiente' : 'Suspendido'}
                        </Badge>
                      </TableCell>
                      <TableCell>{systemUser.joinDate}</TableCell>
                      <TableCell>{systemUser.lastActive}</TableCell>
                      <TableCell>
                        <div className="text-sm">
                          {systemUser.role === 'sitter' ? (
                            <>
                              <p>Reservas: {systemUser.totalBookings}</p>
                              <p>Rating: {systemUser.rating} ⭐</p>
                              <p>Ingresos: ${systemUser.earnings}</p>
                            </>
                          ) : (
                            <p>Reservas: {systemUser.totalBookings}</p>
                          )}
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button 
                            size="sm" 
                            variant="outline"
                            onClick={() => {
                              setSelectedUser(systemUser);
                              setIsUserModalOpen(true);
                            }}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button size="sm" variant="outline">
                            <Edit className="w-4 h-4" />
                          </Button>
                          {systemUser.status === 'active' ? (
                            <Button 
                              size="sm" 
                              variant="destructive"
                              onClick={() => handleUserAction(systemUser.id, 'suspend')}
                            >
                              <Ban className="w-4 h-4" />
                            </Button>
                          ) : (
                            <Button 
                              size="sm" 
                              variant="default"
                              onClick={() => handleUserAction(systemUser.id, 'activate')}
                            >
                              <CheckCircle className="w-4 h-4" />
                            </Button>
                          )}
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="incidents" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Incidentes del Sistema</CardTitle>
              <CardDescription>Supervisa y gestiona todos los incidentes reportados</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {pendingIncidents.map((incident) => (
                  <Card key={incident.id} className="p-4">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-4">
                        <AlertTriangle className="w-5 h-5 text-orange-500" />
                        <div>
                          <h4 className="font-medium">{incident.title}</h4>
                          <p className="text-sm text-muted-foreground">
                            Mascota: {incident.pet} • Cuidador: {incident.sitter} • {incident.date}
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Badge className={getSeverityColor(incident.severity)}>
                          {incident.severity === 'high' ? 'Alta' : 
                           incident.severity === 'medium' ? 'Media' : 'Baja'}
                        </Badge>
                        <Button size="sm" onClick={() => onNavigate('incidents')}>
                          Ver Detalles
                        </Button>
                      </div>
                    </div>
                  </Card>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="analytics" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card>
              <CardHeader>
                <CardTitle>Métricas de Crecimiento</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex justify-between items-center">
                    <span>Nuevos usuarios este mes</span>
                    <span className="font-semibold text-green-600">+127</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Retención de usuarios</span>
                    <span className="font-semibold">84%</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Calificación promedio</span>
                    <span className="font-semibold">{stats.averageRating} ⭐</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Servicios completados</span>
                    <span className="font-semibold">{stats.totalBookings.toLocaleString()}</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Ingresos y Comisiones</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex justify-between items-center">
                    <span>Ingresos totales</span>
                    <span className="font-semibold">${stats.totalRevenue.toLocaleString()}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Comisiones de plataforma (10%)</span>
                    <span className="font-semibold">${(stats.totalRevenue * 0.1).toLocaleString()}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Crecimiento mensual</span>
                    <span className="font-semibold text-green-600">+8.2%</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span>Servicios activos</span>
                    <span className="font-semibold">{stats.activeBookings}</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="settings" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Configuración del Sistema</CardTitle>
              <CardDescription>Ajusta los parámetros globales de la plataforma</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-4">
                  <div>
                    <Label>Comisión de la plataforma (%)</Label>
                    <Input type="number" defaultValue="10" min="0" max="50" />
                  </div>
                  <div>
                    <Label>Tiempo mínimo de reserva (horas)</Label>
                    <Input type="number" defaultValue="1" min="1" max="24" />
                  </div>
                  <div>
                    <Label>Radio máximo de servicio (km)</Label>
                    <Input type="number" defaultValue="15" min="1" max="50" />
                  </div>
                </div>
                <div className="space-y-4">
                  <div>
                    <Label>Tiempo límite de cancelación (horas)</Label>
                    <Input type="number" defaultValue="24" min="1" max="168" />
                  </div>
                  <div>
                    <Label>Calificación mínima para cuidadores</Label>
                    <Input type="number" defaultValue="4.0" min="1" max="5" step="0.1" />
                  </div>
                  <div>
                    <Label>Verificación obligatoria de identidad</Label>
                    <Select defaultValue="required">
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="required">Obligatoria</SelectItem>
                        <SelectItem value="optional">Opcional</SelectItem>
                        <SelectItem value="disabled">Deshabilitada</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
              </div>
              <Button>Guardar Configuración</Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

  {/* User Details Modal */}
      <Dialog open={isUserModalOpen} onOpenChange={setIsUserModalOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Detalles del Usuario</DialogTitle>
          </DialogHeader>
          {selectedUser && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label>Nombre</Label>
                  <p className="font-medium">{selectedUser.name}</p>
                </div>
                <div>
                  <Label>Email</Label>
                  <p className="font-medium">{selectedUser.email}</p>
                </div>
                <div>
                  <Label>Rol</Label>
                  <Badge>{selectedUser.role === 'owner' ? 'Dueño' : 'Cuidador'}</Badge>
                </div>
                <div>
                  <Label>Estado</Label>
                  <Badge className={getStatusColor(selectedUser.status)}>
                    {selectedUser.status === 'active' ? 'Activo' : 
                     selectedUser.status === 'pending' ? 'Pendiente' : 'Suspendido'}
                  </Badge>
                </div>
              </div>
              
              {selectedUser.role === 'sitter' && (
                <div className="space-y-2">
                  <h4 className="font-medium">Estadísticas de Cuidador</h4>
                  <div className="grid grid-cols-3 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Total Reservas</p>
                      <p className="font-semibold">{selectedUser.totalBookings}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Calificación</p>
                      <p className="font-semibold">{selectedUser.rating} ⭐</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Ingresos</p>
                      <p className="font-semibold">${selectedUser.earnings}</p>
                    </div>
                  </div>
                </div>
              )}
              
              <div className="flex gap-2 pt-4">
                <Button>Editar Usuario</Button>
                <Button variant="outline">Ver Historial</Button>
                <Button variant="outline">Contactar</Button>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}