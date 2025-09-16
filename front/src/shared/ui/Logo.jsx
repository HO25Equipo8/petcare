export function Logo({ size = 32, className = "" }) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 256 256"
      xmlns="http://www.w3.org/2000/svg"
      role="img"
      aria-labelledby="petcare-logo-title petcare-logo-desc"
      className={className}
    >
      <title id="petcare-logo-title">Logotipo plataforma de cuidado de mascotas</title>
      <desc id="petcare-logo-desc">Escudo geométrico con una huella de mascota y una marca de verificación, en estilo minimalista.</desc>

      {/* Estilo */}
      <defs>
        <style>
          {`
            .logo-stroke { 
              stroke: currentColor; 
              fill: none; 
              stroke-width: 12; 
              stroke-linecap: round; 
              stroke-linejoin: round; 
            }
            .logo-fill { 
              fill: currentColor; 
            }
          `}
        </style>
      </defs>

      {/* Escudo */}
      <path 
        className="logo-stroke"
        d="M128 24
           L216 60
           V132
           c0 52-38 86-88 100
           c-50-14-88-48-88-100
           V60
           Z" 
      />

      {/* Huella de pata */}
      {/* Almohadilla */}
      <circle className="logo-stroke" cx="128" cy="120" r="28"/>
      
      {/* Dedos de huella */}
      <circle className="logo-stroke" cx="96"  cy="92" r="12"/>
      <circle className="logo-stroke" cx="160" cy="92" r="12"/>
      <circle className="logo-stroke" cx="112" cy="68" r="12"/>
      <circle className="logo-stroke" cx="144" cy="68" r="12"/>

      {/* Paloma */}
      <path className="logo-stroke" d="M88 164 L120 192 L172 140"/>
    </svg>
  );
}