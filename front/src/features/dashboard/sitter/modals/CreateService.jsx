import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../../../../shared/ui/Dialog';
import { Plus, Save } from 'lucide-react';
import { Label } from '../../../../shared/ui/Label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';
import { Textarea } from '../../../../shared/ui/Textarea';
import { Input } from '../../../../shared/ui/Input';
import { Checkbox } from '../../../../shared/ui/Checkbox';
import { Button } from '../../../../shared/ui/Button';

/**
 * Modal para crear un nuevo servicio en el sistema.
 * @param {Object} props
 * @param {boolean} props.open - Si el modal está abierto.
 * @param {function} props.onOpenChange - Función para cambiar el estado de apertura.
 * @param {Object} props.newService - Estado del nuevo servicio.
 * @param {function} props.setNewService - Setter para el estado del nuevo servicio.
 * @param {function} props.saveService - Función para guardar el servicio.
 */
export default function CreateService({ open, onOpenChange, newService, setNewService, saveService }) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Plus className="h-5 w-5" />
            Crear Nuevo Servicio
          </DialogTitle>
        </DialogHeader>
        <div className="grid gap-6 py-4">
          <div className="grid gap-4">
            <div>
              <Label htmlFor="service-name">Tipo de Servicio *</Label>
              <Select 
                value={newService.name} 
                onValueChange={value => setNewService({...newService, name: value})}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona el tipo de servicio" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PASEO">Paseo</SelectItem>
                  <SelectItem value="ASEO">Aseo</SelectItem>
                  <SelectItem value="GUARDERIA">Guardería</SelectItem>
                  <SelectItem value="VETERINARIA">Veterinaria</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label htmlFor="service-description">Descripción *</Label>
              <Textarea
                id="service-description"
                placeholder="Describe el servicio en detalle..."
                value={newService.description}
                onChange={(e) => setNewService({...newService, description: e.target.value})}
                rows={3}
              />
            </div>
            <div>
              <Label htmlFor="service-price">Precio Base (USD) *</Label>
              <Input
                id="service-price"
                type="number"
                placeholder="0"
                value={newService.basePrice || ''}
                onChange={(e) => setNewService({...newService, basePrice: parseFloat(e.target.value) || 0})}
              />
            </div>
            <div>
              <Label htmlFor="service-role">Rol Permitido *</Label>
              <Select 
                value={newService.allowedRole} 
                onValueChange={value => setNewService({...newService, allowedRole: value})}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona el rol" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PASEADOR">Paseador</SelectItem>
                  <SelectItem value="VETERINARIO">Veterinario</SelectItem>
                  <SelectItem value="PELUQUERO">Peluquero</SelectItem>
                  <SelectItem value="CUIDADOR">Cuidador</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Tipos de Mascota Aplicables</Label>
              <div className="grid grid-cols-2 gap-2 mt-2">
                {['PERRO', 'GATO', 'OTRO'].map((petType) => (
                  <div key={petType} className="flex items-center space-x-2">
                    <Checkbox
                      id={petType}
                      checked={newService.applicablePetTypes?.includes(petType)}
                      onCheckedChange={(checked) => {
                        const currentTypes = newService.applicablePetTypes || [];
                        if (checked) {
                          setNewService({
                            ...newService,
                            applicablePetTypes: [...currentTypes, petType]
                          });
                        } else {
                          setNewService({
                            ...newService,
                            applicablePetTypes: currentTypes.filter(type => type !== petType)
                          });
                        }
                      }}
                    />
                    <Label htmlFor={petType}>{petType}</Label>
                  </div>
                ))}
              </div>
            </div>
          </div>
          <div className="flex justify-end gap-3">
            <Button 
              variant="outline" 
              onClick={() => onOpenChange(false)}
            >
              Cancelar
            </Button>
            <Button 
              onClick={saveService}
              className="bg-gradient-primary"
            >
              <Save className="h-4 w-4 mr-2" />
              Crear Servicio
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
