import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva } from "class-variance-authority"
import { cn } from "./cn"

const ButtonVariants = cva(
  "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0",
  {
    variants: {
      variant: {
        // Principales
        default: "bg-primary-cta text-white shadow hover:bg-primary-hover",
        primary: "bg-primary-cta text-white shadow hover:bg-primary-hover",
        
        // Acento
        accent: "bg-accent-cta text-text-primary shadow hover:bg-accent-hover",
        secondary: "bg-accent-cta text-text-primary shadow hover:bg-accent-hover",
        
        // Status
        destructive: "bg-destructive-error text-white shadow-sm hover:bg-red-600",
        success: "bg-primary-cta text-white shadow-sm hover:bg-primary-hover",
        warning: "bg-warning-alert text-text-primary shadow-sm hover:bg-yellow-500",
        info: "bg-informational-info text-white shadow-sm hover:bg-blue-600",
        
        // Utilitarios
        outline: "border border-dividers bg-surface-cards shadow-sm hover:bg-background-general hover:text-text-primary",
        ghost: "hover:bg-background-general hover:text-text-primary",
        link: "text-primary-cta underline-offset-4 hover:underline",
      },
      size: {
        default: "h-9 px-4 py-2",
        sm: "h-8 rounded-md px-3 text-xs",
        lg: "h-10 rounded-md px-8",
        icon: "h-9 w-9",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
)



const Button = React.forwardRef(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp
        className={cn(ButtonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  }
);
Button.displayName = "Button"

export { Button, ButtonVariants }