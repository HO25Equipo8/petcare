import { Button } from '../../../../shared/ui/Button';
import { Heart, MapPin, Calendar, Star, AlertTriangle } from 'lucide-react';

/**
 * QuickActionsPanelOwner - Panel de acciones rápidas para OwnerDashboard
 * @param {Object} props
 * @param {function} props.onNavigate - Función para navegar a la sección correspondiente
 */
export default function QuickActionsPanelOwner({ onNavigate }) {
  const actions = [
    {
      key: 'tracking',
      icon: <MapPin className="w-6 h-6" />, 
      label: 'Ver Tracking',
    },
    {
      key: 'booking',
      icon: <Calendar className="w-6 h-6" />, 
      label: 'Nueva Reserva',
    },
    {
      key: 'caregivers',
      icon: <Star className="w-6 h-6" />, 
      label: 'Buscar Cuidador',
    },
    {
      key: 'pets',
      icon: <Heart className="w-6 h-6" />, 
      label: 'Agregar Mascota',
    },
    {
      key: 'incidents',
      icon: <AlertTriangle className="w-6 h-6" />, 
      label: 'Gestionar Incidentes',
    },
  ];

  return (
    <div className="mt-8">
      <h2 className="text-xl mb-4">Acciones Rápidas</h2>
      <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
        {actions.map(action => (
          <Button
            key={action.key}
            variant="outline"
            className="h-20 flex flex-col space-y-2"
            onClick={() => onNavigate(action.key)}
          >
            {action.icon}
            <span>{action.label}</span>
          </Button>
        ))}
      </div>
    </div>
  );
}
