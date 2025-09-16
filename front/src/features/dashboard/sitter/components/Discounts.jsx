import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Input } from '../../../../shared/ui/Input';
import { Label } from '../../../../shared/ui/Label';
import { Plus, Trash2, Save, Percent } from 'lucide-react';

export default function Discounts({
  planDiscountRules,
  setIsEditingRule,
  updateDiscountRule,
  removeDiscountRule,
  saveDiscountRules
}) {
  return (
    <Card className="card-modern">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Percent className="h-5 w-5 text-accent-500" />
          Reglas de Descuento por Planes
        </CardTitle>
        <CardDescription>
          Configura descuentos automáticos basados en la frecuencia de sesiones semanales
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="mb-6 flex justify-between items-center">
          <div className="text-sm text-muted-foreground">
            Los descuentos se aplican automáticamente según las sesiones por semana del cliente
          </div>
          <Button 
            onClick={() => setIsEditingRule(true)} 
            className="bg-gradient-accent"
          >
            <Plus className="h-4 w-4 mr-2" />
            Nueva Regla
          </Button>
        </div>

        <div className="grid gap-4">
          {planDiscountRules.map((rule) => (
            <Card key={rule.id} className="border-l-4 border-l-accent-500">
              <CardContent className="p-4">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      <Badge className="bg-accent-50 text-accent-700 border border-accent-200">
                        {rule.category}
                      </Badge>
                      <Badge variant="outline" className="font-mono">
                        {rule.discount}% descuento
                      </Badge>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                      <div>
                        <Label className="text-xs">Sesiones Mínimas/Semana</Label>
                        <Input
                          type="number"
                          value={rule.minSessionsPerWeek}
                          onChange={(e) => updateDiscountRule(rule.id, 'minSessionsPerWeek', parseFloat(e.target.value))}
                          className="h-8"
                        />
                      </div>
                      <div>
                        <Label className="text-xs">Sesiones Máximas/Semana</Label>
                        <Input
                          type="number"
                          value={rule.maxSessionsPerWeek === 999 ? '' : rule.maxSessionsPerWeek}
                          placeholder="∞"
                          onChange={(e) => updateDiscountRule(rule.id, 'maxSessionsPerWeek', e.target.value ? parseFloat(e.target.value) : 999)}
                          className="h-8"
                        />
                      </div>
                      <div>
                        <Label className="text-xs">Descuento (%)</Label>
                        <Input
                          type="number"
                          min="0"
                          max="100"
                          value={rule.discount}
                          onChange={(e) => updateDiscountRule(rule.id, 'discount', parseFloat(e.target.value))}
                          className="h-8"
                        />
                      </div>
                    </div>
                    <div className="mt-2 text-xs text-muted-foreground">
                      Rango: {rule.minSessionsPerWeek} - {rule.maxSessionsPerWeek === 999 ? '∞' : rule.maxSessionsPerWeek} sesiones/semana
                    </div>
                  </div>
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => removeDiscountRule(rule.id)}
                      className="text-coral-600 hover:text-coral-700"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        <div className="mt-6 pt-4 border-t">
          <Button onClick={saveDiscountRules} className="bg-gradient-primary">
            <Save className="h-4 w-4 mr-2" />
            Guardar Configuración
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}
