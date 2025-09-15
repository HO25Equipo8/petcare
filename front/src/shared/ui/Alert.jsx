import * as React from "react"
import { cva } from "class-variance-authority"
import { cn } from "./cn"

const alertVariants = cva(
  "relative w-full rounded-lg border px-4 py-3 text-sm [&>svg+div]:translate-y-[-3px] [&>svg]:absolute [&>svg]:left-4 [&>svg]:top-4 [&>svg]:text-foreground [&>svg~*]:pl-7",
  {
    variants: {
      variant: {
        default: "bg-surface-cards text-text-primary border-border-dividers",
        
        // Alertas por estado
        success: "border-primary-cta/50 bg-background-emphasis text-primary-cta [&>svg]:text-primary-cta",
        destructive: "border-destructive-error/50 bg-red-3 text-destructive-error [&>svg]:text-destructive-error",
        error: "border-destructive-error/50 bg-red-3 text-destructive-error [&>svg]:text-destructive-error",
        info: "border-informational-info/50 bg-blue-3 text-informational-info [&>svg]:text-informational-info",
        warning: "border-warning-alert/50 bg-amber-3 text-warning-alert [&>svg]:text-warning-alert",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
)


const Alert = React.forwardRef((
  { className, variant, ...props }, ref
) => (
  <div
    ref={ref}
    role="alert"
    className={cn(alertVariants({ variant }), className)}
    {...props}
  />
));
Alert.displayName = "Alert";


const AlertTitle = React.forwardRef((
  { className, ...props }, ref
) => (
  <h5
    ref={ref}
    className={cn("mb-1 font-medium leading-none tracking-tight", className)}
    {...props}
  />
));
AlertTitle.displayName = "AlertTitle";


const AlertDescription = React.forwardRef((
  { className, ...props }, ref
) => (
  <div
    ref={ref}
    className={cn("text-sm [&_p]:leading-relaxed", className)}
    {...props}
  />
));
AlertDescription.displayName = "AlertDescription";

export { Alert, AlertTitle, AlertDescription }