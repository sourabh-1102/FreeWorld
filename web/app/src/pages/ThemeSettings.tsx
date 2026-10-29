import { Check } from 'lucide-react';
import { THEME_SECTIONS, THEMES, useTheme, type Theme } from '@/state/theme';
import { settingsUi } from './settingsStyles';

/** One theme: its colours in miniature, the name, and a radio that applies it on change. */
function ThemeCard({ theme, active }: { theme: Theme; active: boolean }) {
  const { bg, surface, accent, accent2 } = theme.swatch;
  return (
    <label className={settingsUi.themeCard}>
      <input
        type="radio"
        name="theme"
        className="sr-only"
        value={theme.id}
        checked={active}
        onChange={() => useTheme.getState().setTheme(theme.id)}
      />
      <span className={settingsUi.swatch} style={{ background: bg }} aria-hidden="true">
        <span className={settingsUi.swatchSurface} style={{ backgroundColor: surface }}>
          <span className={settingsUi.swatchAccent} style={{ backgroundColor: accent }} />
          <span className={settingsUi.swatchAccent2} style={{ backgroundColor: accent2 }} />
        </span>
      </span>
      <span className={settingsUi.themeName}>
        {theme.name}
        {active && <Check size={15} strokeWidth={2.6} className={settingsUi.themeCheck} aria-hidden="true" />}
      </span>
    </label>
  );
}

/** Appearance → Theme: Classic, then the spec's four sections. Arrow keys move between themes, as in any radio group. */
export function ThemeSettings() {
  const current = useTheme((s) => s.theme);
  return (
    <section className={settingsUi.themeGroup} role="radiogroup" aria-labelledby="theme-label">
      <div className={settingsUi.themeHeader}>
        <span id="theme-label">Theme</span>
        <span className={settingsUi.value}>{THEMES.find((theme) => theme.id === current)?.name}</span>
      </div>
      {THEME_SECTIONS.map((section) => (
        <div key={section}>
          <h3 className={settingsUi.themeSection}>{section}</h3>
          <div className={settingsUi.themeGrid}>
            {THEMES.filter((theme) => theme.section === section).map((theme) => (
              <ThemeCard key={theme.id} theme={theme} active={theme.id === current} />
            ))}
          </div>
        </div>
      ))}
    </section>
  );
}
