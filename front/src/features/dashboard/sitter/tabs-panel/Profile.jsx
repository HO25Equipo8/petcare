import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Input } from '../../../../shared/ui/Input';
import { Label } from '../../../../shared/ui/Label';
import { Textarea } from '../../../../shared/ui/Textarea';

export default function Profile({ user }) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Información del Perfil</CardTitle>
        <CardDescription>Gestiona tu información como cuidador</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <Label htmlFor="profileName">Nombre</Label>
            <Input id="profileName" defaultValue={user.name} />
          </div>
          <div>
            <Label htmlFor="profileEmail">Email</Label>
            <Input id="profileEmail" defaultValue={user.email} disabled />
          </div>
          <div>
            <Label htmlFor="profilePhone">Teléfono</Label>
            <Input id="profilePhone" defaultValue={user.phone} />
          </div>
          <div>
            <Label htmlFor="profileAddress">Dirección</Label>
            <Input id="profileAddress" defaultValue={user.address} />
          </div>
        </div>
        <div>
          <Label htmlFor="profileBio">Descripción</Label>
          <Textarea 
            id="profileBio" 
            placeholder="Cuéntanos sobre tu experiencia cuidando mascotas..."
            rows={4}
          />
        </div>
        <Button>Guardar Cambios</Button>
      </CardContent>
    </Card>
  );
}
