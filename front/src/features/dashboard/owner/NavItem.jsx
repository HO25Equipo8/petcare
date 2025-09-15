
import { Button } from '../../../shared/ui/Button';
import { Card, CardContent } from '../../../shared/ui/Card';
import { Heart, Clock, Star, AlertTriangle } from 'lucide-react';
import MyPetsCardOwner from './components/MyPets';
import QuickActionsPanelOwner from './components/QuickActions';
import RecentActivityCardOwner from './components/RecentActivity';
import StatsCard from '../../../shared/dashboard/StatsCard';
import UpcomingServicesCardOwner from './components/UpcomingServices';
import {
  pets,
  upcomingServices,
  recentActivity,
  ownerStats
} from './services/hardcode';

export function OwnerDashboard({ onNavigate }) {

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Bienvenido, Juan</h1>
        <p className="text-gray-600">Gestiona el cuidado de tus mascotas desde aquí</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {[
          {
            title: 'Mascotas',
            value: ownerStats.petsCount,
            icon: <Heart className="w-8 h-8 text-primary" />,
            titleColor: 'text-gray-600',
          },
          {
            title: 'Servicios Activos',
            value: ownerStats.activeServices,
            icon: <Clock className="w-8 h-8 text-blue-500" />,
            titleColor: 'text-gray-600',
          },
          {
            title: 'Incidentes Pendientes',
            value: ownerStats.pendingIncidents,
            valueColor: 'text-orange-600',
            icon: <AlertTriangle className="w-8 h-8 text-orange-500" />,
            titleColor: 'text-gray-600',
          },
          {
            title: 'Calificación',
            value: ownerStats.rating,
            icon: <Star className="w-8 h-8 text-yellow-500" />,
            titleColor: 'text-gray-600',
          },
        ].map((card) => (
          <StatsCard
            key={card.title}
            title={card.title}
            value={card.value}
            valueColor={card.valueColor}
            icon={card.icon}
            titleColor={card.titleColor}
          />
        ))}
      </div>

      {/* Alert for pending incidents */}
      <div className="mb-8">
        <Card className="border-orange-200 bg-orange-50">
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <AlertTriangle className="w-5 h-5 text-orange-600" />
                <div>
                  <h3 className="font-medium text-orange-800">Incidentes Pendientes</h3>
                  <p className="text-sm text-orange-700">Tienes 2 incidentes que requieren atención</p>
                </div>
              </div>
              <Button 
                variant="outline" 
                size="sm"
                onClick={() => onNavigate('incidents')}
                className="border-orange-300 text-orange-700 hover:bg-orange-100"
              >
                Ver Incidentes
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* My Pets */}
        <MyPetsCardOwner pets={pets} onNavigate={onNavigate} />

        {/* Upcoming Services */}
        <UpcomingServicesCardOwner upcomingServices={upcomingServices} onNavigate={onNavigate} />

        {/* Recent Activity */}
        <RecentActivityCardOwner recentActivity={recentActivity} />
      </div>

      {/* Quick Actions */}
  <QuickActionsPanelOwner onNavigate={onNavigate} />
    </div>
  );
}
