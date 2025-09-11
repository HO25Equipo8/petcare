import { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Input } from '../components/ui/Input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/Select';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/Table';
import { Filter, Plus, Search, Edit, XCircle, CheckCircle, Trash2, Package } from 'lucide-react';

export default function ServicesManagementSubTabSitter({
  serviceSearchTerm,
  setServiceSearchTerm,
  serviceStatusFilter,
  setServiceStatusFilter,
  serviceRoleFilter,
  setServiceRoleFilter,
  filteredServices,
  getServiceIcon,
  getServiceNameSpanish,
  formatCurrency,
  getRoleNameSpanish,
  getStatusColor,
  setSelectedService,
  setIsServiceModalOpen,
  handleServiceAction,
  removeService,
  setIsCreatingService
}) {
  return (
    <Card className="card-modern">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Package className="h-5 w-5 text-secondary-500" />
          Gestión de Servicios del Sistema
        </CardTitle>
        <CardDescription>
          Administra los servicios principales de la plataforma: Paseo, Aseo, Guardería y Veterinaria
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="mb-4 flex justify-between items-center gap-4">
          <div className="flex gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-neutral-400" />
              <Input 
                placeholder="Buscar servicios..." 
                className="pl-10 w-64"
                value={serviceSearchTerm}
                onChange={(e) => setServiceSearchTerm(e.target.value)}
              />
            </div>
            <Select value={serviceStatusFilter} onValueChange={setServiceStatusFilter}>
              <SelectTrigger className="w-40">
                <SelectValue placeholder="Estado" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todos</SelectItem>
                <SelectItem value="active">Activos</SelectItem>
                <SelectItem value="inactive">Inactivos</SelectItem>
              </SelectContent>
            </Select>
            <Select value={serviceRoleFilter} onValueChange={setServiceRoleFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Rol permitido" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todos los roles</SelectItem>
                <SelectItem value="PASEADOR">Paseador</SelectItem>
                <SelectItem value="VETERINARIO">Veterinario</SelectItem>
                <SelectItem value="PELUQUERO">Peluquero</SelectItem>
                <SelectItem value="CUIDADOR">Cuidador</SelectItem>
                <SelectItem value="ADMINISTRADOR">Administrador</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div className="flex gap-2">
            <Badge variant="outline" className="flex items-center gap-1">
              <Filter className="h-3 w-3" />
              {filteredServices.length} servicios
            </Badge>
            <Button onClick={() => setIsCreatingService(true)} className="bg-gradient-primary">
              <Plus className="h-4 w-4 mr-2" />
              Nuevo Servicio
            </Button>
          </div>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Servicio</TableHead>
              <TableHead>Precio Base</TableHead>
              <TableHead>Rol Permitido</TableHead>
              <TableHead>Tipos de Mascota</TableHead>
              <TableHead>Estado</TableHead>
              <TableHead>Última Actualización</TableHead>
              <TableHead>Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredServices.map((service) => (
              <TableRow key={service.id}>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-secondary-100 rounded-lg flex items-center justify-center text-secondary-600">
                      {getServiceIcon(service.name)}
                    </div>
                    <div>
                      <p className="font-medium">{getServiceNameSpanish(service.name)}</p>
                      <p className="text-sm text-muted-foreground line-clamp-1">
                        {service.description}
                      </p>
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <div className="font-mono">
                    {formatCurrency(service.basePrice)}
                  </div>
                </TableCell>
                <TableCell>
                  <Badge variant="outline" className="bg-accent-50 text-accent-700 border-accent-200">
                    {getRoleNameSpanish(service.allowedRole)}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className="flex flex-wrap gap-1">
                    {service.applicablePetTypes.slice(0, 2).map((petType) => (
                      <Badge key={petType} variant="outline" className="text-xs">
                        {petType}
                      </Badge>
                    ))}
                    {service.applicablePetTypes.length > 2 && (
                      <Badge variant="outline" className="text-xs">
                        +{service.applicablePetTypes.length - 2} más
                      </Badge>
                    )}
                  </div>
                </TableCell>
                <TableCell>
                  <Badge className={service.active ? getStatusColor('active') : getStatusColor('suspended')}>
                    {service.active ? 'Activo' : 'Inactivo'}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className="text-xs text-muted-foreground">
                    {new Date(service.updatedAt).toLocaleDateString('es-AR')}
                  </div>
                </TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => {
                        setSelectedService(service);
                        setIsServiceModalOpen(true);
                      }}
                    >
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => handleServiceAction(
                        service.id, 
                        service.active ? 'deactivate' : 'activate'
                      )}
                    >
                      {service.active ? <XCircle className="h-4 w-4" /> : <CheckCircle className="h-4 w-4" />}
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => removeService(service.id)}
                      className="text-coral-600 hover:text-coral-700"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
