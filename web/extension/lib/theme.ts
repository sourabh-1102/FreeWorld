/*
 * Silverfy themes on the extension's own pages (docs/SILVERFY_THEMES.md).
 * The colours are variable sets in entrypoints/popup/style.css, which every
 * page loads; this names them, remembers the popup's choice and puts it on
 * <html data-theme> of the popup, discord and grant pages.
 */

export const THEME_KEY = 'silverfy.theme';

export interface ExtensionTheme {
  id: string;
  name: string;
  /** The spec's section; Classic stands on its own. */
  section: string;
  /** The Google Fonts family query (css2 `family=`); null for Classic's DM Sans, which every page loads. */
  font: string | null;
}

const W = ':wght@400;500;600;700';

export const THEMES: readonly ExtensionTheme[] = [
  { id: 'classic', name: 'Classic', section: 'Classic', font: null },
  { id: 'kawaii', name: 'Kawaii', section: 'Soft & Cute', font: `Quicksand${W}` },
  { id: 'cottagecore', name: 'Cottagecore', section: 'Soft & Cute', font: `Lora${W}` },
  { id: 'coquette', name: 'Coquette', section: 'Soft & Cute', font: `Playfair+Display${W}` },
  { id: 'cyberpunk', name: 'Cyberpunk', section: 'Futuristic & Retro', font: `Rajdhani${W}` },
  { id: 'vaporwave', name: 'Vaporwave', section: 'Futuristic & Retro', font: `Space+Grotesk${W}` },
  { id: 'nineties', name: '90s Kid', section: 'Futuristic & Retro', font: `Fredoka${W}` },
  { id: 'minimalist', name: 'Minimalist', section: 'Artistic & Indie', font: `Inter${W}` },
  { id: 'indie', name: 'Indie Kid', section: 'Artistic & Indie', font: `Rubik${W}` },
  { id: 'boho', name: 'Boho', section: 'Artistic & Indie', font: `Josefin+Sans${W}` },
  { id: 'witchcore', name: 'Witchcore', section: 'Mystical & Fantasy', font: `Cormorant+Garamond${W}` },
  // Marcellus has a single weight; asking for others makes Google Fonts refuse the request.
  { id: 'zodiac', name: 'Zodiac', section: 'Mystical & Fantasy', font: 'Marcellus' },
  { id: 'glowwave', name: 'Glowwave', section: 'Mystical & Fantasy', font: `Exo+2${W}` },
];

const themeOf = (id: unknown): ExtensionTheme => THEMES.find((t) => t.id === id) ?? THEMES[0]!;

/** Puts [id] on this page: the attribute the style sets hang on, and the theme's typeface. */
export function applyTheme(id: string): void {
  const theme = themeOf(id);
  if (theme.id === 'classic') delete document.documentElement.dataset.theme;
  else document.documentElement.dataset.theme = theme.id;
  if (theme.font && !document.querySelector(`link[data-theme-font="${theme.id}"]`)) {
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = `https://fonts.googleapis.com/css2?family=${theme.font}&display=swap`;
    link.dataset.themeFont = theme.id;
    document.head.append(link);
  }
}

export async function storedTheme(): Promise<string> {
  try {
    return themeOf((await browser.storage.local.get(THEME_KEY))[THEME_KEY]).id;
  } catch {
    return 'classic';
  }
}

export async function saveTheme(id: string): Promise<void> {
  applyTheme(id);
  try {
    await browser.storage.local.set({ [THEME_KEY]: themeOf(id).id });
  } catch {
    // Storage refused: the theme still holds on this page.
  }
}

/** Applies the saved theme now and follows changes made in the popup while this page is open. */
export function followTheme(): Promise<string> {
  browser.storage.onChanged.addListener((changes, area) => {
    if (area === 'local' && THEME_KEY in changes) applyTheme(String(changes[THEME_KEY]!.newValue ?? 'classic'));
  });
  return storedTheme().then((id) => {
    applyTheme(id);
    return id;
  });
}
