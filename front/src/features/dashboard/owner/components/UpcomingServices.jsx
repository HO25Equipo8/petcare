import { Card, CardHeader, CardTitle, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Plus } from 'lucide-react';

export default function UpcomingServicesCardOwner({ upcomingServices, onNavigate }) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle>Próximos Servicios</CardTitle>
        <Button variant="ghost" size="sm" onClick={() => onNavigate('booking')}>
          <Plus className="w-4 h-4" />
        </Button>
      </CardHeader>
      <CardContent className="space-y-4">
        {upcomingServices.map((service) => (
          <div key={service.id} className="p-4 border rounded-lg">
            <div className="flex justify-between items-start mb-2">
              <h4 className="font-medium">{service.service}</h4>
              <Badge variant={service.status === 'Programado' ? 'default' : 'secondary'}>
                {service.status}
              </Badge>
            </div>
            <p className="text-sm text-gray-600 mb-1">Para: {service.pet}</p>
            <p className="text-sm text-gray-600 mb-1">Cuidador: {service.caregiver}</p>
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-500">{service.time}</span>
              <span className="font-medium">{service.price}</span>
            </div>
          </div>
        ))}
        <Button 
          variant="outline" 
          className="w-full"
          onClick={() => onNavigate('booking')}
        >
          Ver Todas las Reservas
        </Button>
      </CardContent>
    </Card>
  );
}
