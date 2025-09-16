import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../../../shared/ui/Card';
import { Button } from '../../../../shared/ui/Button';
import { Input } from '../../../../shared/ui/Input';
import { Label } from '../../../../shared/ui/Label';
import { Checkbox } from '../../../../shared/ui/Checkbox';
import { useState } from 'react';
import { updateProfile } from '../services/updateProfile';

export default function Profile({ user }) {
  const [form, setForm] = useState({
    name: user.name || '',
    phone: user.phone || '',
    location: {
      street: user.location?.street || '',
      number: user.location?.number || '',
      city: user.location?.city || '',
      province: user.location?.province || '',
      country: user.location?.country || '',
    },
    professionalRoles: user.professionalRoles || [],
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { id, value } = e.target;
    if (["street", "number", "city", "province", "country"].includes(id)) {
      setForm((prev) => ({
        ...prev,
        location: { ...prev.location, [id]: value },
      }));
    } else {
      setForm((prev) => ({ ...prev, [id.replace('profile', '').toLowerCase()]: value }));
    }
  };

  const handleRoleChange = (role) => {
    setForm((prev) => {
      const roles = prev.professionalRoles.includes(role)
        ? prev.professionalRoles.filter((r) => r !== role)
        : [...prev.professionalRoles, role];
      return { ...prev, professionalRoles: roles };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSuccess(false);
    setError(null);
    try {
      await updateProfile(form);
      setSuccess(true);
    } catch (err) {
      setError('Error al actualizar el perfil');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Información del Perfil</CardTitle>
        <CardDescription>Gestiona tu información como cuidador</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <Label htmlFor="profileName">Nombre</Label>
              <Input id="profileName" value={form.name} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="profilePhone">Teléfono</Label>
              <Input id="profilePhone" value={form.phone} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="street">Calle</Label>
              <Input id="street" value={form.location.street} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="number">Número</Label>
              <Input id="number" value={form.location.number} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="city">Ciudad</Label>
              <Input id="city" value={form.location.city} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="province">Provincia</Label>
              <Input id="province" value={form.location.province} onChange={handleChange} />
            </div>
            <div>
              <Label htmlFor="country">País</Label>
              <Input id="country" value={form.location.country} onChange={handleChange} />
            </div>
            <div>
              <Label>Roles profesionales</Label>
              <div className="grid grid-cols-2 gap-2 mt-2">
                {['PASEADOR', 'VETERINARIO', 'PELUQUERO', 'CUIDADOR'].map((role) => (
                  <div key={role} className="flex items-center space-x-2">
                    <Checkbox
                      id={role}
                      checked={form.professionalRoles.includes(role)}
                      onCheckedChange={() => handleRoleChange(role)}
                    />
                    <Label htmlFor={role}>
                      {role.charAt(0) + role.slice(1).toLowerCase()}
                    </Label>
                  </div>
                ))}
              </div>
            </div>
          </div>
          <Button type="submit" disabled={loading}>{loading ? 'Guardando...' : 'Guardar Cambios'}</Button>
          {success && <div className="text-green-600">¡Perfil actualizado!</div>}
          {error && <div className="text-red-600">{error}</div>}
        </form>
      </CardContent>
    </Card>
  );
}
