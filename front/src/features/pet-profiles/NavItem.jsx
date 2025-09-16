import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '../../shared/ui/Card';
import { Button } from '../../shared/ui/Button';
import { Badge } from '../../shared/ui/Badge';
import { Input } from '../../shared/ui/Input';
import { Label } from '../../shared/ui/Label';
import { Textarea } from '../../shared/ui/Textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../shared/ui/Select';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../../shared/ui/Dialog';
import { Plus, Edit, Calendar, Heart, Weight, Info } from 'lucide-react';



export function PetProfiles({ onNavigate }) {
  const [pets, setPets] = useState([
    {
      id: 1,
      name: 'Luna',
      type: 'Perro',
      breed: 'Golden Retriever',
      age: '3 años',
      weight: '28 kg',
      size: 'Grande',
      avatar: '🐕',
      color: 'Dorado',
      birthDate: '2021-03-15',
      vaccinated: true,
      spayed: true,
      microchip: 'MX123456789',
      allergies: 'Ninguna conocida',
      medications: 'Ninguna',
      specialNeeds: 'Le gusta jugar con pelotas',
      emergencyContact: 'Dr. Fernández - (555) 987-6543'
    },
    {
      id: 2,
      name: 'Milo',
      type: 'Gato',
      breed: 'Siamés',
      age: '2 años',
      weight: '4.5 kg',
      size: 'Mediano',
      avatar: '🐱',
      color: 'Seal Point',
      birthDate: '2022-07-20',
      vaccinated: true,
      spayed: false,
      microchip: 'MX987654321',
      allergies: 'Pollo',
      medications: 'Ninguna',
      specialNeeds: 'Muy sociable, le gusta estar con otros gatos',
      emergencyContact: 'Dr. Fernández - (555) 987-6543'
    }
  ]);

  const [isAddingPet, setIsAddingPet] = useState(false);
  const [editingPet, setEditingPet] = useState(null);
  const [newPet, setNewPet] = useState({
    name: '',
    type: '',
    breed: '',
    age: '',
    weight: '',
    size: '',
    color: '',
    birthDate: '',
    vaccinated: false,
    spayed: false,
    microchip: '',
    allergies: '',
    medications: '',
    specialNeeds: '',
    emergencyContact: ''
  });

  const handleAddPet = () => {
    const pet = {
      id: pets.length + 1,
      ...newPet,
      avatar: newPet.type === 'Perro' ? '🐕' : newPet.type === 'Gato' ? '🐱' : '🐾'
    };
    setPets([...pets, pet]);
    setNewPet({
      name: '',
      type: '',
      breed: '',
      age: '',
      weight: '',
      size: '',
      color: '',
      birthDate: '',
      vaccinated: false,
      spayed: false,
      microchip: '',
      allergies: '',
      medications: '',
      specialNeeds: '',
      emergencyContact: ''
    });
    setIsAddingPet(false);
  };

  const PetForm = ({ pet, onChange, isEditing = false }) => (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <Label htmlFor="name">Nombre *</Label>
          <Input
            id="name"
            value={pet.name}
            onChange={(e) => onChange({ ...pet, name: e.target.value })}
            placeholder="Nombre de la mascota"
          />
        </div>
        <div>
          <Label htmlFor="type">Tipo *</Label>
          <Select value={pet.type} onValueChange={(value) => onChange({ ...pet, type: value })}>
            <SelectTrigger>
              <SelectValue placeholder="Seleccionar tipo" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="Perro">Perro</SelectItem>
              <SelectItem value="Gato">Gato</SelectItem>
              <SelectItem value="Otro">Otro</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label htmlFor="breed">Raza</Label>
          <Input
            id="breed"
            value={pet.breed}
            onChange={(e) => onChange({ ...pet, breed: e.target.value })}
            placeholder="Raza de la mascota"
          />
        </div>
        <div>
          <Label htmlFor="age">Edad</Label>
          <Input
            id="age"
            value={pet.age}
            onChange={(e) => onChange({ ...pet, age: e.target.value })}
            placeholder="Ej: 3 años"
          />
        </div>
        <div>
          <Label htmlFor="weight">Peso</Label>
          <Input
            id="weight"
            value={pet.weight}
            onChange={(e) => onChange({ ...pet, weight: e.target.value })}
            placeholder="Ej: 25 kg"
          />
        </div>
        <div>
          <Label htmlFor="size">Tamaño</Label>
          <Select value={pet.size} onValueChange={(value) => onChange({ ...pet, size: value })}>
            <SelectTrigger>
              <SelectValue placeholder="Seleccionar tamaño" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="Pequeño">Pequeño</SelectItem>
              <SelectItem value="Mediano">Mediano</SelectItem>
              <SelectItem value="Grande">Grande</SelectItem>
              <SelectItem value="Extra Grande">Extra Grande</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label htmlFor="color">Color</Label>
          <Input
            id="color"
            value={pet.color}
            onChange={(e) => onChange({ ...pet, color: e.target.value })}
            placeholder="Color del pelaje"
          />
        </div>
        <div>
          <Label htmlFor="birthDate">Fecha de Nacimiento</Label>
          <Input
            id="birthDate"
            type="date"
            value={pet.birthDate}
            onChange={(e) => onChange({ ...pet, birthDate: e.target.value })}
          />
        </div>
        <div>
          <Label htmlFor="microchip">Microchip</Label>
          <Input
            id="microchip"
            value={pet.microchip}
            onChange={(e) => onChange({ ...pet, microchip: e.target.value })}
            placeholder="Número de microchip"
          />
        </div>
        <div>
          <Label htmlFor="emergencyContact">Contacto de Emergencia</Label>
          <Input
            id="emergencyContact"
            value={pet.emergencyContact}
            onChange={(e) => onChange({ ...pet, emergencyContact: e.target.value })}
            placeholder="Veterinario de emergencia"
          />
        </div>
      </div>
      
      <div>
        <Label htmlFor="allergies">Alergias</Label>
        <Textarea
          id="allergies"
          value={pet.allergies}
          onChange={(e) => onChange({ ...pet, allergies: e.target.value })}
          placeholder="Describir alergias conocidas"
          rows={2}
        />
      </div>
      
      <div>
        <Label htmlFor="medications">Medicamentos</Label>
        <Textarea
          id="medications"
          value={pet.medications}
          onChange={(e) => onChange({ ...pet, medications: e.target.value })}
          placeholder="Medicamentos actuales"
          rows={2}
        />
      </div>
      
      <div>
        <Label htmlFor="specialNeeds">Necesidades Especiales</Label>
        <Textarea
          id="specialNeeds"
          value={pet.specialNeeds}
          onChange={(e) => onChange({ ...pet, specialNeeds: e.target.value })}
          placeholder="Cuidados especiales, comportamiento, etc."
          rows={3}
        />
      </div>
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl mb-2">Mis Mascotas</h1>
          <p className="text-gray-600">Gestiona los perfiles de tus mascotas</p>
        </div>
        <Dialog open={isAddingPet} onOpenChange={setIsAddingPet}>
          <DialogTrigger asChild>
            <Button className="flex items-center space-x-2">
              <Plus className="w-4 h-4" />
              <span>Agregar Mascota</span>
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-3xl max-h-[80vh] overflow-y-auto">
            <DialogHeader>
              <DialogTitle>Agregar Nueva Mascota</DialogTitle>
            </DialogHeader>
            <PetForm pet={newPet} onChange={setNewPet} />
            <div className="flex justify-end space-x-2 pt-4">
              <Button variant="outline" onClick={() => setIsAddingPet(false)}>
                Cancelar
              </Button>
              <Button onClick={handleAddPet} disabled={!newPet.name || !newPet.type}>
                Agregar Mascota
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {pets.map((pet) => (
          <Card key={pet.id} className="overflow-hidden">
            <CardHeader className="pb-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="text-4xl">{pet.avatar}</div>
                  <div>
                    <CardTitle className="text-xl">{pet.name}</CardTitle>
                    <p className="text-gray-600">{pet.breed}</p>
                  </div>
                </div>
                <Button variant="ghost" size="sm">
                  <Edit className="w-4 h-4" />
                </Button>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <Badge variant="secondary">{pet.type}</Badge>
                <Badge variant="outline">{pet.size}</Badge>
              </div>

              <div className="grid grid-cols-2 gap-4 text-sm">
                <div className="flex items-center space-x-2">
                  <Calendar className="w-4 h-4 text-gray-500" />
                  <span>{pet.age}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <Weight className="w-4 h-4 text-gray-500" />
                  <span>{pet.weight}</span>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-600">Vacunado:</span>
                  <Badge variant={pet.vaccinated ? "default" : "destructive"}>
                    {pet.vaccinated ? "Sí" : "No"}
                  </Badge>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-600">Esterilizado:</span>
                  <Badge variant={pet.spayed ? "default" : "secondary"}>
                    {pet.spayed ? "Sí" : "No"}
                  </Badge>
                </div>
              </div>

              {pet.allergies && pet.allergies !== 'Ninguna conocida' && (
                <div className="bg-red-50 p-3 rounded-lg">
                  <div className="flex items-start space-x-2">
                    <Info className="w-4 h-4 text-red-500 mt-0.5" />
                    <div>
                      <p className="text-sm font-medium text-red-800">Alergias</p>
                      <p className="text-sm text-red-600">{pet.allergies}</p>
                    </div>
                  </div>
                </div>
              )}

              {pet.specialNeeds && (
                <div className="bg-blue-50 p-3 rounded-lg">
                  <div className="flex items-start space-x-2">
                    <Heart className="w-4 h-4 text-blue-500 mt-0.5" />
                    <div>
                      <p className="text-sm font-medium text-blue-800">Cuidados Especiales</p>
                      <p className="text-sm text-blue-600">{pet.specialNeeds}</p>
                    </div>
                  </div>
                </div>
              )}

              <div className="flex space-x-2">
                <Button variant="outline" size="sm" className="flex-1">
                  Ver Historial
                </Button>
                <Button 
                  variant="outline" 
                  size="sm" 
                  className="flex-1"
                  onClick={() => onNavigate('booking')}
                >
                  Agendar Servicio
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {pets.length === 0 && (
        <div className="text-center py-16">
          <div className="text-6xl mb-4">🐾</div>
          <h3 className="text-xl mb-2">No tienes mascotas registradas</h3>
          <p className="text-gray-600 mb-6">
            Agrega el perfil de tu primera mascota para comenzar a usar nuestros servicios
          </p>
          <Button onClick={() => setIsAddingPet(true)}>
            Agregar Mi Primera Mascota
          </Button>
        </div>
      )}
    </div>
  );
}