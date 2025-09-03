
import { useState } from 'react';
import { Calendar, Clock, DollarSign, Star, CheckCircle } from 'lucide-react';
import BookingsSubTabSitter from '../components/BookingsSubTabSitter.jsx';
import ProfileSubTabSitter from '../components/ProfileSubTabSitter.jsx';
import ScheduleSubTabSitter from '../components/ScheduleSubTabSitter.jsx';
import ServiceZoneSubTabSitter from '../components/ServiceZoneSubTabSitter.jsx';
import StatsCard from '../components/StatsCard.jsx';
import TabsPanel from '../components/TabsPanel.jsx';
import {
  initialSchedule,
  initialServiceZones,
  initialNewZone,
  upcomingBookings,
  stats
} from '../services/SitterDashboard.js';

export function SitterDashboard({ user, onNavigate }) {
  const [schedule, setSchedule] = useState(initialSchedule);
  const [serviceZones, setServiceZones] = useState(initialServiceZones);
  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const [isZoneModalOpen, setIsZoneModalOpen] = useState(false);
  const [newZone, setNewZone] = useState(initialNewZone);

  const handleScheduleUpdate = (dayIndex, field, value) => {
    const updatedSchedule = [...schedule];
    updatedSchedule[dayIndex] = { ...updatedSchedule[dayIndex], [field]: value };
    setSchedule(updatedSchedule);
  };

  const handleAddServiceZone = () => {
    if (newZone.name && newZone.centerAddress) {
  const zone = {
        id: Math.random().toString(),
        ...newZone,
        active: true
      };
      setServiceZones([...serviceZones, zone]);
      setNewZone({ name: '', radius: 5, centerAddress: '' });
      setIsZoneModalOpen(false);
    }
  };

  const toggleZoneStatus = (zoneId) => {
    setServiceZones(serviceZones.map(zone => 
      zone.id === zoneId ? { ...zone, active: !zone.active } : zone
    ));
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl mb-2">Dashboard de Cuidador</h1>
        <p className="text-muted-foreground">Bienvenido, {user.name}</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-6 mb-8">
        {[
          {
            title: 'Reservas Totales',
            value: stats.totalBookings,
            icon: <Calendar className="w-8 h-8 text-blue-500" />,
          },
          {
            title: 'Esta Semana',
            value: stats.thisWeekBookings,
            icon: <Clock className="w-8 h-8 text-green-500" />,
          },
          {
            title: 'Ganancias',
            value: `$${stats.earnings}`,
            icon: <DollarSign className="w-8 h-8 text-green-600" />,
          },
          {
            title: 'Calificación',
            value: stats.rating,
            icon: <Star className="w-8 h-8 text-yellow-500" />,
          },
          {
            title: 'Completadas',
            value: stats.completedServices,
            icon: <CheckCircle className="w-8 h-8 text-primary" />,
          },
        ].map((card) => (
          <StatsCard
            key={card.title}
            title={card.title}
            value={card.value}
            icon={card.icon}
          />
        ))}
      </div>


      <TabsPanel
        defaultValue="bookings"
        className="space-y-6"
        tabs={[
          {
            value: 'bookings',
            label: 'Reservas',
            content: <BookingsSubTabSitter upcomingBookings={upcomingBookings} />,
          },
          {
            value: 'schedule',
            label: 'Horarios',
            content: (
              <ScheduleSubTabSitter
                schedule={schedule}
                isScheduleModalOpen={isScheduleModalOpen}
                setIsScheduleModalOpen={setIsScheduleModalOpen}
                handleScheduleUpdate={handleScheduleUpdate}
              />
            ),
          },
          {
            value: 'zones',
            label: 'Zonas de Servicio',
            content: (
              <ServiceZoneSubTabSitter
                serviceZones={serviceZones}
                isZoneModalOpen={isZoneModalOpen}
                setIsZoneModalOpen={setIsZoneModalOpen}
                newZone={newZone}
                setNewZone={setNewZone}
                handleAddServiceZone={handleAddServiceZone}
                toggleZoneStatus={toggleZoneStatus}
              />
            ),
          },
          {
            value: 'profile',
            label: 'Perfil',
            content: <ProfileSubTabSitter user={user} />,
          },
        ]}
      />
    </div>
  );
}