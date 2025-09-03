
import { useState } from 'react';
import { PetHeader } from './components/PetHeader';
import { AppRoutes } from './AppRoutes';
import { AuthModal } from './components/AuthModal';
import { BrowserRouter } from 'react-router-dom';


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [showAuthModal, setShowAuthModal] = useState(false);

  const handleLogin = (userData) => {
    setUser(userData);
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setUser(null);
    setIsLoggedIn(false);
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