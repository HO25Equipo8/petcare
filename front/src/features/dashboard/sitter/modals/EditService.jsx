import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../../../../shared/ui/Dialog';
import { Edit, Save } from 'lucide-react';
import { Label } from '../../../../shared/ui/Label';
import { Textarea } from '../../../../shared/ui/Textarea';
import { Input } from '../../../../shared/ui/Input';
import { Checkbox } from '../../../../shared/ui/Checkbox';
import { Button } from '../../../../shared/ui/Button';

/**
 * Modal para editar un servicio existente.
 * @param {Object} props
 * @param {boolean} props.open - Si el modal está abierto.
 * @param {function} props.onOpenChange - Función para cambiar el estado de apertura.
 * @param {Object|null} props.selectedService - Servicio seleccionado para editar.
 * @param {function} props.setSelectedService - Setter para el servicio seleccionado.
 * @param {function} props.updateService - Función para actualizar el servicio.
 * @param {function} props.getServiceNameSpanish - Función para obtener el nombre en español.
 */
export default function EditService({ open, onOpenChange, selectedService, setSelectedService, updateService, getServiceNameSpanish }) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Edit className="h-5 w-5" />
            Editar Servicio: {selectedService ? getServiceNameSpanish(selectedService.name) : ''}
          </DialogTitle>
        </DialogHeader>
        {selectedService && (
          <div className="grid gap-6 py-4">
            <div className="grid gap-4">
              <div>
                <Label htmlFor="edit-service-description">Descripción *</Label>
                <Textarea
                  id="edit-service-description"
                  value={selectedService.description}
                  onChange={(e) => setSelectedService({ ...selectedService, description: e.target.value })}
                  rows={3}
                />
              </div>
              <div>
                <Label htmlFor="edit-service-price">Precio Base (USD) *</Label>
                <Input
                  id="edit-service-price"
                  type="number"
                  value={selectedService.basePrice}
                  onChange={(e) => setSelectedService({ ...selectedService, basePrice: parseFloat(e.target.value) || 0 })}
                />
              </div>
              <div>
                <Label>Tipos de Mascota Aplicables</Label>
                <div className="grid grid-cols-2 gap-2 mt-2">
                  {['Perros', 'Gatos', 'Aves', 'Conejos', 'Roedores', 'Peces'].map((petType) => (
                    <div key={petType} className="flex items-center space-x-2">
                      <Checkbox
                        id={`edit-${petType}`}
                        checked={selectedService.applicablePetTypes.includes(petType)}
                        onCheckedChange={(checked) => {
                          if (checked) {
                            setSelectedService({
                              ...selectedService,
                              applicablePetTypes: [...selectedService.applicablePetTypes, petType]
                            });
                          } else {
                            setSelectedService({
                              ...selectedService,
                              applicablePetTypes: selectedService.applicablePetTypes.filter(type => type !== petType)
                            });
                          }
                        }}
                      />
                      <Label htmlFor={`edit-${petType}`}>{petType}</Label>
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
                onClick={updateService}
                className="bg-gradient-primary"
              >
                <Save className="h-4 w-4 mr-2" />
                Actualizar Servicio
              </Button>
            </div>
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}
