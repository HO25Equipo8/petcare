
import { useState, useEffect } from 'react';
import { PetHeader } from './components/PetHeader';
import { AppRoutes } from './AppRoutes';
import { AuthModal } from './components/AuthModal';
import { BrowserRouter } from 'react-router-dom';


function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [showAuthModal, setShowAuthModal] = useState(false);

  // Mock users (igual que en AuthModal)
  const mockUsers = [
    {
      id: '1',
      email: 'admin@petcare.com',
      name: 'Admin PetCare',
      role: 'admin'
    },
    {
      id: '2',
      email: 'owner1@petcare.com',
      name: 'Juan Pérez',
      role: 'owner',
      phone: '+1234567890',
      address: 'Calle Principal 123'
    },
    {
      id: '3',
      email: 'sitter1@petcare.com',
      name: 'María García',
      role: 'sitter',
      phone: '+0987654321',
      address: 'Av. Central 456'
    }
  ];

  // Restaurar usuario desde localStorage si hay token
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token && !isLoggedIn) {
      // Buscar usuario mock por token (en este mock, el token no tiene info del usuario, así que usamos el primer usuario)
      // Si tu token tiene info del usuario, decodifica aquí
      // Para demo, busca el usuario por email en localStorage (opcional)
      // Aquí simplemente restauramos el primer usuario que coincida con el token
      // Puedes guardar el email en localStorage también al hacer login para mejor restauración
      // Ejemplo:
      const email = localStorage.getItem('userEmail');
      let foundUser = null;
      if (email) {
        foundUser = mockUsers.find(u => u.email === email);
      }
      if (!foundUser) {
        foundUser = mockUsers[0]; // fallback
      }
      setUser(foundUser);
      setIsLoggedIn(true);
    }
  }, [isLoggedIn]);

  const handleLogin = (userData) => {
    setUser(userData);
    setIsLoggedIn(true);
    // Guardar email para restaurar después
    if (userData && userData.email) {
      localStorage.setItem('userEmail', userData.email);
    }
  };

  const handleLogout = () => {
    setUser(null);
    setIsLoggedIn(false);
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    console.log('Sesión cerrada');
  };

  return (
    <BrowserRouter>
      <div className="min-h-screen bg-background-general">
        <PetHeader 
          isLoggedIn={isLoggedIn}
          user={user}
          onLogin={() => setShowAuthModal(true)}
          onLogout={handleLogout}
        />
        <AppRoutes
          user={user}
          isLoggedIn={isLoggedIn}
          setShowAuthModal={setShowAuthModal}
        />
        {!isLoggedIn && (
          <AuthModal
            isOpen={showAuthModal}
            onClose={() => setShowAuthModal(false)}
            onLogin={handleLogin}
          />
        )}
      </div>
    </BrowserRouter>
  );
}

export default App;