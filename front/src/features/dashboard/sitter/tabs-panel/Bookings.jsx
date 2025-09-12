import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Heart } from 'lucide-react';

export default function Bookings({ upcomingBookings }) {
  return (
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
  );
}
