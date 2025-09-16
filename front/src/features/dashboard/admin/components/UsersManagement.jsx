import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Input } from '../../../../shared/ui/Input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../../../../shared/ui/Table';
import { Eye, Edit, Ban, CheckCircle } from 'lucide-react';

export default function UsersManagementSubTabAdmin({ recentUsers, getStatusColor, setSelectedUser, setIsUserModalOpen, handleUserAction }) {
  return (
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
  );
}
