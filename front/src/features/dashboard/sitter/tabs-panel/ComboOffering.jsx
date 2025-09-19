import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Badge } from '../../../../shared/ui/Badge';
import { Plus, XCircle, CheckCircle, Trash2, Package } from 'lucide-react';

export default function ComboOffering({
  comboOfferings,
  setIsCreatingCombo,
  formatCurrency,
  getStatusColor,
  toggleComboStatus,
  removeCombo,
  availableServices
}) {
  return (
    <Card className="card-modern">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Package className="h-5 w-5 text-secondary-500" />
          Combos de Servicios
        </CardTitle>
        <CardDescription>
          Crea y gestiona combos atractivos de servicios con descuentos especiales
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="mb-4 flex justify-between items-center">
          <div className="text-sm text-muted-foreground">
            {comboOfferings.length} combos disponibles
          </div>
          <Button onClick={() => setIsCreatingCombo(true)} className="bg-gradient-secondary">
            <Plus className="h-4 w-4 mr-2" />
            Nuevo Combo
          </Button>
        </div>

        <div className="grid gap-4">
          {comboOfferings.map((combo) => (
            <Card key={combo.id} className="border-l-4 border-l-secondary-500">
              <CardContent className="p-4">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      <h4 className="font-medium">{combo.name}</h4>
                      <Badge className="bg-secondary-50 text-secondary-700 border border-secondary-200">
                        {combo.discount}% OFF
                      </Badge>
                      <Badge 
                        className={combo.active ? getStatusColor('active') : getStatusColor('suspended')}
                      >
                        {combo.active ? 'Activo' : 'Inactivo'}
                      </Badge>
                    </div>
                    <p className="text-sm text-muted-foreground mb-3">
                      {combo.description}
                    </p>
                    <div className="flex items-center gap-4 text-sm">
                      <div className="flex items-center gap-1">
                        <span className="text-muted-foreground">Servicios:</span>
                        <span className="font-mono">{combo.offeringIds.length}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-muted-foreground line-through">
                          {formatCurrency(combo.originalPrice)}
                        </span>
                        <span className="font-mono text-primary-600">
                          {formatCurrency(combo.finalPrice)}
                        </span>
                      </div>
                      <div className="text-xs text-primary-600 bg-primary-50 px-2 py-1 rounded">
                        Ahorro: {formatCurrency(combo.originalPrice - combo.finalPrice)}
                      </div>
                    </div>
                    <div className="mt-2 flex flex-wrap gap-1">
                      {combo.offeringIds.map((offeringId) => {
                        const service = availableServices.find(s => s.id === offeringId.toString());
                        return service ? (
                          <Badge key={offeringId} variant="outline" className="text-xs">
                            {service.icon} {service.name}
                          </Badge>
                        ) : null;
                      })}
                    </div>
                  </div>
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => toggleComboStatus(combo.id)}
                    >
                      {combo.active ? <XCircle className="h-4 w-4" /> : <CheckCircle className="h-4 w-4" />}
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => removeCombo(combo.id)}
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
      </CardContent>
    </Card>
  );
}
