import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Badge } from '../../../../shared/ui/Badge';
import { Button } from '../../../../shared/ui/Button';
import { AlertTriangle } from 'lucide-react';

export default function IncidentsSubTabAdmin({ pendingIncidents, getSeverityColor, onNavigate }) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Incidentes del Sistema</CardTitle>
        <CardDescription>Supervisa y gestiona todos los incidentes reportados</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {pendingIncidents.map((incident) => (
            <Card key={incident.id} className="p-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-4">
                  <AlertTriangle className="w-5 h-5 text-orange-500" />
                  <div>
                    <h4 className="font-medium">{incident.title}</h4>
                    <p className="text-sm text-muted-foreground">
                      Mascota: {incident.pet} • Cuidador: {incident.sitter} • {incident.date}
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <Badge className={getSeverityColor(incident.severity)}>
                    {incident.severity === 'high' ? 'Alta' : 
                      incident.severity === 'medium' ? 'Media' : 'Baja'}
                  </Badge>
                  <Button size="sm" onClick={() => onNavigate('incidents')}>
                    Ver Detalles
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
