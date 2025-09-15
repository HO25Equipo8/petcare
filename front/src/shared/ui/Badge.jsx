import { cva } from "class-variance-authority"
import { cn } from "./cn"

const badgeVariants = cva(
  "inline-flex items-center rounded-md border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
  {
    variants: {
      variant: {
        default: "border-transparent bg-primary-cta text-white shadow hover:bg-primary-hover",
        
        // Badges de estado
        available: "border-transparent bg-background-emphasis text-primary-cta",
        busy: "border-transparent bg-amber-3 text-accent-cta",
        inactive: "border-transparent bg-border-dividers text-text-secondary",
        
        // Principal y acento
        primary: "border-transparent bg-primary-cta text-white shadow hover:bg-primary-hover",
        accent: "border-transparent bg-accent-cta text-text-primary shadow hover:bg-accent-hover",
        
        // Colores por status
        success: "border-transparent bg-background-emphasis text-primary-cta border-primary-cta",
        destructive: "border-transparent bg-red-3 text-destructive-error border-destructive-error",
        error: "border-transparent bg-destructive-error text-white shadow hover:bg-red-600",
        warning: "border-transparent bg-amber-3 text-warning-alert border-warning-alert",
        info: "border-transparent bg-blue-3 text-informational-info border-informational-info",
        
        // Utilitarios
        outline: "text-text-primary border-border-dividers",
        secondary: "border-transparent bg-background-general text-text-secondary",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
)



function Badge({ className, variant, ...props }) {
  return (
    <div className={cn(badgeVariants({ variant }), className)} {...props} />
  );
}

export { Badge, badgeVariants }