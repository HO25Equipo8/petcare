import { Button } from './ui/Button';
import { User, MapPin, Calendar, Users, AlertTriangle, Shield } from 'lucide-react';
import { Logo } from './ui/Logo';



export function PetHeader({ currentView, onNavigate, isLoggedIn, user, onLogin, onLogout }) {
  const getNavItems = () => {
    if (!isLoggedIn || !user) {
      return [];
    }

    const baseItems = [
      { id: 'dashboard', label: 'Dashboard', icon: Shield }
    ];

    if (user.role === 'admin') {
      return [
        ...baseItems,
        { id: 'incidents', label: 'Incidents', icon: AlertTriangle }
      ];
    }

    if (user.role === 'sitter') {
      return [
        { id: 'sitter-dashboard', label: 'Dashboard', icon: Shield },
        { id: 'incidents', label: 'Incidents', icon: AlertTriangle }
      ];
    }

    // Owner navigation
    return [
      ...baseItems,
      { id: 'pets', label: 'My Pets', icon: Users },
      { id: 'caregivers', label: 'Caregivers', icon: User },
      { id: 'tracking', label: 'Live Tracking', icon: MapPin },
      { id: 'booking', label: 'Book Service', icon: Calendar },
      { id: 'incidents', label: 'Incidents', icon: AlertTriangle }
    ];
  };

  const navItems = getNavItems();

  return (
    <header className="sticky top-0 z-50 w-full bg-surface-cards/95 backdrop-blur supports-[backdrop-filter]:bg-surface-cards/60 border-b border-border-default">
      <div className="container mx-auto px-4 h-16 flex items-center justify-between">
        {/* Logo y Brand Section */}
        <div className="flex items-center space-x-4">
          <div className="flex items-center cursor-pointer" onClick={() => onNavigate('landing')}>
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
            const isActive = currentView === item.id || 
              (item.id === 'dashboard' && (currentView === 'admin-dashboard' || currentView === 'sitter-dashboard'));
            
            return (
              <Button
                key={item.id}
                variant={isActive ? "default" : "ghost"}
                size="sm"
                onClick={() => onNavigate(item.id)}
                className="flex items-center space-x-2"
              >
                <Icon className="w-4 h-4" />
                <span>{item.label}</span>
              </Button>
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
                const isActive = currentView === item.id || 
                  (item.id === 'dashboard' && (currentView === 'admin-dashboard' || currentView === 'sitter-dashboard'));
                
                return (
                  <Button
                    key={item.id}
                    variant={isActive ? "default" : "ghost"}
                    size="sm"
                    onClick={() => onNavigate(item.id)}
                    className="flex items-center space-x-2 text-xs"
                  >
                    <Icon className="w-3 h-3" />
                    <span>{item.label}</span>
                  </Button>
                );
              })}
            </div>
          </div>
        </div>
      )}
    </header>
  );
}