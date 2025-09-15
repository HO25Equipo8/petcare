import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../../../../shared/ui/Dialog';
import { Plus, Save } from 'lucide-react';
import { Label } from '../../../../shared/ui/Label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';
import { Input } from '../../../../shared/ui/Input';
import { Button } from '../../../../shared/ui/Button';

/**
 * Modal para crear una nueva regla de descuento.
 * @param {Object} props
 * @param {boolean} props.open - Si el modal está abierto.
 * @param {function} props.onOpenChange - Función para cambiar el estado de apertura.
 * @param {Object} props.newRule - Estado de la nueva regla.
 * @param {function} props.setNewRule - Setter para el estado de la nueva regla.
 * @param {function} props.addDiscountRule - Función para guardar la regla.
 */
export default function CreateDiscount({ open, onOpenChange, newRule, setNewRule, addDiscountRule }) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Plus className="h-5 w-5" />
            Nueva Regla de Descuento
          </DialogTitle>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div>
            <Label htmlFor="rule-category">Categoría de Cliente *</Label>
            <Select
              value={newRule.category}
              onValueChange={value => setNewRule({ ...newRule, category: value })}
            >
              <SelectTrigger>
                <SelectValue placeholder="Selecciona categoría" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="Cliente Nuevo">Cliente Nuevo</SelectItem>
                <SelectItem value="Cliente Regular">Cliente Regular</SelectItem>
                <SelectItem value="Cliente Frecuente">Cliente Frecuente</SelectItem>
                <SelectItem value="Cliente VIP">Cliente VIP</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label htmlFor="min-sessions">Sesiones Mín/Semana *</Label>
              <Input
                id="min-sessions"
                type="number"
                min="1"
                value={newRule.minSessionsPerWeek || ''}
                onChange={e => setNewRule({ ...newRule, minSessionsPerWeek: parseFloat(e.target.value) || 1 })}
              />
            </div>
            <div>
              <Label htmlFor="max-sessions">Sesiones Máx/Semana *</Label>
              <Input
                id="max-sessions"
                type="number"
                min="1"
                value={newRule.maxSessionsPerWeek === 999 ? '' : newRule.maxSessionsPerWeek || ''}
                placeholder="∞"
                onChange={e => setNewRule({ ...newRule, maxSessionsPerWeek: e.target.value ? parseFloat(e.target.value) : 999 })}
              />
            </div>
          </div>
          <div>
            <Label htmlFor="discount">Descuento (%) *</Label>
            <Input
              id="discount"
              type="number"
              min="0"
              max="100"
              value={newRule.discount || ''}
              onChange={e => setNewRule({ ...newRule, discount: parseFloat(e.target.value) || 0 })}
            />
          </div>
        </div>
        <div className="flex justify-end gap-3">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button onClick={addDiscountRule} className="bg-gradient-accent">
            <Save className="h-4 w-4 mr-2" />
            Crear Regla
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
