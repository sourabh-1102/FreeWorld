import { create } from 'zustand';

/*
 * FreeWorld's visual themes (docs/FREEWORLD_THEMES.md, the binding spec for
 * every platform). The colours themselves are tokens in styles/tokens.css,
 * under [data-theme] on <html>; this file names them, remembers the choice
 * and loads a theme's typeface only when it's picked. index.html sets the
 * attribute before first paint from the same key, so a reload doesn't flash.
 */

export type ThemeId =
  | 'classic'
  | 'kawaii'
  | 'cottagecore'
  | 'coquette'
  | 'cyberpunk'
  | 'vaporwave'
  | 'nineties'
  | 'minimalist'
  | 'indie'
  | 'boho'
  | 'witchcore'
  | 'zodiac'
  | 'glowwave';

export interface Theme {
  id: ThemeId;
  name: string;
  /** The spec's section; Classic stands on its own. */
  section: string;
  /** null for Classic, which follows the system appearance. */
  mode: 'light' | 'dark' | null;
  /** The picker's preview: bg, surface, accent and accent2 from the spec (Classic's bg is a CSS gradient). */
  swatch: { bg: string; surface: string; accent: string; accent2: string };
  /** The Google Fonts family query (css2 `family=`); null for Classic's DM Sans, which index.html always loads. */
  font: string | null;
}

const W = ':wght@400;500;600;700';

export const THEMES: readonly Theme[] = [
  // Classic's preview is split light and dark: it follows the system.
  { id: 'classic', name: 'Classic', section: 'Classic', mode: null, swatch: { bg: 'linear-gradient(135deg, #ffffff 50%, #1f1f1f 50%)', surface: '#f2f2f2', accent: '#d60017', accent2: '#fa586a' }, font: null },
  { id: 'kawaii', name: 'Kawaii', section: 'Soft & Cute', mode: 'light', swatch: { bg: '#FFF4F8', surface: '#FFFFFF', accent: '#FF5C9D', accent2: '#8FD3FF' }, font: `Quicksand${W}` },
  { id: 'cottagecore', name: 'Cottagecore', section: 'Soft & Cute', mode: 'light', swatch: { bg: '#F6F1E4', surface: '#FFFBF2', accent: '#5E8146', accent2: '#D98E73' }, font: `Lora${W}` },
  { id: 'coquette', name: 'Coquette', section: 'Soft & Cute', mode: 'light', swatch: { bg: '#FFF0F3', surface: '#FFF9FA', accent: '#B5395B', accent2: '#F4B6C2' }, font: `Playfair+Display${W}` },
  { id: 'cyberpunk', name: 'Cyberpunk', section: 'Futuristic & Retro', mode: 'dark', swatch: { bg: '#0A0A12', surface: '#131322', accent: '#FCEE0A', accent2: '#00F0FF' }, font: `Rajdhani${W}` },
  { id: 'vaporwave', name: 'Vaporwave', section: 'Futuristic & Retro', mode: 'dark', swatch: { bg: '#1B1035', surface: '#261649', accent: '#FF71CE', accent2: '#01CDFE' }, font: `Space+Grotesk${W}` },
  { id: 'nineties', name: '90s Kid', section: 'Futuristic & Retro', mode: 'light', swatch: { bg: '#FFF8E7', surface: '#FFFFFF', accent: '#7B2FF7', accent2: '#00B8A0' }, font: `Fredoka${W}` },
  { id: 'minimalist', name: 'Minimalist', section: 'Artistic & Indie', mode: 'light', swatch: { bg: '#FAFAFA', surface: '#FFFFFF', accent: '#111111', accent2: '#9A9A9A' }, font: `Inter${W}` },
  { id: 'indie', name: 'Indie Kid', section: 'Artistic & Indie', mode: 'light', swatch: { bg: '#FFF6DA', surface: '#FFFDF4', accent: '#F0532F', accent2: '#2FA84F' }, font: `Rubik${W}` },
  { id: 'boho', name: 'Boho', section: 'Artistic & Indie', mode: 'light', swatch: { bg: '#F5EBDD', surface: '#FBF5EC', accent: '#B85F37', accent2: '#7F8F50' }, font: `Josefin+Sans${W}` },
  { id: 'witchcore', name: 'Witchcore', section: 'Mystical & Fantasy', mode: 'dark', swatch: { bg: '#14111A', surface: '#1E1926', accent: '#D4AF37', accent2: '#9B6BD6' }, font: `Cormorant+Garamond${W}` },
  // Marcellus has a single weight; asking for others makes Google Fonts refuse the request.
  { id: 'zodiac', name: 'Zodiac', section: 'Mystical & Fantasy', mode: 'dark', swatch: { bg: '#0B1026', surface: '#131A3A', accent: '#E8C872', accent2: '#8FA8FF' }, font: 'Marcellus' },
  { id: 'glowwave', name: 'Glowwave', section: 'Mystical & Fantasy', mode: 'dark', swatch: { bg: '#07141A', surface: '#0E2129', accent: '#4DFFC3', accent2: '#FF4DD8' }, font: `Exo+2${W}` },
];

/** The picker's groups, in the spec's order. */
export const THEME_SECTIONS = ['Classic', 'Soft & Cute', 'Futuristic & Retro', 'Artistic & Indie', 'Mystical & Fantasy'] as const;

const THEME_KEY = 'freeworld:theme';

const themeOf = (id: string | null): Theme | undefined => THEMES.find((t) => t.id === id);

function storedTheme(): ThemeId {
  try {
    return themeOf(localStorage.getItem(THEME_KEY))?.id ?? 'classic';
  } catch {
    return 'classic';
  }
}

/** Adds the theme's Google Fonts stylesheet once; Classic users never fetch any. */
function loadFont(theme: Theme): void {
  if (!theme.font || document.querySelector(`link[data-theme-font="${theme.id}"]`)) return;
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = `https://fonts.googleapis.com/css2?family=${theme.font}&display=swap`;
  link.dataset.themeFont = theme.id;
  document.head.append(link);
}

/** The browser chrome's colour: the theme's background, or index.html's own per-appearance values for Classic. */
function paintThemeColor(theme: Theme): void {
  for (const meta of document.querySelectorAll<HTMLMetaElement>('meta[name="theme-color"]')) {
    meta.dataset.classic ??= meta.content;
    meta.content = theme.mode ? theme.swatch.bg : meta.dataset.classic;
  }
}

function apply(id: ThemeId): void {
  const theme = themeOf(id)!;
  const root = document.documentElement;
  if (theme.mode) root.dataset.theme = theme.id;
  else delete root.dataset.theme;
  loadFont(theme);
  paintThemeColor(theme);
}

interface ThemeState {
  theme: ThemeId;
  setTheme: (theme: ThemeId) => void;
}

export const useTheme = create<ThemeState>()((set) => ({
  theme: storedTheme(),
  setTheme: (theme) => {
    try {
      if (theme === 'classic') localStorage.removeItem(THEME_KEY);
      else localStorage.setItem(THEME_KEY, theme);
    } catch {
      // Storage blocked: the theme still holds for this visit.
    }
    apply(theme);
    set({ theme });
  },
}));

apply(useTheme.getState().theme);

// Another tab picked a theme: follow it here too.
window.addEventListener('storage', (event) => {
  if (event.key !== THEME_KEY && event.key !== null) return;
  const theme = storedTheme();
  apply(theme);
  useTheme.setState({ theme });
});
