import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../../../../shared/ui/Dialog';
import { Input } from '../../../../shared/ui/Input';
import { Label } from '../../../../shared/ui/Label';
import { Switch } from '../../../../shared/ui/Switch';
import { Settings } from 'lucide-react';

export default function Schedule({ schedule, isScheduleModalOpen, setIsScheduleModalOpen, handleScheduleUpdate }) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle>Configuración de Horarios</CardTitle>
          <CardDescription>Define tu disponibilidad semanal</CardDescription>
        </div>
        <Dialog open={isScheduleModalOpen} onOpenChange={setIsScheduleModalOpen}>
          <DialogTrigger asChild>
            <Button>
              <Settings className="w-4 h-4 mr-2" />
              Configuración Masiva
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Configuración Masiva de Horarios</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label>Hora de Inicio</Label>
                  <Input type="time" defaultValue="08:00" />
                </div>
                <div>
                  <Label>Hora de Fin</Label>
                  <Input type="time" defaultValue="18:00" />
                </div>
              </div>
              <div className="space-y-2">
                <Label>Aplicar a:</Label>
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <input type="checkbox" id="weekdays" defaultChecked />
                    <Label htmlFor="weekdays">Días de semana (L-V)</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <input type="checkbox" id="weekends" />
                    <Label htmlFor="weekends">Fines de semana (S-D)</Label>
                  </div>
                </div>
              </div>
              <Button onClick={() => setIsScheduleModalOpen(false)}>
                Aplicar Cambios
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {schedule.map((day, index) => (
            <div key={day.day} className="flex items-center justify-between p-4 border rounded-lg">
              <div className="flex items-center space-x-4">
                <Switch
                  checked={day.enabled}
                  onCheckedChange={(checked) => handleScheduleUpdate(index, 'enabled', checked)}
                />
                <span className="font-medium min-w-[80px]">{day.day}</span>
              </div>
              {day.enabled && (
                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-2">
                    <Input
                      type="time"
                      value={day.startTime}
                      onChange={(e) => handleScheduleUpdate(index, 'startTime', e.target.value)}
                      className="w-32"
                    />
                    <span className="text-muted-foreground">a</span>
                    <Input
                      type="time"
                      value={day.endTime}
                      onChange={(e) => handleScheduleUpdate(index, 'endTime', e.target.value)}
                      className="w-32"
                    />
                  </div>
                </div>
              )}
              {!day.enabled && (
                <span className="text-muted-foreground">No disponible</span>
              )}
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
