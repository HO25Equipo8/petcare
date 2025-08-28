import { Button } from '../components/ui/Button.jsx';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card.jsx';
import { Badge } from '../components/ui/Badge.jsx';
import { MapPin, Clock, Shield, Star, Heart } from 'lucide-react';
import { Logo } from '../components/ui/Logo.jsx';



export function LandingPage({ onNavigate, onLogin }) {
  const features = [
    {
      icon: MapPin,
  title: "Rastreo GPS en Vivo",
  description: "Sigue el paseo de tu mascota en tiempo real con nuestra tecnología GPS avanzada",
      color: "text-primary-500"
    },
    {
      icon: Shield,
  title: "Cuidadores de Confianza",
  description: "Todos nuestros cuidadores están verificados, asegurados y con antecedentes revisados",
      color: "text-secondary-500"
    },
    {
      icon: Clock,
  title: "Horarios Flexibles",
  description: "Reserva servicios que se adapten a tu agenda, desde paseos diarios hasta estancias nocturnas",
      color: "text-accent-500"
    },
    {
      icon: Heart,
  title: "Cuidado Personalizado",
  description: "Cada mascota recibe atención personalizada según sus necesidades únicas",
      color: "text-coral-500"
    }
  ];

  const services = [
    {
  title: "Paseo de Perros",
  description: "Servicio profesional de paseo con rastreo GPS en tiempo real",
  price: "Desde $25/paseo",
  duration: "30-60 minutos",
  features: ["Rastreo GPS", "Actualizaciones con fotos", "Horarios flexibles"]
    },
    {
  title: "Cuidado en Casa",
  description: "Cuidado de mascotas en tu hogar cuando no estás",
  price: "Desde $50/día",
  duration: "Cuidado todo el día",
  features: ["Visitas a domicilio", "Alimentación y medicación", "Tiempo de juego"]
    },
    {
  title: "Guardería de Mascotas",
  description: "Estancia nocturna en hogares certificados de nuestros socios",
  price: "Desde $75/noche",
  duration: "Estancias nocturnas",
  features: ["Ambiente hogareño", "Cuidado 24/7", "Actualizaciones diarias"]
    }
  ];

  const testimonials = [
    {
      name: "Sara Juárez",
      role: "Dog Owner",
  content: "¡PetCare ha sido un salvavidas! El rastreo GPS me da tranquilidad y Max ama sus paseos diarios con Emma.",
      rating: 5
    },
    {
      name: "Miguel Restrepo",
      role: "Cat Owner",
  content: "El servicio de cuidado en casa es fantástico. Luna recibe atención personalizada mientras viajo por trabajo.",
      rating: 5
    },
    {
      name: "Lisa Rodriguez",
      role: "Pet Sitter",
  content: "Trabajar con PetCare ha sido increíble. La plataforma facilita conectar con dueños y gestionar reservas.",
      rating: 5
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
              <Button 
                size="lg" 
                variant="outline" 
                onClick={() => onNavigate('caregivers')}
                className="px-8 py-3"
              >
                Encuentra Cuidadores
              </Button>
            </div>
          </div>
        </div>

        {/* Decorative background elements */}
        <div className="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none">
          <div className="absolute top-20 left-10 w-20 h-20 bg-primary-200 rounded-full opacity-20 animate-pulse"></div>
          <div className="absolute top-40 right-20 w-16 h-16 bg-secondary-200 rounded-full opacity-20 animate-pulse delay-1000"></div>
          <div className="absolute bottom-20 left-20 w-24 h-24 bg-accent-200 rounded-full opacity-20 animate-pulse delay-2000"></div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-background-secondary">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              ¿Por qué elegir PetCare?
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Somos más que un simple servicio de cuidado de mascotas. Somos tu aliado de confianza para mantener a tus peludos amigos felices, sanos y seguros.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <Card key={index} className="text-center hover:shadow-lg transition-shadow duration-300">
                  <CardHeader>
                    <div className={`w-12 h-12 ${feature.color} mx-auto mb-4 flex items-center justify-center rounded-lg bg-background-emphasis`}>
                      <Icon className="w-6 h-6" />
                    </div>
                    <CardTitle className="text-xl mb-2">{feature.title}</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <CardDescription className="text-text-secondary">
                      {feature.description}
                    </CardDescription>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        </div>
      </section>

      {/* Services Section */}
      <section className="py-20 bg-background-emphasis">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              Nuestros Servicios
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Servicios integrales de cuidado de mascotas adaptados a las necesidades de tu mascota y a su estilo de vida.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {services.map((service, index) => (
              <Card key={index} className="hover:shadow-lg transition-shadow duration-300">
                <CardHeader>
                  <div className="flex justify-between items-start mb-2">
                    <CardTitle className="text-xl">{service.title}</CardTitle>
                    <Badge variant="secondary" className="bg-primary-100 text-primary-700">
                      {service.duration}
                    </Badge>
                  </div>
                  <CardDescription className="text-text-secondary mb-4">
                    {service.description}
                  </CardDescription>
                  <div className="text-2xl font-bold text-primary-600 font-headings">
                    {service.price}
                  </div>
                </CardHeader>
                <CardContent>
                  <ul className="space-y-2">
                    {service.features.map((feature, featureIndex) => (
                      <li key={featureIndex} className="flex items-center text-text-secondary">
                        <div className="w-1.5 h-1.5 bg-primary-500 rounded-full mr-3"></div>
                        {feature}
                      </li>
                    ))}
                  </ul>
                  <Button className="w-full mt-6" onClick={onLogin}>
                    Book Now
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="py-20 bg-background-secondary">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4 font-headings">
              Lo Que Dice Nuestra Comunidad
            </h2>
            <p className="text-lg text-text-secondary max-w-2xl mx-auto">
              Únete a miles de dueños y cuidadores de mascotas felices que confían en PetCare.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {testimonials.map((testimonial, index) => (
              <Card key={index} className="hover:shadow-lg transition-shadow duration-300">
                <CardHeader>
                  <div className="flex items-center space-x-1 mb-3">
                    {[...Array(testimonial.rating)].map((_, i) => (
                      <Star key={i} className="w-4 h-4 fill-accent-500 text-accent-500" />
                    ))}
                  </div>
                  <CardDescription className="text-text-primary italic text-base leading-relaxed">
                    "{testimonial.content}"
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="font-medium text-text-primary">{testimonial.name}</div>
                  <div className="text-text-secondary text-sm">{testimonial.role}</div>
                </CardContent>
              </Card>
            ))}
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