import { useState } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from './ui/Dialog';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import { Label } from './ui/Label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/Tabs';
import { Card, CardContent } from './ui/Card';
import { User, Mail, Lock } from 'lucide-react';
import { Logo } from './ui/Logo';



export function AuthModal({ isOpen, onClose, onLogin }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [selectedRole, setSelectedRole] = useState('owner');
  const [isLoading, setIsLoading] = useState(false);

  // Mock users for demonstration
  const mockUsers = [
    {
      id: '1',
      email: 'admin@petcare.com',
      name: 'Admin PetCare',
      role: 'admin'
    },
    {
      id: '2', 
      email: 'owner@petcare.com',
      name: 'Juan Pérez',
      role: 'owner',
      phone: '+1234567890',
      address: 'Calle Principal 123'
    },
    {
      id: '3',
      email: 'sitter@petcare.com', 
      name: 'María García',
      role: 'sitter',
      phone: '+0987654321',
      address: 'Av. Central 456'
    }
  ];

  const roleInfo = {
    owner: {
      title: 'Dueño de Mascota',
      description: 'Encuentra cuidadores de confianza para tus mascotas',
      icon: User, // Puedes cambiar el icono si lo deseas
      features: ['Gestionar perfiles de mascotas', 'Reservar servicios', 'Seguimiento GPS en tiempo real', 'Gestionar incidentes']
    },
    sitter: {
      title: 'Cuidador',
      description: 'Ofrece servicios de cuidado de mascotas de calidad',
      icon: User,
      features: ['Configurar horarios disponibles', 'Gestionar zonas de servicio', 'Recibir reservas', 'Reportar incidentes']
    }
  };

  const handleLogin = async () => {
    setIsLoading(true);
    
    // Simulate API call
    setTimeout(() => {
      const user = mockUsers.find(u => u.email === email);
      
      if (user) {
        onLogin(user);
        onClose();
        resetForm();
      } else {
        alert('Usuario no encontrado. Intenta con:\n- admin@petcare.com\n- owner@petcare.com\n- sitter@petcare.com');
      }
      
      setIsLoading(false);
    }, 1000);
  };

  const handleRegister = async () => {
    setIsLoading(true);
    // Simulate API call
    setTimeout(() => {
      const newUser = {
        id: Math.random().toString(),
        email,
        name,
        role: selectedRole,
        phone,
        address
      };
      onLogin(newUser);
      onClose();
      resetForm();
      setIsLoading(false);
    }, 1000);
  };

  const resetForm = () => {
    setEmail('');
    setPassword('');
    setName('');
    setPhone('');
    setAddress('');
    setSelectedRole('owner');
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
              <Logo size={20} className="text-white" />
            </div>
            Bienvenido a PetCare
          </DialogTitle>
        </DialogHeader>

        <Tabs defaultValue="login" className="w-full">
          <TabsList className="grid w-full grid-cols-2">
            <TabsTrigger value="login">Iniciar Sesión</TabsTrigger>
            <TabsTrigger value="register">Registrarse</TabsTrigger>
          </TabsList>

          <TabsContent value="login" className="space-y-4">
            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">Correo Electrónico</Label>
                <div className="relative">
                  <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="email"
                    type="email"
                    placeholder="tu@email.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              

              <div className="space-y-2">
                <Label htmlFor="password">Contraseña</Label>
                <div className="relative">
                  <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="password"
                    type="password"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="pl-10"
                  />
                </div>
                <div className="text-right">
                  <button
                    type="button"
                    className="text-xs text-primary hover:underline focus:outline-none"
                    onClick={() => alert('Funcionalidad de recuperación de contraseña próximamente.')}
                  >
                    ¿Olvidaste tu contraseña? Haz click aquí
                  </button>
                </div>
              </div>

              <Button 
                onClick={handleLogin} 
                className="w-full" 
                disabled={isLoading || !email || !password}
              >
                {isLoading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
              </Button>

              <div className="text-sm text-muted-foreground">
                <p className="mb-2">Usuarios de prueba:</p>
                <div className="space-y-1 text-xs">
                  <p>• <strong>Admin:</strong> admin@petcare.com</p>
                  <p>• <strong>Dueño:</strong> owner@petcare.com</p>
                  <p>• <strong>Cuidador:</strong> sitter@petcare.com</p>
                </div>
              </div>
            </div>
          </TabsContent>

          <TabsContent value="register" className="space-y-4">
            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="registerEmail">Correo Electrónico</Label>
                <div className="relative">
                  <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="registerEmail"
                    type="email"
                    placeholder="tu@email.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="registerPassword">Contraseña</Label>
                <div className="relative">
                  <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="registerPassword"
                    type="password"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="registerPassword">Confirmar contraseña</Label>
                <div className="relative">
                  <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="registerPassword"
                    type="password"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>

              <div className="space-y-3">
                <Label>Tipo de Usuario</Label>
                <div className="flex flex-row gap-3">
                  {Object.entries(roleInfo).map(([key, info]) => {
                    const Icon = info.icon;
                    return (
                      <Card
                        key={key}
                        className={`flex-1 max-w-[220px] cursor-pointer transition-all ${
                          selectedRole === key ? 'ring-2 ring-primary' : 'hover:bg-muted/50'
                        }`}
                        onClick={() => setSelectedRole(key)}
                      >
                        <CardContent className="p-4 h-full">
                          <div className="flex flex-col items-center justify-center h-full gap-1">
                            <div className={`p-2 rounded-full mb-1 flex items-center justify-center ${
                              selectedRole === key ? 'bg-primary text-primary-foreground' : 'bg-muted'
                            }`}>
                              <Icon className="w-5 h-5" />
                            </div>
                            <div className="w-full text-center">
                              <div className="flex flex-col items-center gap-1 mb-0.5">
                                <h4 className="font-medium text-sm">{info.title}</h4>
                                {/* {selectedRole === key && (
                                  <Badge variant="default" className="text-xs">Seleccionado</Badge>
                                )} */}
                              </div>
                              {/* <p className="text-xs text-muted-foreground mb-1">{info.description}</p>
                              <div className="space-y-0.5">
                                {info.features.map((feature, index) => (
                                  <p key={index} className="text-xs text-muted-foreground">• {feature}</p>
                                ))}
                              </div> */}
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    );
                  })}
                </div>
              </div>

              <Button 
                onClick={handleRegister} 
                className="w-full"
                disabled={isLoading || !email || !password || !name}
              >
                {isLoading ? 'Registrando...' : 'Crear Cuenta'}
              </Button>
            </div>
          </TabsContent>
        </Tabs>
      </DialogContent>
    </Dialog>
  );
}