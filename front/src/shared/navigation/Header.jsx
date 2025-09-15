
import { Button } from '../ui/Button';
import { User, MapPin, Calendar, Users, AlertTriangle, Shield } from 'lucide-react';
import { Logo } from '../ui/Logo';
import { Link, useLocation, useNavigate } from 'react-router-dom';

export function Header({ isLoggedIn, user, onLogin, onLogout }) {
  const location = useLocation();
  const navigate = useNavigate();

  // Ruta e items de nav por rol
  const getNavItems = () => {
    if (!isLoggedIn || !user) return [];

    if (user.role === 'admin') {
      return [
        { to: '/admin/dashboard', label: 'Dashboard', icon: Shield },
        { to: '/admin/incidents', label: 'Incidents', icon: AlertTriangle }
      ];
    }
    if (user.role === 'sitter') {
      return [
        { to: '/sitter/dashboard', label: 'Dashboard', icon: Shield },
        { to: '/sitter/incidents', label: 'Incidents', icon: AlertTriangle }
      ];
    }
    // Propietario de mascota
    return [
      { to: '/owner/dashboard', label: 'Dashboard', icon: Shield },
      { to: '/owner/pets', label: 'My Pets', icon: Users },
      { to: '/owner/caregivers', label: 'Caregivers', icon: User },
      { to: '/owner/tracking', label: 'Live Tracking', icon: MapPin },
      { to: '/owner/booking', label: 'Book Service', icon: Calendar },
      { to: '/owner/incidents', label: 'Incidents', icon: AlertTriangle }
    ];
  };

  const navItems = getNavItems();

  // Revisar si item de nav está activo
  const isActive = (to) => {
    // Coincide o subruta
    return location.pathname === to || location.pathname.startsWith(to + '/');
  };

  // Click en logo para ir /home
  const handleBrandClick = () => {
    navigate('/home');
  };

  return (
    <header className="sticky top-0 z-50 w-full bg-surface-cards/95 backdrop-blur supports-[backdrop-filter]:bg-surface-cards/60 border-b border-border-default">
      <div className="container mx-auto px-4 h-16 flex items-center justify-between">
        {/* Logo y Brand Section */}
        <div className="flex items-center space-x-4">
          <div className="flex items-center cursor-pointer" onClick={handleBrandClick}>
            {/* Logo actualizado con el nuevo diseño */}
            <div className="w-8 h-8 text-primary-500 flex items-center justify-center mr-3">
              <Logo size={32} />
            </div>
            {/* Brand name y slogan */}
            <div className="flex flex-col">
              <span className="text-lg font-semibold text-text-primary font-headings">
                PetCare
              </span>
              <span className="text-xs text-text-secondary font-body hidden sm:block leading-none">
                Paseo seguro, cariño puro
              </span>
            </div>
          </div>
        </div>

        {/* Navigation - Desktop */}
        <nav className="hidden md:flex items-center space-x-1">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <Link key={item.to} to={item.to} className="flex items-center">
                <Button
                  variant={isActive(item.to) ? 'default' : 'ghost'}
                  size="sm"
                  className="flex items-center space-x-2"
                >
                  <Icon className="w-4 h-4" />
                  <span>{item.label}</span>
                </Button>
              </Link>
            );
          })}
        </nav>

        {/* User Actions */}
        <div className="flex items-center space-x-2">
          {isLoggedIn && user ? (
            <div className="flex items-center space-x-2">
              {/* User info - hidden on mobile */}
              <div className="hidden sm:flex flex-col text-right">
                <span className="text-sm font-medium text-text-primary">
                  {user.name}
                </span>
                <span className="text-xs text-text-secondary capitalize">
                  {user.role}
                </span>
              </div>
              {/* User avatar/menu */}
              <Button
                variant="outline"
                size="sm"
                onClick={onLogout}
                className="flex items-center space-x-2"
              >
                <User className="w-4 h-4" />
                <span className="hidden sm:inline">Sign Out</span>
              </Button>
            </div>
          ) : (
            <Button onClick={onLogin} size="sm">
              Acceder
            </Button>
          )}
        </div>
      </div>

      {/* Mobile Navigation */}
      {isLoggedIn && navItems.length > 0 && (
        <div className="md:hidden border-t border-border-default bg-surface-cards">
          <div className="container mx-auto px-4 py-2">
            <div className="flex flex-wrap gap-1">
              {navItems.map((item) => {
                const Icon = item.icon;
                return (
                  <Link key={item.to} to={item.to} className="flex items-center">
                    <Button
                      variant={isActive(item.to) ? 'default' : 'ghost'}
                      size="sm"
                      className="flex items-center space-x-2 text-xs"
                    >
                      <Icon className="w-3 h-3" />
                      <span>{item.label}</span>
                    </Button>
                  </Link>
                );
              })}
            </div>
          </div>
        </div>
      )}
    </header>
  );
}