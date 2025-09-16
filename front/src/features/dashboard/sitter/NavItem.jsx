
import { useState } from 'react';
import { Calendar, Clock, DollarSign, Star, CheckCircle, Package, Shield, Stethoscope, Scissors, Car } from 'lucide-react';
import Bookings from './components/Bookings';
import Profile from './components/Profile';
import Schedule from './components/Schedule';
import CreateService from './components/CreateService';
import EditService from './components/EditService';
import CreateDiscount from './components/CreateDiscount';
import CreateCombo from './components/CreateCombo';
import ServicesManagement from './components/ServicesManagement';
import Discounts from './components/Discounts';
import ComboOffering from './components/ComboOffering';
import Simulator from './components/Simulator'
import { toast } from 'sonner';
import StatsCard from '../../../shared/dashboard/StatsCard';
import TabsPanel from '../../../shared/dashboard/TabsPanel';
import {
  initialSchedule,
  initialServiceZones,
  initialNewZone,
  upcomingBookings,
  stats
} from './services/hardcode';


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

    const [planDiscountRules, setPlanDiscountRules] = useState([
      { id: 'rule1', category: 'Cliente Nuevo', minSessionsPerWeek: 1, maxSessionsPerWeek: 1, discount: 0 },
      { id: 'rule2', category: 'Cliente Regular', minSessionsPerWeek: 2, maxSessionsPerWeek: 3, discount: 8 },
      { id: 'rule3', category: 'Cliente Frecuente', minSessionsPerWeek: 4, maxSessionsPerWeek: 6, discount: 15 },
      { id: 'rule4', category: 'Cliente VIP', minSessionsPerWeek: 7, maxSessionsPerWeek: 999, discount: 25 }
    ]);
  
    const [comboOfferings, setComboOfferings] = useState([
      {
        id: 'combo1',
        name: 'Pack Básico',
        description: 'Combo ideal para mascotas que necesitan cuidado básico diario',
        discount: 10,
        offeringIds: [1, 5],
        originalPrice: 3500,
        finalPrice: 3150,
        active: true
      },
      {
        id: 'combo2',
        name: 'Pack Premium',
        description: 'Combo paseo + baño para mascotas energéticas con necesidades especiales',
        discount: 20,
        offeringIds: [2, 7, 3],
        originalPrice: 8500,
        finalPrice: 6800,
        active: true
      },
      {
        id: 'combo3',
        name: 'Pack Completo',
        description: 'Combo integral con todos los servicios para mascotas activas',
        discount: 30,
        offeringIds: [2, 8, 3, 6, 9],
        originalPrice: 15000,
        finalPrice: 10500,
        active: true
      },
      {
        id: 'combo4',
        name: 'Pack Wellness',
        description: 'Combo enfocado en el bienestar integral de tu mascota',
        discount: 18,
        offeringIds: [4, 7, 9],
        originalPrice: 11000,
        finalPrice: 9020,
        active: true
      }
    ]);
  
    const [systemServices, setSystemServices] = useState([
      {
        id: 'service1',
        name: 'PASEO',
        description: 'Servicio de paseo personalizado para tu mascota con cuidadores verificados',
        basePrice: 2500,
        applicablePetTypes: ['Perros', 'Gatos'],
        allowedRole: 'PASEADOR',
        active: true,
        createdAt: '2024-01-15T10:00:00Z',
        updatedAt: '2024-01-20T14:30:00Z'
      },
      {
        id: 'service2',
        name: 'ASEO',
        description: 'Servicio completo de aseo y belleza para mascotas con productos premium',
        basePrice: 4500,
        applicablePetTypes: ['Perros', 'Gatos', 'Conejos'],
        allowedRole: 'PELUQUERO',
        active: true,
        createdAt: '2024-01-16T11:15:00Z',
        updatedAt: '2024-01-22T16:45:00Z'
      },
      {
        id: 'service3',
        name: 'GUARDERIA',
        description: 'Cuidado temporal de mascotas en instalaciones seguras y monitoreadas',
        basePrice: 6000,
        applicablePetTypes: ['Perros', 'Gatos', 'Aves', 'Conejos'],
        allowedRole: 'CUIDADOR',
        active: true,
        createdAt: '2024-01-17T09:30:00Z',
        updatedAt: '2024-01-21T12:20:00Z'
      },
      {
        id: 'service4',
        name: 'VETERINARIA',
        description: 'Atención veterinaria profesional a domicilio o en clínica',
        basePrice: 8500,
        applicablePetTypes: ['Perros', 'Gatos', 'Aves', 'Conejos', 'Roedores'],
        allowedRole: 'VETERINARIO',
        active: true,
        createdAt: '2024-01-18T08:00:00Z',
        updatedAt: '2024-01-23T10:15:00Z'
      }
    ]);
  
    const availableServices = [
      { id: 'walk', name: 'Paseo Regular', price: 2000, icon: '🚶', category: 'Ejercicio', active: true },
      { id: 'walk-extended', name: 'Paseo Extendido', price: 3500, icon: '🏃', category: 'Ejercicio', active: true },
      { id: 'playtime', name: 'Tiempo de Juego', price: 2500, icon: '🎈', category: 'Ejercicio', active: true },
      { id: 'socialization', name: 'Socialización', price: 3500, icon: '🐕', category: 'Ejercicio', active: true },
      { id: 'feeding', name: 'Alimentación', price: 1500, icon: '🍽️', category: 'Alimentación', active: true },
      { id: 'feeding-special', name: 'Alimentación Especial', price: 2500, icon: '🥘', category: 'Alimentación', active: true },
      { id: 'grooming', name: 'Baño y Aseo', price: 4500, icon: '🛁', category: 'Cuidado Personal', active: true },
      { id: 'grooming-premium', name: 'Aseo Premium', price: 7500, icon: '✨', category: 'Cuidado Personal', active: true },
      { id: 'training', name: 'Entrenamiento Básico', price: 6000, icon: '🎾', category: 'Entrenamiento', active: true }
    ];
  
    // Estados de componentes para combos
    const [isCreatingCombo, setIsCreatingCombo] = useState(false);
    const [newCombo, setNewCombo] = useState({
      name: 'Pack Básico',
      description: '',
      discount: 0,
      offeringIds: [],
      originalPrice: 0,
      finalPrice: 0,
      active: true
    });
    const [isEditingRule, setIsEditingRule] = useState(false);
    const [newRule, setNewRule] = useState({
      category: 'Cliente Nuevo',
      minSessionsPerWeek: 1,
      maxSessionsPerWeek: 1,
      discount: 0
    });
  
    // Estados para alta/baja de servicios
    const [selectedService, setSelectedService] = useState(null);
    const [isServiceModalOpen, setIsServiceModalOpen] = useState(false);
    const [isCreatingService, setIsCreatingService] = useState(false);
    const [serviceSearchTerm, setServiceSearchTerm] = useState('');
    const [serviceStatusFilter, setServiceStatusFilter] = useState('all');
    const [serviceRoleFilter, setServiceRoleFilter] = useState('all');
    const [newService, setNewService] = useState({
      name: 'PASEO',
      description: '',
      basePrice: 0,
      applicablePetTypes: [],
      allowedRole: 'PASEADOR',
      active: true
    });
  
    // Funciones utilitarias para status
    const getStatusColor = (status) => {
      switch (status) {
        case 'active': return 'bg-primary-50 text-primary-700 border border-primary-200';
        case 'suspended': return 'bg-coral-50 text-coral-700 border border-coral-200';
        case 'pending': return 'bg-accent-50 text-accent-700 border border-accent-200';
        default: return 'bg-neutral-50 text-neutral-700 border border-neutral-200';
      }
    };
  
    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
      }).format(amount);
    };
  
    // Gestión de servicios
    const getServiceIcon = (serviceName) => {
      switch (serviceName) {
        case 'PASEO': return <Car className="h-5 w-5" />;
        case 'ASEO': return <Scissors className="h-5 w-5" />;
        case 'GUARDERIA': return <Shield className="h-5 w-5" />;
        case 'VETERINARIA': return <Stethoscope className="h-5 w-5" />;
        default: return <Package className="h-5 w-5" />;
      }
    };
  
    const getServiceNameSpanish = (serviceName) => {
      switch (serviceName) {
        case 'PASEO': return 'Paseo';
        case 'ASEO': return 'Aseo';
        case 'GUARDERIA': return 'Guardería';
        case 'VETERINARIA': return 'Veterinaria';
        default: return serviceName;
      }
    };
  
    const getRoleNameSpanish = (role) => {
      switch (role) {
        case 'PASEADOR': return 'Paseador';
        case 'VETERINARIO': return 'Veterinario';
        case 'PELUQUERO': return 'Peluquero';
        case 'CUIDADOR': return 'Cuidador';
        case 'ADMINISTRADOR': return 'Administrador';
        case 'USUARIO': return 'Usuario';
        default: return role;
      }
    };
  
    const filteredServices = systemServices.filter(service => {
      const matchesSearch = service.name.toLowerCase().includes(serviceSearchTerm.toLowerCase()) ||
                           service.description.toLowerCase().includes(serviceSearchTerm.toLowerCase());
      const matchesStatus = serviceStatusFilter === 'all' || 
                           (serviceStatusFilter === 'active' && service.active) ||
                           (serviceStatusFilter === 'inactive' && !service.active);
      const matchesRole = serviceRoleFilter === 'all' || service.allowedRole === serviceRoleFilter;
      return matchesSearch && matchesStatus && matchesRole;
    });
  
    const handleServiceAction = (serviceId, action) => {
      setSystemServices(services => 
        services.map(service => {
          if (service.id === serviceId) {
            switch (action) {
              case 'activate':
                toast.success(`Servicio ${getServiceNameSpanish(service.name)} activado exitosamente`);
                return { ...service, active: true, updatedAt: new Date().toISOString() };
              case 'deactivate':
                toast.success(`Servicio ${getServiceNameSpanish(service.name)} desactivado exitosamente`);
                return { ...service, active: false, updatedAt: new Date().toISOString() };
              default:
                return service;
            }
          }
          return service;
        })
      );
    };
  
    const saveService = () => {
      if (!newService.name || !newService.description || !newService.basePrice) {
        toast.error('Por favor completa todos los campos obligatorios');
        return;
      }
  
      const service = {
        id: Date.now().toString(),
        name: newService.name,
        description: newService.description || '',
        basePrice: newService.basePrice || 0,
        applicablePetTypes: newService.applicablePetTypes || [],
        allowedRole: newService.allowedRole,
        active: true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
  
      setSystemServices([...systemServices, service]);
      setNewService({
        name: 'PASEO',
        description: '',
        basePrice: 0,
        applicablePetTypes: [],
        allowedRole: 'PASEADOR',
        active: true
      });
      setIsCreatingService(false);
      toast.success(`Servicio "${getServiceNameSpanish(service.name)}" creado exitosamente`);
    };
  
    const removeService = (id) => {
      const service = systemServices.find(s => s.id === id);
      setSystemServices(systemServices.filter(service => service.id !== id));
      toast.success(`Servicio "${service ? getServiceNameSpanish(service.name) : ''}" eliminado exitosamente`);
    };
  
    const updateService = () => {
      if (!selectedService) return;
      
      setSystemServices(services =>
        services.map(service => {
          if (service.id === selectedService.id) {
            const updatedService = { 
              ...selectedService, 
              updatedAt: new Date().toISOString() 
            };
            toast.success(`Servicio "${getServiceNameSpanish(updatedService.name)}" actualizado exitosamente`);
            return updatedService;
          }
          return service;
        })
      );
      setIsServiceModalOpen(false);
      setSelectedService(null);
    };
  
    // Reglas de descuentos
    const addDiscountRule = () => {
      const rule = {
        id: Date.now().toString(),
        category: newRule.category,
        minSessionsPerWeek: newRule.minSessionsPerWeek || 1,
        maxSessionsPerWeek: newRule.maxSessionsPerWeek || 1,
        discount: newRule.discount || 0
      };
      setPlanDiscountRules([...planDiscountRules, rule]);
      setNewRule({
        category: 'Cliente Nuevo',
        minSessionsPerWeek: 1,
        maxSessionsPerWeek: 1,
        discount: 0
      });
      setIsEditingRule(false);
      toast.success('Regla de descuento creada exitosamente');
    };
  
    const updateDiscountRule = (id, field, value) => {
      setPlanDiscountRules(rules =>
        rules.map(rule =>
          rule.id === id ? { ...rule, [field]: value } : rule
        )
      );
    };
  
    const removeDiscountRule = (id) => {
      const rule = planDiscountRules.find(r => r.id === id);
      setPlanDiscountRules(rules => rules.filter(rule => rule.id !== id));
      toast.success(`Regla "${rule?.category}" eliminada exitosamente`);
    };
  
    const saveDiscountRules = () => {
      toast.success('Reglas de descuento guardadas exitosamente');
    };
  
    // Combos
    const calculateComboPrice = (offeringIds, discount) => {
      const originalPrice = offeringIds.reduce((total, offeringId) => {
        const service = availableServices.find(s => s.id === offeringId.toString());
        return total + (service?.price || 0);
      }, 0);
      const finalPrice = originalPrice - (originalPrice * discount / 100);
      return { originalPrice, finalPrice };
    };
  
    const handleOfferingToggle = (offeringId) => {
      const currentOfferings = newCombo.offeringIds || [];
      const updatedOfferings = currentOfferings.includes(offeringId)
        ? currentOfferings.filter(id => id !== offeringId)
        : [...currentOfferings, offeringId];
      
      const { originalPrice, finalPrice } = calculateComboPrice(updatedOfferings, newCombo.discount || 0);
      
      setNewCombo({
        ...newCombo,
        offeringIds: updatedOfferings,
        originalPrice,
        finalPrice
      });
    };
  
    const handleComboDiscountChange = (discount) => {
      const { originalPrice, finalPrice } = calculateComboPrice(newCombo.offeringIds || [], discount);
      setNewCombo({
        ...newCombo,
        discount,
        originalPrice,
        finalPrice
      });
    };
  
    const saveCombo = () => {
      if (!newCombo.name || !newCombo.description || !newCombo.offeringIds?.length) {
        toast.error('Por favor completa todos los campos obligatorios');
        return;
      }
      
      const combo = {
        id: Date.now().toString(),
        name: newCombo.name,
        description: newCombo.description || '',
        discount: newCombo.discount || 0,
        offeringIds: newCombo.offeringIds || [],
        originalPrice: newCombo.originalPrice || 0,
        finalPrice: newCombo.finalPrice || 0,
        active: true
      };
      
      setComboOfferings([...comboOfferings, combo]);
      setNewCombo({
        name: 'Pack Básico',
        description: '',
        discount: 0,
        offeringIds: [],
        originalPrice: 0,
        finalPrice: 0,
        active: true
      });
      setIsCreatingCombo(false);
      toast.success(`Combo "${combo.name}" creado exitosamente`);
    };
  
    const removeCombo = (id) => {
      const combo = comboOfferings.find(c => c.id === id);
      setComboOfferings(comboOfferings.filter(combo => combo.id !== id));
      toast.success(`Combo "${combo?.name}" eliminado exitosamente`);
    };
  
    const toggleComboStatus = (id) => {
      setComboOfferings(combos => 
        combos.map(combo => {
          if (combo.id === id) {
            const newStatus = !combo.active;
            toast.success(`Combo ${newStatus ? 'activado' : 'desactivado'} exitosamente`);
            return { ...combo, active: newStatus };
          }
          return combo;
        })
      );
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
            content: <Bookings upcomingBookings={upcomingBookings} />,
          },
          {
            value: 'schedule',
            label: 'Horarios',
            content: (
              <Schedule
                schedule={schedule}
                isScheduleModalOpen={isScheduleModalOpen}
                setIsScheduleModalOpen={setIsScheduleModalOpen}
                handleScheduleUpdate={handleScheduleUpdate}
              />
            ),
          },
          {
            value: 'services',
            label: 'Gestión de Servicios',
            content: (
              <ServicesManagement
                serviceSearchTerm={serviceSearchTerm}
                setServiceSearchTerm={setServiceSearchTerm}
                serviceStatusFilter={serviceStatusFilter}
                setServiceStatusFilter={setServiceStatusFilter}
                serviceRoleFilter={serviceRoleFilter}
                setServiceRoleFilter={setServiceRoleFilter}
                filteredServices={filteredServices}
                getServiceIcon={getServiceIcon}
                getServiceNameSpanish={getServiceNameSpanish}
                formatCurrency={formatCurrency}
                getRoleNameSpanish={getRoleNameSpanish}
                getStatusColor={getStatusColor}
                setSelectedService={setSelectedService}
                setIsServiceModalOpen={setIsServiceModalOpen}
                handleServiceAction={handleServiceAction}
                removeService={removeService}
                setIsCreatingService={setIsCreatingService}
              />
            ),
          },
          {
            value: 'discounts',
            label: 'Descuentos de Planes',
            content: (
              <Discounts
                planDiscountRules={planDiscountRules}
                setIsEditingRule={setIsEditingRule}
                updateDiscountRule={updateDiscountRule}
                removeDiscountRule={removeDiscountRule}
                saveDiscountRules={saveDiscountRules}
              />
            ),
          },
          {
            value: 'combos',
            label: 'Combos de Servicios',
            content: (
              <ComboOffering
                comboOfferings={comboOfferings}
                setIsCreatingCombo={setIsCreatingCombo}
                formatCurrency={formatCurrency}
                getStatusColor={getStatusColor}
                toggleComboStatus={toggleComboStatus}
                removeCombo={removeCombo}
                availableServices={availableServices}
              />
            ),
          },
          {
            value: 'simulator',
            label: 'Simulador de Reservas',
            content: <Simulator/>
          },
          {
            value: 'profile',
            label: 'Perfil',
            content: <Profile user={user} />,
          },
        ]}
      />

      {/* Modal para crear servicio extraído a componente */}
      <CreateService
        open={isCreatingService}
        onOpenChange={setIsCreatingService}
        newService={newService}
        setNewService={setNewService}
        saveService={saveService}
      />

      {/* Modal para editar servicio extraído a componente */}
      <EditService
        open={isServiceModalOpen}
        onOpenChange={setIsServiceModalOpen}
        selectedService={selectedService}
        setSelectedService={setSelectedService}
        updateService={updateService}
        getServiceNameSpanish={getServiceNameSpanish}
      />

      {/* Modal para crear regla de descuento extraído a componente */}
      <CreateDiscount
        open={isEditingRule}
        onOpenChange={setIsEditingRule}
        newRule={newRule}
        setNewRule={setNewRule}
        addDiscountRule={addDiscountRule}
      />

      {/* Modal para crear combo extraído a componente */}
      <CreateCombo
        open={isCreatingCombo}
        onOpenChange={setIsCreatingCombo}
        newCombo={newCombo}
        setNewCombo={setNewCombo}
        saveCombo={saveCombo}
        availableServices={availableServices}
        handleOfferingToggle={handleOfferingToggle}
        handleComboDiscountChange={handleComboDiscountChange}
        formatCurrency={formatCurrency}
      />
      
    </div>
  );
}