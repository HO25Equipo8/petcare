import { useState, useEffect } from 'react';
import { Header } from './shared/navigation/Header';
import { AppRoutes } from './AppRoutes';
import { AuthModal } from './features/auth/Modal';
import { BrowserRouter } from 'react-router-dom';
import * as jwt_decode from 'jwt-decode';
import { getProfile } from './features/dashboard/sitter/services/getProfile';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [loadingLogin, setLoadingLogin] = useState(false);

  // Si JWT, decodificar y obtener perfil
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token && !isLoggedIn) {
      try {
        const decoded = jwt_decode.default(token);
        const userId = decoded.id;
        getProfile(userId)
          .then((profile) => {
            setUser({ ...profile, id: userId, email: decoded.sub, role: (decoded.role || '').toLowerCase() });
            setIsLoggedIn(true);
          })
          .catch(() => {
            setUser(null);
            setIsLoggedIn(false);
          });
      } catch {
        setUser(null);
        setIsLoggedIn(false);
      }
    }
  }, [isLoggedIn]);

  const handleLogin = async (userData) => {
    setLoadingLogin(true);
    try {
      const profile = await getProfile(userData.id);
      setUser({ ...profile, id: userData.id, email: userData.email, role: userData.role });
      setIsLoggedIn(true);
      if (userData && userData.email) {
        localStorage.setItem('userEmail', userData.email);
      }
    } catch {
      setUser(userData);
      setIsLoggedIn(true);
    } finally {
      setLoadingLogin(false);
      setShowAuthModal(false);
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
        <Header 
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
            loading={loadingLogin}
          />
        )}
      </div>
    </BrowserRouter>
  );
}

export default App;