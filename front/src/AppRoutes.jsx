
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { LandingPage } from './pages/LandingPage';
import { OwnerDashboard } from './pages/OwnerDashboard';
import { SitterDashboard } from './pages/SitterDashboard';
import { AdminDashboard } from './pages/AdminDashboard';
import { PetProfiles } from './components/PetProfiles';
import { CaregiverDirectory } from './components/CaregiverDirectory';
import { LiveTracking } from './components/LiveTracking';
import { BookingSystem } from './components/BookingSystem';
import { IncidentManagement } from './components/IncidentManagement';

export function AppRoutes({ user, isLoggedIn, setShowAuthModal }) {
  const location = useLocation();

  // Si no se inició sesión, mostrar landing page
  if (!isLoggedIn || !user) {
    return (
      <Routes>
        <Route path="/home" element={<LandingPage onLogin={() => setShowAuthModal(true)} />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    );
  }

  // Redirección a dashboard por rol
  let dashboardPath = '/home';
  if (user.role === 'admin') dashboardPath = '/admin/dashboard';
  if (user.role === 'sitter') dashboardPath = '/sitter/dashboard';
  if (user.role === 'owner') dashboardPath = '/owner/dashboard';

  // Si usuario está /home, redirigir a dashboard
  if (location.pathname === '/home') {
    return <Navigate to={dashboardPath} replace />;
  }

  // Rutas por rol
  return (
    <Routes>
      {/* Home */}
      <Route path="/home" element={<LandingPage onLogin={() => setShowAuthModal(true)} />} />

      {/* Admin */}
      {user.role === 'admin' && (
        <>
          <Route path="/admin/dashboard" element={<AdminDashboard user={user} />} />
          <Route path="/admin/incidents" element={<IncidentManagement />} />
          <Route path="*" element={<Navigate to="/admin/dashboard" replace />} />
        </>
      )}

      {/* Sitter */}
      {user.role === 'sitter' && (
        <>
          <Route path="/sitter/dashboard" element={<SitterDashboard user={user} />} />
          <Route path="/sitter/incidents" element={<IncidentManagement />} />
          <Route path="*" element={<Navigate to="/sitter/dashboard" replace />} />
        </>
      )}

      {/* Owner */}
      {user.role === 'owner' && (
        <>
          <Route path="/owner/dashboard" element={<OwnerDashboard />} />
          <Route path="/owner/pets" element={<PetProfiles />} />
          <Route path="/owner/caregivers" element={<CaregiverDirectory />} />
          <Route path="/owner/tracking" element={<LiveTracking />} />
          <Route path="/owner/booking" element={<BookingSystem />} />
          <Route path="/owner/incidents" element={<IncidentManagement />} />
          <Route path="*" element={<Navigate to="/owner/dashboard" replace />} />
        </>
      )}
    </Routes>
  );
}
