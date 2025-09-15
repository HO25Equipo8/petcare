import { Card, CardHeader, CardTitle, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Plus } from 'lucide-react';

export default function MyPetsCardOwner({ pets, onNavigate }) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle>Mis Mascotas</CardTitle>
        <Button variant="ghost" size="sm" onClick={() => onNavigate('pets')}>
          <Plus className="w-4 h-4" />
        </Button>
      </CardHeader>
      <CardContent className="space-y-4">
        {pets.map((pet) => (
          <div key={pet.id} className="flex items-center space-x-4 p-3 rounded-lg bg-gray-50">
            <div className="text-2xl">{pet.avatar}</div>
            <div className="flex-1">
              <h3 className="font-medium">{pet.name}</h3>
              <p className="text-sm text-gray-600">{pet.breed} • {pet.age}</p>
            </div>
            <Badge variant="secondary">{pet.type}</Badge>
          </div>
        ))}
        <Button 
          variant="outline" 
          className="w-full" 
          onClick={() => onNavigate('pets')}
        >
          Ver Todas las Mascotas
        </Button>
      </CardContent>
    </Card>
  );
}
