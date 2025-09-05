import { Card, CardHeader, CardTitle, CardDescription, CardContent } from './ui/Card.jsx';
import { Button } from './ui/Button.jsx';
import { Badge } from './ui/Badge.jsx';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from './ui/Dialog.jsx';
import { Input } from './ui/Input.jsx';
import { Label } from './ui/Label.jsx';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/Select.jsx';
import { Plus, MapPin } from 'lucide-react';

export default function ServiceZoneSubTabSitter({
  serviceZones,
  isZoneModalOpen,
  setIsZoneModalOpen,
  newZone,
  setNewZone,
  handleAddServiceZone,
  toggleZoneStatus
}) {
  return (
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
  );
}
