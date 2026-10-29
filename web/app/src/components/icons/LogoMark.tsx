import markSvg from '@/assets/logo-mark.svg?raw';

// Silverfy's mark (the silver element tile with headphones and a waveform), the same art as the Android app,
// recoloured to currentColor so it follows the text colour it sits in.
const markup = markSvg.replace(/width="\d+" height="\d+"/, 'width="100%" height="100%"');

export function LogoMark({ size = 20, className }: { size?: number; className?: string }) {
  return (
    <span
      aria-hidden="true"
      className={className}
      style={{ display: 'inline-flex', width: size, height: size }}
      dangerouslySetInnerHTML={{ __html: markup }}
    />
  );
}
