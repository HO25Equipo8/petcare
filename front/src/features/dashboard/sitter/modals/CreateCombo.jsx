import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../../../../shared/ui/Dialog';
import { Plus, Save } from 'lucide-react';
import { Label } from '../../../../shared/ui/Label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';
import { Textarea } from '../../../../shared/ui/Textarea';
import { Input } from '../../../../shared/ui/Input';
import { Checkbox } from '../../../../shared/ui/Checkbox';
import { Button } from '../../../../shared/ui/Button';

/**
 * Modal para crear un nuevo combo de servicios.
 * @param {Object} props
 * @param {boolean} props.open - Si el modal está abierto.
 * @param {function} props.onOpenChange - Función para cambiar el estado de apertura.
 * @param {Object} props.newCombo - Estado del nuevo combo.
 * @param {function} props.setNewCombo - Setter para el estado del nuevo combo.
 * @param {function} props.saveCombo - Función para guardar el combo.
 * @param {Array} props.availableServices - Servicios disponibles para seleccionar.
 * @param {function} props.handleOfferingToggle - Función para alternar servicios incluidos.
 * @param {function} props.handleComboDiscountChange - Función para cambiar el descuento.
 * @param {function} props.formatCurrency - Función para formatear moneda.
 */
export default function CreateCombo({
  open,
  onOpenChange,
  newCombo,
  setNewCombo,
  saveCombo,
  availableServices,
  handleOfferingToggle,
  handleComboDiscountChange,
  formatCurrency
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Plus className="h-5 w-5" />
            Crear Nuevo Combo
          </DialogTitle>
        </DialogHeader>
        <div className="grid gap-6 py-4">
          <div className="grid gap-4">
            <div>
              <Label htmlFor="combo-name">Nombre del Combo *</Label>
              <Select
                value={newCombo.name}
                onValueChange={value => setNewCombo({ ...newCombo, name: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona el nombre del combo" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Pack Básico">Pack Básico</SelectItem>
                  <SelectItem value="Pack Premium">Pack Premium</SelectItem>
                  <SelectItem value="Pack Completo">Pack Completo</SelectItem>
                  <SelectItem value="Pack Ejercicio">Pack Ejercicio</SelectItem>
                  <SelectItem value="Pack Cuidado">Pack Cuidado</SelectItem>
                  <SelectItem value="Pack Wellness">Pack Wellness</SelectItem>
                  <SelectItem value="Pack Familiar">Pack Familiar</SelectItem>
                  <SelectItem value="Pack Deportivo">Pack Deportivo</SelectItem>
                  <SelectItem value="Pack Relax">Pack Relax</SelectItem>
                  <SelectItem value="Pack Social">Pack Social</SelectItem>
                  <SelectItem value="Pack Aventura">Pack Aventura</SelectItem>
                  <SelectItem value="Pack Urbano">Pack Urbano</SelectItem>
                  <SelectItem value="Pack Especial">Pack Especial</SelectItem>
                  <SelectItem value="Pack Personalizado">Pack Personalizado</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label htmlFor="combo-description">Descripción *</Label>
              <Textarea
                id="combo-description"
                placeholder="Ej: Combo ideal para mascotas activas que incluye paseo y baño..."
                value={newCombo.description}
                onChange={e => setNewCombo({ ...newCombo, description: e.target.value })}
                rows={3}
              />
              <div className="text-xs text-muted-foreground mt-1">
                Describe los beneficios y características del combo
              </div>
            </div>
            <div>
              <Label htmlFor="combo-discount">Descuento (%) *</Label>
              <Input
                id="combo-discount"
                type="number"
                min="0"
                max="100"
                placeholder="15"
                value={newCombo.discount || ''}
                onChange={e => handleComboDiscountChange(parseFloat(e.target.value) || 0)}
              />
            </div>
            <div>
              <Label>Servicios Incluidos *</Label>
              <div className="grid grid-cols-1 gap-2 mt-2 max-h-48 overflow-y-auto border rounded-md p-3">
                {availableServices.map((service, index) => (
                  <div key={service.id} className="flex items-center space-x-2">
                    <Checkbox
                      id={`combo-service-${service.id}`}
                      checked={newCombo.offeringIds?.includes(index + 1)}
                      onCheckedChange={() => handleOfferingToggle(index + 1)}
                    />
                    <Label
                      htmlFor={`combo-service-${service.id}`}
                      className="flex items-center gap-2 cursor-pointer flex-1"
                    >
                      <span>{service.icon}</span>
                      <span>{service.name}</span>
                      <span className="text-sm text-muted-foreground">
                        ({formatCurrency(service.price)})
                      </span>
                    </Label>
                  </div>
                ))}
              </div>
            </div>
            {newCombo.offeringIds && newCombo.offeringIds.length > 0 && (
              <div className="bg-secondary-50 p-4 rounded-md">
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span className="text-muted-foreground">Precio Original:</span>
                    <div className="font-mono text-lg">{formatCurrency(newCombo.originalPrice || 0)}</div>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Precio Final:</span>
                    <div className="font-mono text-lg text-primary-600">{formatCurrency(newCombo.finalPrice || 0)}</div>
                  </div>
                </div>
                <div className="mt-2 text-xs text-primary-600">
                  Ahorro total: {formatCurrency((newCombo.originalPrice || 0) - (newCombo.finalPrice || 0))}
                </div>
              </div>
            )}
          </div>
          <div className="flex justify-end gap-3">
            <Button
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancelar
            </Button>
            <Button
              onClick={saveCombo}
              className="bg-gradient-secondary"
            >
              <Save className="h-4 w-4 mr-2" />
              Crear Combo
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
