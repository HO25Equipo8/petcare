import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../../../../shared/ui/Dialog';
import { Label } from '../../../../shared/ui/Label';
import { Badge } from '../../../../shared/ui/Badge';
import { Button } from '../../../../shared/ui/Button';

export default function UserDetailsModal({ open, onOpenChange, selectedUser, getStatusColor }) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
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
  );
}
