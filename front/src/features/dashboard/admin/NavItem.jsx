import { useState } from 'react';
import { AlertTriangle, DollarSign, Heart, UserCheck, Users } from 'lucide-react';
import AnalyticsSubTabAdmin from './components/Analytics';
import IncidentsSubTabAdmin from './components/Incidents';
import StatsCard from '../../../shared/dashboard/StatsCard';
import SystemSettingsSubTabAdmin from './components/Settings';
import TabsPanel from '../../../shared/dashboard/TabsPanel';
import UserDetailsModal from './components/UserDetails';
import UsersManagementSubTabAdmin from './components/UsersManagement';
import {
  stats,
  recentUsers,
  pendingIncidents,
  getStatusColor,
  getSeverityColor,
  handleUserAction
} from './services/hardcode';

export function AdminDashboard({ user, onNavigate }) {
  const [selectedUser, setSelectedUser] = useState(null);
  const [isUserModalOpen, setIsUserModalOpen] = useState(false);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Panel de Administración</h1>
        <p className="text-muted-foreground">Bienvenido, {user.name} - Gestiona la plataforma PetCare</p>
      </div>

  {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {[
          {
            title: 'Total Usuarios',
            value: stats.totalUsers.toLocaleString(),
            icon: <Users className="w-8 h-8 text-blue-500" />,
            additionalText: <p className="text-xs text-green-600">+12% este mes</p>,
          },
          {
            title: 'Dueños / Cuidadores',
            value: `${stats.totalOwners} / ${stats.totalSitters}`,
            icon: (
              <div className="flex space-x-1">
                <Heart className="w-4 h-4 text-primary" />
                <UserCheck className="w-4 h-4 text-green-500" />
              </div>
            ),
            additionalText: <p className="text-xs text-blue-600">Ratio 2.5:1</p>,
          },
          {
            title: 'Ingresos Totales',
            value: `$${stats.totalRevenue.toLocaleString()}`,
            icon: <DollarSign className="w-8 h-8 text-green-500" />,
            additionalText: <p className="text-xs text-green-600">+8% este mes</p>,
          },
          {
            title: 'Incidentes Pendientes',
            value: stats.pendingIncidents,
            valueColor: 'text-orange-600',
            icon: <AlertTriangle className="w-8 h-8 text-orange-500" />,
            additionalText: <p className="text-xs text-orange-600">Requiere atención</p>,
          },
        ].map((card) => (
          <StatsCard
            key={card.title}
            title={card.title}
            value={card.value}
            valueColor={card.valueColor}
            icon={card.icon}
            additionalText={card.additionalText}
          />
        ))}
      </div>


      <TabsPanel
        defaultValue="users"
        className="space-y-6"
        tabs={[
          {
            value: 'users',
            label: 'Gestión de Usuarios',
            content: (
              <UsersManagementSubTabAdmin
                recentUsers={recentUsers}
                getStatusColor={getStatusColor}
                setSelectedUser={setSelectedUser}
                setIsUserModalOpen={setIsUserModalOpen}
                handleUserAction={handleUserAction}
              />
            ),
          },
          {
            value: 'incidents',
            label: 'Incidentes',
            content: (
              <IncidentsSubTabAdmin
                pendingIncidents={pendingIncidents}
                getSeverityColor={getSeverityColor}
                onNavigate={onNavigate}
              />
            ),
          },
          {
            value: 'analytics',
            label: 'Analytics',
            content: <AnalyticsSubTabAdmin stats={stats} />,
          },
          {
            value: 'settings',
            label: 'Configuración',
            content: <SystemSettingsSubTabAdmin />,
          },
        ]}
      />

  {/* User Details Modal */}
  <UserDetailsModal
    open={isUserModalOpen}
    onOpenChange={setIsUserModalOpen}
    selectedUser={selectedUser}
    getStatusColor={getStatusColor}
  />
    </div>
  );
}