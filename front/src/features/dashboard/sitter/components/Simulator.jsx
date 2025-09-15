import { useState, useCallback } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../../shared/ui/Select';
import { Badge } from '../../../../shared/ui/Badge';
import { Alert, AlertDescription } from '../../../../shared/ui/Alert';
import { Calculator, CheckCircle, AlertTriangle, DollarSign, Settings, TrendingUp } from 'lucide-react';

// Mock data para simulación, reemplazar por petición
const mockServices = [
  { id: 'walk-basic', name: 'Paseo Básico (30 min)', price: 1500 },
  { id: 'walk-premium', name: 'Paseo Premium (60 min)', price: 2500 },
  { id: 'daycare-half', name: 'Guardería Media Jornada', price: 3500 },
  { id: 'daycare-full', name: 'Guardería Jornada Completa', price: 6000 },
  { id: 'grooming-basic', name: 'Lavado Básico', price: 2000 },
  { id: 'grooming-premium', name: 'Lavado Premium + SPA', price: 4500 },
  { id: 'training-basic', name: 'Entrenamiento Básico', price: 3000 },
  { id: 'vet-checkup', name: 'Consulta Veterinaria', price: 4000 },
];

const mockCombos = [
  { id: 'none', name: 'Sin Combo', discount: 0 },
  { id: 'walk-groom', name: 'Paseo + Lavado', discount: 15 },
  { id: 'daycare-groom', name: 'Guardería + Lavado', discount: 20 },
  { id: 'premium-pack', name: 'Pack Premium Completo', discount: 25 },
  { id: 'weekly-care', name: 'Cuidado Semanal', discount: 18 },
  { id: 'monthly-plan', name: 'Plan Mensual', discount: 30 },
];

const mockPlanCategories = [
  { id: 'basic', name: 'Plan Básico', businessDiscount: 5 },
  { id: 'premium', name: 'Plan Premium', businessDiscount: 10 },
  { id: 'vip', name: 'Plan VIP', businessDiscount: 15 },
  { id: 'corporate', name: 'Plan Corporativo', businessDiscount: 20 },
];

