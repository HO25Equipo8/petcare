import { Avatar, AvatarFallback, AvatarImage } from '../../shared/ui/Avatar';
import { Button } from '../../shared/ui/Button';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../shared/ui/Card';
import { Badge } from '../../shared/ui/Badge';
import { AlertTriangle, ArrowRight, Award, Camera, Calendar, CheckCircle, Globe, MapPin, Play, Shield, Smartphone, Star, Heart, Users, Zap } from 'lucide-react';
import { Logo } from '../../shared/ui/Logo';

export function HomePage({ onNavigate, onLogin }) {

  const services = [
    {
      icon: MapPin,
      title: "Paseo de Perros",
      description: "Servicio profesional de paseo con rastreo GPS en tiempo real y fotos actualizadas",
      price: "Desde $25",
      duration: "30-60 min",
      features: ["Rastreo GPS", "Fotos en tiempo real", "Horarios flexibles", "Reporte del clima"],
      popular: true,
      color: "text-primary-500",
      bgColor: "bg-primary-50"
    },
    {
      icon: Heart,
      title: "Cuidado en Casa",
      description: "Cuidado amoroso en tu hogar cuando no estás, incluye alimentación y juegos",
      price: "Desde $50",
      duration: "Día completo",
      features: ["Visitas a domicilio", "Alimentación y medicinas", "Sesiones de juego", "Seguridad en casa"],
      popular: false,
      color: "text-coral-500",
      bgColor: "bg-coral-50"
    },
    {
      icon: Shield,
      title: "Hospedaje de Mascotas",
      description: "Cuidado nocturno en hogares verificados con atención 24/7 y actualizaciones",
      price: "Desde $75",
      duration: "Por noche",
      features: ["Ambiente hogareño", "Cuidado 24/7", "Reportes diarios", "Contacto de emergencia"],
      popular: false,
      color: "text-secondary-500",
      bgColor: "bg-secondary-50"
    },
    {
      icon: AlertTriangle,
      title: "Atención de Emergencia",
      description: "Cuidado de último minuto para situaciones inesperadas con respuesta inmediata",
      price: "Desde $40",
      duration: "Según necesidad",
      features: ["Reserva el mismo día", "Protocolo de emergencia", "Coordinación con veterinario", "Soporte 24/7"],
      popular: false,
      color: "text-accent-500",
      bgColor: "bg-accent-50"
    }
  ];

    const howItWorks = [
    {
      step: 1,
      title: "Crea tu Perfil",
      description: "Cuéntanos sobre las necesidades, preferencias y horarios de tu mascota",
      icon: Users,
      color: "text-primary-500"
    },
    {
      step: 2,
      title: "Busca Cuidadores",
      description: "Mira perfiles, calificaciones y disponibilidad de cuidadores verificados cerca de ti",
      icon: Globe,
      color: "text-secondary-500"
    },
    {
      step: 3,
      title: "Reserva y Conecta",
      description: "Agenda servicios, chatea con tu cuidador y deja instrucciones especiales",
      icon: Calendar,
      color: "text-accent-500"
    },
    {
      step: 4,
      title: "Sigue y Disfruta",
      description: "Recibe actualizaciones, fotos y rastreo GPS en tiempo real durante el servicio",
      icon: Smartphone,
      color: "text-coral-500"
    }
  ];

  const popularSitters = [
    {
      id: 1,
      name: "Ernesto Tapia",
      location: "Centro",
      rating: 4.9,
      reviews: 127,
      specialties: ["Perros grandes", "Mascotas mayores", "Medicación"],
      experience: "5 años",
      price: "$30/paseo",
      image: "https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face",
      verified: true,
      responseTime: "< 1 hora",
      completedJobs: 850
    },
    {
      id: 2,
      name: "Marcos Juárez",
      location: "Zona Media",
      rating: 4.8,
      reviews: 89,
      specialties: ["Cachorros", "Entrenamiento", "Multi-mascota"],
      experience: "3 años",
      price: "$28/paseo",
      image: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face",
      verified: true,
      responseTime: "< 2 horas",
      completedJobs: 520
    },
    {
      id: 3,
      name: "Sara Martinez",
      location: "Zona Oeste",
      rating: 5.0,
      reviews: 203,
      specialties: ["Gatos", "Perros pequeños", "Mascotas ansiosas"],
      experience: "4 años",
      price: "$32/paseo",
      image: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face",
      verified: true,
      responseTime: "< 30 min",
      completedJobs: 920
    }
  ];

  const dashboardFeatures = [
    {
      title: "Panel de Dueño",
      description: "Administra tus mascotas, reserva servicios y sigue sus actividades",
      features: ["Perfiles de mascotas", "Calendario de reservas", "Seguimiento en vivo", "Historial de pagos"],
      icon: Users,
      color: "border-primary-200 bg-primary-50"
    },
    {
      title: "Panel de Cuidador",
      description: "Acepta reservas, comunícate con clientes y gestiona tu agenda",
      features: ["Solicitudes de trabajo", "Mensajería con clientes", "Seguimiento de ganancias", "Planificador de agenda"],
      icon: Calendar,
      color: "border-secondary-200 bg-secondary-50"
    },
    {
      title: "Seguimiento en Vivo",
      description: "Rastreo GPS en tiempo real con fotos y actualizaciones durante los paseos",
      features: ["Mapa de ruta GPS", "Fotos con hora", "Resumen de actividad", "Reportes de incidentes"],
      icon: MapPin,
      color: "border-accent-200 bg-accent-50"
    }
  ];

  return (
    <div className="min-h-screen bg-background-primary">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-50 via-background-secondary to-secondary-50 py-20 sm:py-32">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-4xl mx-auto">
            {/* Logo principal en hero */}
            <div className="flex justify-center mb-8">
              <div className="w-24 h-24 text-primary-500 flex items-center justify-center">
                <Logo size={96} />
              </div>
            </div>
            
            <h1 className="text-4xl sm:text-6xl font-bold text-text-primary mb-6 font-headings">
              Cuidado de Mascotas
              <span className="text-primary-500 block">Totalmente Confiable</span>
            </h1>
            
            {/* Slogan destacado */}
            <p className="text-xl sm:text-2xl text-primary-600 mb-4 font-headings font-medium">
              Paseo seguro, cariño puro
            </p>
            
            <p className="text-lg text-text-secondary mb-8 max-w-2xl mx-auto">
              Conéctate con cuidadores de mascotas verificados en tu zona. Rastreo GPS en tiempo real, actualizaciones instantáneas y tranquilidad en cada paseo, visita y estancia..
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button 
                size="lg" 
                onClick={onLogin}
                className="bg-gradient-primary hover:shadow-primary text-text-inverse px-8 py-3"
              >
                Empieza Hoy
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Services Overview */}
      <section className="py-20 bg-background-secondary">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              Servicios Profesionales para Mascotas
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Desde paseos diarios hasta hospedaje nocturno, ofrecemos atención integral adaptada a las necesidades únicas de tu mascota.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {services.map((service, index) => {
              const Icon = service.icon;
              return (
                <Card key={index} className={`relative hover:shadow-lg transition-all duration-300 hover:-translate-y-1 ${service.bgColor} border-0`}>
                  {service.popular && (
                    <span className="absolute -top-2 -right-2 text-white bg-accent-500 rounded px-2 py-1 text-xs font-semibold shadow">
                      Más Popular
                    </span>
                  )}
                  <CardHeader className="text-center">
                    <div className={`w-12 h-12 ${service.color} mx-auto mb-4 flex items-center justify-center rounded-lg bg-white shadow-sm`}>
                      <Icon className="w-6 h-6" />
                    </div>
                    <CardTitle className="text-xl mb-2">{service.title}</CardTitle>
                    <div className="flex justify-between items-center text-sm text-text-secondary mb-2">
                      <span className="font-mono text-primary-600 font-medium">{service.price}</span>
                      <span>{service.duration}</span>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <CardDescription className="text-text-secondary mb-4 text-center">
                      {service.description}
                    </CardDescription>
                    <ul className="space-y-2">
                      {service.features.map((feature, featureIndex) => (
                        <li key={featureIndex} className="flex items-center text-sm text-text-secondary">
                          <CheckCircle className="w-4 h-4 text-primary-500 mr-2 flex-shrink-0" />
                          {feature}
                        </li>
                      ))}
                    </ul>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-20 bg-background-emphasis">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              ¿Cómo funciona PetCare?
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Comenzar es muy fácil. Sigue estos pasos para conectar con el cuidador ideal para tu mascota.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {howItWorks.map((step, index) => {
              const Icon = step.icon;
              return (
                <div key={index} className="text-center">
                  <div className={`w-16 h-16 ${step.color} mx-auto mb-6 flex items-center justify-center rounded-full bg-white shadow-md relative`}>
                    <Icon className="w-8 h-8" />
                  </div>
                  <h3 className="text-xl font-bold text-text-primary mb-3 font-headings">
                    {step.title}
                  </h3>
                  <p className="text-text-secondary">
                    {step.description}
                  </p>
                  {index < howItWorks.length - 1 && (
                    <div className="hidden lg:block absolute top-8 left-full w-full">
                      <ArrowRight className="w-6 h-6 text-primary-300 mx-auto" />
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* Popular Sitters */}
      <section className="py-20 bg-background-secondary">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              Conoce a nuestros cuidadores mejor valorados
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Nuestra comunidad de cuidadores verificados son profesionales apasionados que tratan a tus mascotas como familia.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {popularSitters.map((sitter) => (
              <Card key={sitter.id} className="hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
                <CardHeader>
                  <div className="flex items-center space-x-4">
                    <div className="relative">
                      <Avatar className="w-16 h-16">
                        <AvatarImage src={sitter.image} alt={sitter.name} />
                        <AvatarFallback>{sitter.name.split(' ').map(n => n[0]).join('')}</AvatarFallback>
                      </Avatar>
                      {sitter.verified && (
                        <div className="absolute -bottom-1 -right-1 w-6 h-6 bg-primary-500 rounded-full flex items-center justify-center">
                          <CheckCircle className="w-4 h-4 text-white" />
                        </div>
                      )}
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center space-x-2">
                        <h3 className="font-bold text-text-primary">{sitter.name}</h3>
                        <span className="badge badge-secondary text-xs">
                          {sitter.experience}
                        </span>
                      </div>
                      <div className="flex items-center text-sm text-text-secondary">
                        <MapPin className="w-4 h-4 mr-1" />
                        {sitter.location}
                      </div>
                      <div className="flex items-center space-x-4 mt-1">
                        <div className="flex items-center">
                          <Star className="w-4 h-4 fill-accent-500 text-accent-500 mr-1" />
                          <span className="font-medium text-sm">{sitter.rating}</span>
                          <span className="text-xs text-text-secondary ml-1">({sitter.reviews})</span>
                        </div>
                        <div className="text-primary-600 font-mono font-medium text-sm">
                          {sitter.price}
                        </div>
                      </div>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    <div>
                      <p className="text-sm text-text-secondary mb-2">Especialidades:</p>
                      <div className="flex flex-wrap gap-1">
                        {sitter.specialties.map((specialty, index) => (
                          <span key={index} className="inline-block border border-primary-200 bg-primary-50 rounded px-2 py-1 text-xs">
                            {specialty}
                          </span>
                        ))}
                      </div>
                    </div>
                    <div className="flex justify-between text-xs text-text-secondary">
                      <span>Respuesta: {sitter.responseTime}</span>
                      <span>{sitter.completedJobs} trabajos completados</span>
                    </div>
                    <Button 
                      className="w-full mt-4" 
                      onClick={onLogin}
                      size="sm"
                    >
                      Ver perfil
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>

          <div className="text-center mt-12">
            <Button 
              variant="outline" 
              size="lg"
              onClick={onLogin}
              className="px-8 py-3"
            >
              Ver todos los cuidadores
            </Button>
          </div>
        </div>
      </section>

      {/* Dashboard Preview */}
      <section className="py-20 bg-background-emphasis">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              Un panel poderoso para ti
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Administra todo desde un solo lugar. Seas dueño o cuidador, nuestros paneles intuitivos hacen el cuidado de mascotas simple y transparente.
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {dashboardFeatures.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <Card key={index} className={`${feature.color} border-2 hover:shadow-lg transition-shadow duration-300`}>
                  <CardHeader>
                    <div className="flex items-center space-x-3 mb-4">
                      <div className="w-10 h-10 bg-white rounded-lg flex items-center justify-center shadow-sm">
                        <Icon className="w-5 h-5 text-primary-600" />
                      </div>
                      <CardTitle className="text-xl">{feature.title}</CardTitle>
                    </div>
                    <CardDescription className="text-text-secondary">
                      {feature.description}
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <ul className="space-y-2">
                      {feature.features.map((item, itemIndex) => (
                        <li key={itemIndex} className="flex items-center text-sm text-text-secondary">
                          <Zap className="w-4 h-4 text-accent-500 mr-2 flex-shrink-0" />
                          {item}
                        </li>
                      ))}
                    </ul>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        </div>
      </section>

      {/* Trust & Safety */}
      <section className="py-20 bg-background-secondary">
        <div className="container mx-auto px-4">
          <div className="max-w-4xl mx-auto text-center">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-8 font-headings">
              La seguridad de tu mascota es nuestra prioridad
            </h2>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
              <div className="text-center">
                <Shield className="w-12 h-12 text-primary-500 mx-auto mb-4" />
                <h3 className="font-bold text-text-primary mb-2">Verificación de antecedentes</h3>
                <p className="text-text-secondary text-sm">Todos los cuidadores pasan por una verificación exhaustiva de antecedentes y referencias.</p>
              </div>
              <div className="text-center">
                <Award className="w-12 h-12 text-secondary-500 mx-auto mb-4" />
                <h3 className="font-bold text-text-primary mb-2">Asegurados y certificados</h3>
                <p className="text-text-secondary text-sm">Cada reserva está cubierta por seguro y protección certificada.</p>
              </div>
              <div className="text-center">
                <Camera className="w-12 h-12 text-accent-500 mx-auto mb-4" />
                <h3 className="font-bold text-text-primary mb-2">Actualizaciones en tiempo real</h3>
                <p className="text-text-secondary text-sm">Rastreo GPS, fotos y mensajería instantánea para mantenerte siempre conectado.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-gradient-to-r from-primary-500 to-secondary-500 text-text-inverse">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-3xl sm:text-4xl font-bold mb-4 font-headings">
            ¿Estás Listo para Brindarle a tu Mascota el Mejor Cuidado?

          </h2>
          <p className="text-xl mb-8 opacity-90">
            Únete a nuestra comunidad hoy y experimenta el cuidado de sus mascotas sin preocupaciones.
          </p>
          <Button 
            size="lg" 
            variant="secondary"
            onClick={onLogin}
            className="bg-background-secondary text-text-primary hover:bg-background-primary px-8 py-3"
          >
            Comienza tu Viaje
          </Button>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-neutral-800 text-neutral-200 py-12">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <div className="flex items-center mb-4">
                <div className="w-8 h-8 text-primary-400 mr-3">
                  <Logo size={32} />
                </div>
                <div>
                  <div className="font-semibold text-white font-headings">PetCare</div>
                  <div className="text-xs text-neutral-400">Paseo seguro, cariño puro</div>
                </div>
              </div>
              <p className="text-neutral-400 text-sm">
                Servicios profesionales de cuidado de mascotas en los que puede confiar, con seguimiento en tiempo real y cuidadores verificados.
              </p>
            </div>
            
            <div>
              <h3 className="font-semibold text-white mb-4">Servicios</h3>
              <ul className="space-y-2 text-sm text-neutral-400">
                <li>Paseo de Perros</li>
                <li>Cuidado en Casa</li>
                <li>Guardería de Mascotas</li>
                <li>Cuidado de Emergencia</li>
              </ul>
            </div>
            
            <div>
              <h3 className="font-semibold text-white mb-4">Para Cuidadores</h3>
              <ul className="space-y-2 text-sm text-neutral-400">
                <li>Conviértete en Cuidador</li>
                <li>Recursos para Cuidadores</li>
                <li>Capacitación y Certificación</li>
                <li>Guías de Seguridad</li>
              </ul>
            </div>
            
            <div>
              <h3 className="font-semibold text-white mb-4">Soporte</h3>
              <ul className="space-y-2 text-sm text-neutral-400">
                <li>Centro de Ayuda</li>
                <li>Contáctanos</li>
                <li>Seguridad</li>
                <li>Términos de Servicio</li>
              </ul>
            </div>
          </div>
          
          <div className="border-t border-neutral-700 mt-8 pt-8 text-center text-sm text-neutral-400">
            <p>&copy; 2025 PetCare Platform. Todos los derechos reservados.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}