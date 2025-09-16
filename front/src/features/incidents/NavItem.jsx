import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../shared/ui/Card';
import { Button } from '../../shared/ui/Button';
import { Input } from '../../shared/ui/Input';
import { Textarea } from '../../shared/ui/Textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../shared/ui/Select';
import { Label } from '../../shared/ui/Label';
import { Badge } from '../../shared/ui/Badge';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../../shared/ui/Dialog';
import { AlertTriangle, Camera, CheckCircle, Clock, Plus } from 'lucide-react';
import { FallbackImage } from '../../shared/ui/FallbackImage';
import StatsCard from '../../shared/dashboard/StatsCard';

export function IncidentManagement({ onNavigate }) {
  const [incidents, setIncidents] = useState([
    {
      id: '1',
      petName: 'Max',
      caregiverName: 'Carlos García',
      date: '2024-01-15',
      time: '14:30',
      type: 'medical',
      severity: 'medium',
      title: 'Cojera leve durante el paseo',
      description: 'Max comenzó a cojear ligeramente de la pata trasera derecha durante el paseo en el parque. Se detuvo varias veces para descansar.',
      images: ['https://images.unsplash.com/photo-1551717743-49959800b1f6?w=300&h=200&fit=crop'],
      status: 'resolved',
      resolution: 'Se llevó a Max al veterinario. Diagnóstico: pequeña espina en la almohadilla. Se removió y se aplicó antiséptico. Max está completamente recuperado.',
      resolvedBy: 'Dr. Ana Martínez',
      resolvedDate: '2024-01-16'
    },
    {
      id: '2',
      petName: 'Luna',
      caregiverName: 'María Rodríguez',
      date: '2024-01-14',
      time: '10:15',
      type: 'behavioral',
      severity: 'low',
      title: 'Comportamiento ansioso en guardería',
      description: 'Luna mostró signos de ansiedad al separarse del dueño. Lloró por aproximadamente 15 minutos antes de calmarse.',
      images: [],
      status: 'in-progress',
    },
    {
      id: '3',
      petName: 'Rocky',
      caregiverName: 'Juan López',
      date: '2024-01-13',
      time: '16:45',
      type: 'accident',
      severity: 'high',
      title: 'Altercado con otro perro',
      description: 'Rocky tuvo un altercado menor con otro perro en el parque. Se separaron inmediatamente. Rocky tiene un pequeño rasguño en el hocico.',
      images: ['https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=300&h=200&fit=crop'],
      status: 'pending',
    }
  ]);

  const [newIncident, setNewIncident] = useState({
    type: 'other',
    severity: 'low',
    status: 'pending',
    images: []
  });
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedIncident, setSelectedIncident] = useState(null);
  const [resolution, setResolution] = useState('');

  const handleCreateIncident = () => {
    if (newIncident.title && newIncident.description) {
      const incident = {
        id: Math.random().toString(),
        petName: newIncident.petName || '',
        caregiverName: newIncident.caregiverName || '',
        date: new Date().toISOString().split('T')[0],
        time: new Date().toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' }),
        type: newIncident.type,
        severity: newIncident.severity,
        title: newIncident.title,
        description: newIncident.description,
        images: newIncident.images || [],
        status: 'pending'
      };
      setIncidents([incident, ...incidents]);
      setNewIncident({ type: 'other', severity: 'low', status: 'pending', images: [] });
      setIsDialogOpen(false);
    }
  };

  const handleResolveIncident = (incidentId) => {
    if (resolution.trim()) {
      setIncidents(incidents.map(incident => 
        incident.id === incidentId 
          ? {
              ...incident,
              status: 'resolved',
              resolution,
              resolvedBy: 'Sistema PetCare',
              resolvedDate: new Date().toISOString().split('T')[0]
            }
          : incident
      ));
      setSelectedIncident(null);
      setResolution('');
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'high': return 'bg-red-100 text-red-800 border-red-200';
      case 'medium': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'low': return 'bg-green-100 text-green-800 border-green-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'resolved': return 'bg-green-100 text-green-800 border-green-200';
      case 'in-progress': return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'pending': return 'bg-orange-100 text-orange-800 border-orange-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'resolved': return <CheckCircle className="w-4 h-4" />;
      case 'in-progress': return <Clock className="w-4 h-4" />;
      case 'pending': return <AlertTriangle className="w-4 h-4" />;
      default: return <Clock className="w-4 h-4" />;
    }
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="mb-2">Gestión de Incidentes</h1>
          <p className="text-muted-foreground">
            Reporta y gestiona incidentes durante los servicios de cuidado
          </p>
        </div>
        
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button className="flex items-center gap-2">
              <Plus className="w-4 h-4" />
              Reportar Incidente
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Reportar Nuevo Incidente</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="petName">Nombre de la Mascota</Label>
                  <Input
                    id="petName"
                    placeholder="Nombre de la mascota"
                    value={newIncident.petName || ''}
                    onChange={(e) => setNewIncident({ ...newIncident, petName: e.target.value })}
                  />
                </div>
                <div>
                  <Label htmlFor="caregiverName">Cuidador</Label>
                  <Input
                    id="caregiverName"
                    placeholder="Nombre del cuidador"
                    value={newIncident.caregiverName || ''}
                    onChange={(e) => setNewIncident({ ...newIncident, caregiverName: e.target.value })}
                  />
                </div>
              </div>
              
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="type">Tipo de Incidente</Label>
                  <Select value={newIncident.type} onValueChange={(value) => setNewIncident({ ...newIncident, type: value })}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="medical">Médico</SelectItem>
                      <SelectItem value="behavioral">Comportamental</SelectItem>
                      <SelectItem value="accident">Accidente</SelectItem>
                      <SelectItem value="other">Otro</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="severity">Severidad</Label>
                  <Select value={newIncident.severity} onValueChange={(value) => setNewIncident({ ...newIncident, severity: value })}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="low">Baja</SelectItem>
                      <SelectItem value="medium">Media</SelectItem>
                      <SelectItem value="high">Alta</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              
              <div>
                <Label htmlFor="title">Título del Incidente</Label>
                <Input
                  id="title"
                  placeholder="Breve descripción del incidente"
                  value={newIncident.title || ''}
                  onChange={(e) => setNewIncident({ ...newIncident, title: e.target.value })}
                />
              </div>
              
              <div>
                <Label htmlFor="description">Descripción Detallada</Label>
                <Textarea
                  id="description"
                  placeholder="Describe en detalle lo que ocurrió..."
                  rows={4}
                  value={newIncident.description || ''}
                  onChange={(e) => setNewIncident({ ...newIncident, description: e.target.value })}
                />
              </div>
              
              <div>
                <Label>Imágenes (Opcional)</Label>
                <div className="flex items-center gap-2 mt-2">
                  <Button variant="outline" className="flex items-center gap-2">
                    <Camera className="w-4 h-4" />
                    Subir Imagen
                  </Button>
                  <span className="text-sm text-muted-foreground">
                    Agrega imágenes como evidencia del incidente
                  </span>
                </div>
              </div>
              
              <div className="flex justify-end gap-2 pt-4">
                <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                  Cancelar
                </Button>
                <Button onClick={handleCreateIncident}>
                  Reportar Incidente
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {[
          {
            title: 'Total Incidentes',
            value: incidents.length,
            icon: <AlertTriangle className="w-8 h-8 text-orange-500" />,
          },
          {
            title: 'Pendientes',
            value: incidents.filter(i => i.status === 'pending').length,
            valueColor: 'text-orange-600',
            icon: <Clock className="w-8 h-8 text-orange-500" />,
          },
          {
            title: 'En Progreso',
            value: incidents.filter(i => i.status === 'in-progress').length,
            valueColor: 'text-blue-600',
            icon: <Clock className="w-8 h-8 text-blue-500" />,
          },
          {
            title: 'Resueltos',
            value: incidents.filter(i => i.status === 'resolved').length,
            valueColor: 'text-green-600',
            icon: <CheckCircle className="w-8 h-8 text-green-500" />,
          },
        ].map((card, idx) => (
          <StatsCard
            key={card.title}
            title={card.title}
            value={card.value}
            valueColor={card.valueColor}
            icon={card.icon}
          />
        ))}
      </div>

      {/* Incidents List */}
      <div className="space-y-6">
        {incidents.map((incident) => (
          <Card key={incident.id} className="overflow-hidden">
            <CardHeader className="pb-4">
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <CardTitle className="text-lg">{incident.title}</CardTitle>
                    <Badge className={getSeverityColor(incident.severity)}>
                      {incident.severity === 'high' ? 'Alta' : 
                       incident.severity === 'medium' ? 'Media' : 'Baja'}
                    </Badge>
                    <Badge className={getStatusColor(incident.status)} variant="outline">
                      <div className="flex items-center gap-1">
                        {getStatusIcon(incident.status)}
                        {incident.status === 'pending' ? 'Pendiente' :
                         incident.status === 'in-progress' ? 'En Progreso' : 'Resuelto'}
                      </div>
                    </Badge>
                  </div>
                  <CardDescription className="flex items-center gap-4">
                    <span><strong>Mascota:</strong> {incident.petName}</span>
                    <span><strong>Cuidador:</strong> {incident.caregiverName}</span>
                    <span><strong>Fecha:</strong> {incident.date} - {incident.time}</span>
                  </CardDescription>
                </div>
                
                {incident.status !== 'resolved' && (
                  <Dialog>
                    <DialogTrigger asChild>
                      <Button 
                        variant="outline" 
                        size="sm"
                        onClick={() => setSelectedIncident(incident)}
                      >
                        Resolver
                      </Button>
                    </DialogTrigger>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle>Resolver Incidente</DialogTitle>
                      </DialogHeader>
                      <div className="space-y-4">
                        <div>
                          <Label htmlFor="resolution">Resolución</Label>
                          <Textarea
                            id="resolution"
                            placeholder="Describe cómo se resolvió el incidente..."
                            rows={4}
                            value={resolution}
                            onChange={(e) => setResolution(e.target.value)}
                          />
                        </div>
                        <div className="flex justify-end gap-2">
                          <Button variant="outline" onClick={() => setSelectedIncident(null)}>
                            Cancelar
                          </Button>
                          <Button onClick={() => handleResolveIncident(incident.id)}>
                            Marcar como Resuelto
                          </Button>
                        </div>
                      </div>
                    </DialogContent>
                  </Dialog>
                )}
              </div>
            </CardHeader>
            
            <CardContent>
              <p className="text-muted-foreground mb-4">{incident.description}</p>
              
              {incident.images && incident.images.length > 0 && (
                <div className="mb-4">
                  <Label className="text-sm text-muted-foreground mb-2 block">Imágenes:</Label>
                  <div className="flex gap-2">
                    {incident.images.map((image, index) => (
                      <div key={index} className="relative">
                        <FallbackImage
                          src={image}
                          alt={`Evidencia ${index + 1}`}
                          className="w-24 h-24 object-cover rounded-lg border"
                        />
                      </div>
                    ))}
                  </div>
                </div>
              )}
              
              {incident.status === 'resolved' && incident.resolution && (
                <div className="bg-green-50 border border-green-200 rounded-lg p-4 mt-4">
                  <div className="flex items-center gap-2 mb-2">
                    <CheckCircle className="w-4 h-4 text-green-600" />
                    <span className="font-medium text-green-800">Resolución</span>
                  </div>
                  <p className="text-green-700 mb-2">{incident.resolution}</p>
                  <div className="text-sm text-green-600">
                    Resuelto por: {incident.resolvedBy} • {incident.resolvedDate}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}