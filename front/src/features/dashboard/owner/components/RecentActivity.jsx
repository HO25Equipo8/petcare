import { Card, CardHeader, CardTitle, CardContent } from '../../../../shared/ui/Card';
import { Star } from 'lucide-react';

export default function RecentActivityCardOwner({ recentActivity }) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Actividad Reciente</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {recentActivity.map((activity) => (
          <div key={activity.id} className="flex items-start space-x-3">
            <div className="w-2 h-2 bg-primary rounded-full mt-2"></div>
            <div className="flex-1">
              <p className="font-medium">{activity.activity}</p>
              <p className="text-sm text-gray-600">
                {activity.pet} • {activity.caregiver}
              </p>
              <div className="flex items-center justify-between mt-1">
                <span className="text-xs text-gray-500">{activity.time}</span>
                <div className="flex items-center">
                  {[...Array(activity.rating)].map((_, i) => (
                    <Star key={i} className="w-3 h-3 text-yellow-400 fill-current" />
                  ))}
                </div>
              </div>
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  );
}