export default function Simulator() {
  const [simulationData, setSimulationData] = useState({
    serviceId: '',
    comboId: '',
    planCategory: '',
  });

  const [results, setResults] = useState(null);
  const [isSimulating, setIsSimulating] = useState(false);

  const calculateSimulation = useCallback(() => {
    if (!simulationData.serviceId || !simulationData.comboId || !simulationData.planCategory) {
      return;
    }

    setIsSimulating(true);

    // Demora de cálculo simulado
    setTimeout(() => {
      const service = mockServices.find(s => s.id === simulationData.serviceId);
      const combo = mockCombos.find(c => c.id === simulationData.comboId);
      const plan = mockPlanCategories.find(p => p.id === simulationData.planCategory);

      if (!service || !combo || !plan) {
        setIsSimulating(false);
        return;
      }

      const baseServicePrice = service.price;
      const comboPrice = baseServicePrice * 1.5; // Precio estimado del combo
      const comboDiscountAmount = (comboPrice * combo.discount) / 100;
      const totalBeforeRules = comboPrice - comboDiscountAmount;
      const businessRuleDiscountAmount = (totalBeforeRules * plan.businessDiscount) / 100;
      const finalPrice = totalBeforeRules - businessRuleDiscountAmount;
      // Calcular margen de ganancia (asumiendo costo del 60%)
      const estimatedCost = baseServicePrice * 0.6;
      const profitMargin = ((finalPrice - estimatedCost) / finalPrice) * 100;
      const isViable = profitMargin >= 25; // Margen mínimo del 25%
      let recommendation = '';
      if (isViable) {
        if (profitMargin >= 40) {
          recommendation = '¡Excelente! El margen de ganancia es muy saludable para el negocio.';
        } else if (profitMargin >= 30) {
          recommendation = 'Buena rentabilidad. El precio final es viable para el negocio.';
        } else {
          recommendation = 'Rentabilidad aceptable. Considerá monitorear este combo regularmente.';
        }
      } else {
        if (profitMargin < 15) {
          recommendation = 'Precio demasiado bajo. Considerá reducir descuentos o ajustar la estrategia de combo.';
        } else {
          recommendation = 'Margen bajo. Recomendamos ajustar el combo o las reglas de descuento.';
        }
      }
      const calculatedResults = {
        baseServicePrice,
        comboPrice,
        comboDiscount: comboDiscountAmount,
        businessRuleDiscount: businessRuleDiscountAmount,
        totalBeforeRules,
        finalPrice,
        isViable,
        recommendation,
        profitMargin,
      };
      setResults(calculatedResults);
      setIsSimulating(false);
    }, 800);
  }, [simulationData]);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-AR', {
      style: 'currency',
      currency: 'ARS',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  const resetSimulation = () => {
    setSimulationData({
      serviceId: '',
      comboId: '',
      planCategory: '',
    });
    setResults(null);
  };

  const selectedService = mockServices.find(s => s.id === simulationData.serviceId);
  const selectedCombo = mockCombos.find(c => c.id === simulationData.comboId);
  const selectedPlan = mockPlanCategories.find(p => p.id === simulationData.planCategory);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-gradient-primary rounded-lg">
            <Calculator className="h-6 w-6 text-white" />
          </div>
          <div>
            <h2 className="font-headings">Simulación de Planes y Reservas</h2>
            <p className="text-body text-neutral-600">
              Simula el impacto financiero de descuentos en servicios y combos
            </p>
          </div>
        </div>
        
        <Button 
          variant="outline" 
          onClick={resetSimulation}
          className="text-neutral-600 hover:text-neutral-800"
        >
          <Settings className="h-4 w-4 mr-2" />
          Limpiar
        </Button>
      </div>

      {/* Main Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Left Column - Inputs */}
        <Card className="card-modern">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <DollarSign className="h-5 w-5 text-primary-500" />
              Parámetros de Simulación
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            {/* Service Selector */}
            <div className="space-y-2">
              <Select 
                value={simulationData.serviceId} 
                onValueChange={(value) => setSimulationData(prev => ({ ...prev, serviceId: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona un servicio..." />
                </SelectTrigger>
                <SelectContent>
                  {mockServices.map((service) => (
                    <SelectItem key={service.id} value={service.id}>
                      <div className="flex justify-between items-center w-full">
                        <span>{service.name}</span>
                        <span className="text-metadata text-primary-600 ml-4">
                          {formatCurrency(service.price)}
                        </span>
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              
              {selectedService && (
                <div className="text-metadata text-neutral-600">
                  Precio base: {formatCurrency(selectedService.price)}
                </div>
              )}
            </div>

            {/* Combo Selector */}
            <div className="space-y-2">              
              <Select 
                value={simulationData.comboId} 
                onValueChange={(value) => setSimulationData(prev => ({ ...prev, comboId: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona un combo..." />
                </SelectTrigger>
                <SelectContent>
                  {mockCombos.map((combo) => (
                    <SelectItem key={combo.id} value={combo.id}>
                      <div className="flex justify-between items-center w-full">
                        <span>{combo.name}</span>
                        {combo.discount > 0 && (
                          <Badge variant="secondary" className="ml-4">
                            -{combo.discount}%
                          </Badge>
                        )}
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              
              {selectedCombo && selectedCombo.discount > 0 && (
                <div className="text-metadata text-secondary-600">
                  Descuento de combo: {selectedCombo.discount}%
                </div>
              )}
            </div>

            {/* Plan Category Selector */}
            <div className="space-y-2">
              <Select 
                value={simulationData.planCategory} 
                onValueChange={(value) => setSimulationData(prev => ({ ...prev, planCategory: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona una categoría..." />
                </SelectTrigger>
                <SelectContent>
                  {mockPlanCategories.map((plan) => (
                    <SelectItem key={plan.id} value={plan.id}>
                      <div className="flex justify-between items-center w-full">
                        <span>{plan.name}</span>
                        <Badge variant="outline" className="ml-4">
                          -{plan.businessDiscount}%
                        </Badge>
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              
              {selectedPlan && (
                <div className="text-metadata text-accent-600">
                  Descuento por regla: {selectedPlan.businessDiscount}%
                </div>
              )}
            </div>

            {/* Simulate Button */}
            <Button 
              onClick={calculateSimulation}
              disabled={!simulationData.serviceId || !simulationData.comboId || !simulationData.planCategory || isSimulating}
              className="w-full btn-primary-modern"
            >
              <Calculator className="h-4 w-4 mr-2" />
              {isSimulating ? 'Simulando...' : 'Simular Reserva'}
            </Button>
          </CardContent>
        </Card>

        {/* Right Column - Results */}
        <Card className="card-modern">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="h-5 w-5 text-secondary-500" />
              Resultados de Simulación
            </CardTitle>
          </CardHeader>
          <CardContent>
            {!results ? (
              <div className="flex flex-col items-center justify-center py-12 text-center">
                <div className="p-4 bg-neutral-100 rounded-full mb-4">
                  <Calculator className="h-8 w-8 text-neutral-400" />
                </div>
                <h3 className="text-heading text-neutral-600 mb-2">
                  Simulación Pendiente
                </h3>
                <p className="text-body text-neutral-500">
                  Completa los campos del formulario y presiona "Simular Reserva" para ver los resultados
                </p>
              </div>
            ) : (
              <div className="space-y-6">
                {/* Price Breakdown */}
                <div className="space-y-4">
                  <h4 className="text-heading">Desglose de Precios</h4>
                  
                  <div className="space-y-3">
                    <div className="flex justify-between items-center p-3 bg-neutral-50 rounded-lg">
                      <span className="text-body-medium">Precio base del servicio</span>
                      <span className="text-metadata">{formatCurrency(results.baseServicePrice)}</span>
                    </div>
                    
                    <div className="flex justify-between items-center p-3 bg-neutral-50 rounded-lg">
                      <span className="text-body-medium">Precio del combo</span>
                      <span className="text-metadata">{formatCurrency(results.comboPrice)}</span>
                    </div>
                    
                    <div className="flex justify-between items-center p-3 bg-secondary-50 rounded-lg">
                      <span className="text-body-medium text-secondary-700">Descuento de combo</span>
                      <span className="text-metadata text-secondary-600">
                        -{formatCurrency(results.comboDiscount)}
                      </span>
                    </div>
                    
                    <div className="flex justify-between items-center p-3 bg-accent-50 rounded-lg">
                      <span className="text-body-medium text-accent-700">Descuento por regla</span>
                      <span className="text-metadata text-accent-600">
                        -{formatCurrency(results.businessRuleDiscount)}
                      </span>
                    </div>
                  </div>
                </div>

                {/* Final Price */}
                <div className="p-4 bg-gradient-to-r from-primary-50 to-secondary-50 rounded-lg border border-primary-200">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-heading">Precio Final</span>
                    <div className="flex items-center gap-2">
                      {results.isViable ? (
                        <CheckCircle className="h-5 w-5 text-primary-500" />
                      ) : (
                        <AlertTriangle className="h-5 w-5 text-coral-500" />
                      )}
                      <span className={`text-2xl font-bold ${
                        results.isViable ? 'text-primary-600' : 'text-coral-600'
                      }`}>
                        {formatCurrency(results.finalPrice)}
                      </span>
                    </div>
                  </div>
                  
                  <div className="flex justify-between items-center text-sm">
                    <span className="text-neutral-600">Margen de ganancia</span>
                    <Badge 
                      variant={results.isViable ? "" : "destructive"}
                      className={results.isViable ? "bg-primary-500" : "bg-coral-500"}
                    >
                      {results.profitMargin.toFixed(1)}%
                    </Badge>
                  </div>
                </div>

                {/* Recommendation */}
                <Alert className={results.isViable ? "border-primary-200 bg-primary-50" : "border-coral-200 bg-coral-50"}>
                  <div className="flex items-start gap-3">
                    {results.isViable ? (
                      <CheckCircle className="h-5 w-5 text-primary-500 mt-0.5" />
                    ) : (
                      <AlertTriangle className="h-5 w-5 text-coral-500 mt-0.5" />
                    )}
                    <div>
                      <h4 className={`font-medium mb-1 ${results.isViable ? 'text-primary-700' : 'text-coral-700'}`}>
                        {results.isViable ? 'Precio Viable' : 'Atención Requerida'}
                      </h4>
                      <AlertDescription className={results.isViable ? 'text-primary-600' : 'text-coral-600'}>
                        {results.recommendation}
                      </AlertDescription>
                    </div>
                  </div>
                </Alert>

                {/* Action Button */}
                {!results.isViable && (
                  <Button 
                    variant="outline" 
                    className="w-full border-coral-300 text-coral-600 hover:bg-coral-50"
                  >
                    <Settings className="h-4 w-4 mr-2" />
                    Ajustar Combo
                  </Button>
                )}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
