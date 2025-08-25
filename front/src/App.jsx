import { useState } from 'react';
import { PetHeader } from './components/PetHeader';
import { LandingPage } from './components/LandingPage';
import { Dashboard } from './components/Dashboard';
import { SitterDashboard } from './components/SitterDashboard';
import { AdminDashboard } from './components/AdminDashboard';
import { PetProfiles } from './components/PetProfiles';
import { CaregiverDirectory } from './components/CaregiverDirectory';
import { LiveTracking } from './components/LiveTracking';
import { BookingSystem } from './components/BookingSystem';
import { IncidentManagement } from './components/IncidentManagement';
import { AuthModal } from './components/AuthModal';

function App() {
  const [currentView, setCurrentView] = useState('landing');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [showAuthModal, setShowAuthModal] = useState(false);

  const handleLogin = (userData) => {
    setUser(userData);
    setIsLoggedIn(true);
    // Navigate to appropriate dashboard based on role
    if (userData.role === 'admin') {
      setCurrentView('admin-dashboard');
    } else if (userData.role === 'sitter') {
      setCurrentView('sitter-dashboard');
    } else {
      setCurrentView('dashboard');
    }
  };

  const handleLogout = () => {
    setUser(null);
    setIsLoggedIn(false);
    setCurrentView('landing');
  };

  const renderContent = () => {
    // If not logged in, show landing page
    if (!isLoggedIn || !user) {
      return <LandingPage onNavigate={setCurrentView} onLogin={() => setShowAuthModal(true)} />;
    }

    // Route based on current view and user role
    switch (currentView) {
      case 'admin-dashboard':
        return user.role === 'admin' ? (
          <AdminDashboard user={user} onNavigate={setCurrentView} />
        ) : (
          <Dashboard onNavigate={setCurrentView} />
        );
      case 'sitter-dashboard':
        return user.role === 'sitter' ? (
          <SitterDashboard user={user} onNavigate={setCurrentView} />
        ) : (
          <Dashboard onNavigate={setCurrentView} />
        );
      case 'dashboard':
        // Route to appropriate dashboard based on role
        if (user.role === 'admin') {
          return <AdminDashboard user={user} onNavigate={setCurrentView} />;
        } else if (user.role === 'sitter') {
          return <SitterDashboard user={user} onNavigate={setCurrentView} />;
        } else {
          return <Dashboard onNavigate={setCurrentView} />;
        }
      case 'pets':
        return user.role !== 'admin' ? (
          <PetProfiles onNavigate={setCurrentView} />
        ) : (
          <AdminDashboard user={user} onNavigate={setCurrentView} />
        );
      case 'caregivers':
        return user.role === 'owner' ? (
          <CaregiverDirectory onNavigate={setCurrentView} />
        ) : (
          <Dashboard onNavigate={setCurrentView} />
        );
      case 'tracking':
        return user.role !== 'admin' ? (
          <LiveTracking onNavigate={setCurrentView} />
        ) : (
          <AdminDashboard user={user} onNavigate={setCurrentView} />
        );
      case 'booking':
        return user.role !== 'admin' ? (
          <BookingSystem onNavigate={setCurrentView} />
        ) : (
          <AdminDashboard user={user} onNavigate={setCurrentView} />
        );
      case 'incidents':
        return <IncidentManagement onNavigate={setCurrentView} />;
      default:
        return <LandingPage onNavigate={setCurrentView} onLogin={() => setShowAuthModal(true)} />;
    }
  };

  return (
    <div className="min-h-screen bg-background-general">
      <PetHeader 
        currentView={currentView} 
        onNavigate={setCurrentView} 
        isLoggedIn={isLoggedIn}
        user={user}
        onLogin={() => setShowAuthModal(true)}
        onLogout={handleLogout}
      />
      {renderContent()}
      
      <AuthModal
        isOpen={showAuthModal}
        onClose={() => setShowAuthModal(false)}
        onLogin={handleLogin}
      />
    </div>
  );
}

export default App;