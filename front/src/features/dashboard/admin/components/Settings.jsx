import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Input } from '../../../../shared/ui/Input';
import { Label } from '../../../../shared/ui/Label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';

export default function SystemSettingsSubTabAdmin() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Configuración del Sistema</CardTitle>
        <CardDescription>Ajusta los parámetros globales de la plataforma</CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="space-y-4">
            <div>
              <Label>Comisión de la plataforma (%)</Label>
              <Input type="number" defaultValue="10" min="0" max="50" />
            </div>
            <div>
              <Label>Tiempo mínimo de reserva (horas)</Label>
              <Input type="number" defaultValue="1" min="1" max="24" />
            </div>
            <div>
              <Label>Radio máximo de servicio (km)</Label>
              <Input type="number" defaultValue="15" min="1" max="50" />
            </div>
          </div>
          <div className="space-y-4">
            <div>
              <Label>Tiempo límite de cancelación (horas)</Label>
              <Input type="number" defaultValue="24" min="1" max="168" />
            </div>
            <div>
              <Label>Calificación mínima para cuidadores</Label>
              <Input type="number" defaultValue="4.0" min="1" max="5" step="0.1" />
            </div>
            <div>
              <Label>Verificación obligatoria de identidad</Label>
              <Select defaultValue="required">
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="required">Obligatoria</SelectItem>
                  <SelectItem value="optional">Opcional</SelectItem>
                  <SelectItem value="disabled">Deshabilitada</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </div>
        <Button>Guardar Configuración</Button>
      </CardContent>
    </Card>
  );
}
