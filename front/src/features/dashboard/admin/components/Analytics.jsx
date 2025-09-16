import { Card, CardHeader, CardTitle, CardContent } from '../../../../shared/ui/Card';

const AnalyticsSubTabAdmin = ({ stats }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card>
        <CardHeader>
          <CardTitle>Métricas de Crecimiento</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <span>Nuevos usuarios este mes</span>
              <span className="font-semibold text-green-600">+127</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Retención de usuarios</span>
              <span className="font-semibold">84%</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Calificación promedio</span>
              <span className="font-semibold">{stats.averageRating} ⭐</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Servicios completados</span>
              <span className="font-semibold">{stats.totalBookings.toLocaleString()}</span>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Ingresos y Comisiones</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <span>Ingresos totales</span>
              <span className="font-semibold">${stats.totalRevenue.toLocaleString()}</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Comisiones de plataforma (10%)</span>
              <span className="font-semibold">${(stats.totalRevenue * 0.1).toLocaleString()}</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Crecimiento mensual</span>
              <span className="font-semibold text-green-600">+8.2%</span>
            </div>
            <div className="flex justify-between items-center">
              <span>Servicios activos</span>
              <span className="font-semibold">{stats.activeBookings}</span>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default AnalyticsSubTabAdmin;
