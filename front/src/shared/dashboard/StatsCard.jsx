import { Card, CardContent } from '../ui/Card';

const StatsCard = ({
  title,
  value,
  icon,
  valueColor = '',
  additionalText,
  titleColor = 'text-muted-foreground'
}) => {
  return (
    <Card>
      <CardContent className="p-6">
        <div className="flex items-center justify-between">
          <div>
            <p className={`text-sm ${titleColor}`}>{title}</p>
            <p className={`text-2xl font-semibold ${valueColor}`}>{value}</p>
            {additionalText && additionalText}
          </div>
          {icon}
        </div>
      </CardContent>
    </Card>
  );
};

export default StatsCard;