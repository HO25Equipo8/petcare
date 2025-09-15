
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { HomePage } from './features/landing-page/Page';
import { OwnerDashboard } from './features/dashboard/owner/NavItem';
import { SitterDashboard } from './features/dashboard/sitter/NavItem';
import { AdminDashboard } from './features/dashboard/admin/NavItem';
import { PetProfiles } from './features/pet-profiles/NavItem';
import { CaregiverDirectory } from './features/caregivers/NavItem';
import { LiveTracking } from './features/tracking/NavItem';
import { BookingSystem } from './features/booking/NavItem';
import { IncidentManagement } from './features/incidents/NavItem';

export function AppRoutes({ user, isLoggedIn, setShowAuthModal }) {
  const location = useLocation();

  // Si no loggeado, solo /home
  if (!isLoggedIn || !user) {
    return (
      <Routes>
        <Route path="/home" element={<HomePage onLogin={() => setShowAuthModal(true)} />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    );
  }

  // Dashboard path por rol
  let dashboardPath = '/home';
  if (user.role === 'admin') dashboardPath = '/admin/dashboard';
  if (user.role === 'sitter') dashboardPath = '/sitter/dashboard';
  if (user.role === 'owner') dashboardPath = '/owner/dashboard';

  // Si usuario en /home, redirigir a su dashboard
  if (location.pathname === '/home') {
    return <Navigate to={dashboardPath} replace />;
  }

  // Rutas por rol
  return (
    <Routes>
      {/* Home */}
      <Route path="/home" element={<HomePage onLogin={() => setShowAuthModal(true)} />} />

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
