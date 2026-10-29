package com.music.bitchord.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import com.music.bitchord.R
import com.music.bitchord.data.settings.AppSettings

/** The picker's groups, in the order docs/SILVERFY_THEMES.md lists them. */
enum class ThemeSection(@StringRes val label: Int) {
    SOFT_CUTE(R.string.theme_section_soft_cute),
    FUTURISTIC_RETRO(R.string.theme_section_futuristic_retro),
    ARTISTIC_INDIE(R.string.theme_section_artistic_indie),
    MYSTICAL_FANTASY(R.string.theme_section_mystical_fantasy),
}

/**
 * One Silverfy theme, in the spec's own roles rather than Material's.
 *
 * The web player and the extension read the same table, so the fields mirror
 * its columns one for one; [colorScheme] is where they are mapped onto
 * Material 3. Unlike Classic, a theme is either light or dark ([dark]) and
 * ignores the system and in-app light/dark choice.
 */
class SilverfyPalette(
    val id: String,
    @StringRes val name: Int,
    val section: ThemeSection,
    val dark: Boolean,
    val bg: Color,
    val surface: Color,
    val surface2: Color,
    val text: Color,
    val text2: Color,
    val accent: Color,
    val onAccent: Color,
    val accent2: Color,
    val divider: Color,
) {
    val colorScheme: ColorScheme by lazy { buildColorScheme() }

    private fun buildColorScheme(): ColorScheme {
        // Containers are the accent washed into the surface, so a filled chip
        // or selected row still reads as the theme's colour without shouting.
        val primaryContainer = accent.copy(alpha = 0.22f).compositeOver(surface)
        val secondaryContainer = accent2.copy(alpha = 0.22f).compositeOver(surface)
        val onAccent2 = contentOn(accent2)
        val between = lerp(surface, surface2, 0.5f)
        val highest = lerp(surface2, text, 0.06f)
        return if (dark) {
            darkColorScheme(
                primary = accent, onPrimary = onAccent,
                primaryContainer = primaryContainer, onPrimaryContainer = text,
                inversePrimary = accent,
                secondary = accent2, onSecondary = onAccent2,
                secondaryContainer = secondaryContainer, onSecondaryContainer = text,
                tertiary = accent2, onTertiary = onAccent2,
                tertiaryContainer = secondaryContainer, onTertiaryContainer = text,
                background = bg, onBackground = text,
                surface = surface, onSurface = text,
                surfaceVariant = surface2, onSurfaceVariant = text2,
                surfaceTint = accent,
                inverseSurface = text, inverseOnSurface = bg,
                outline = divider, outlineVariant = divider,
                surfaceBright = surface2, surfaceDim = bg,
                surfaceContainerLowest = bg, surfaceContainerLow = surface,
                surfaceContainer = between, surfaceContainerHigh = surface2,
                surfaceContainerHighest = highest,
            )
        } else {
            lightColorScheme(
                primary = accent, onPrimary = onAccent,
                primaryContainer = primaryContainer, onPrimaryContainer = text,
                inversePrimary = accent,
                secondary = accent2, onSecondary = onAccent2,
                secondaryContainer = secondaryContainer, onSecondaryContainer = text,
                tertiary = accent2, onTertiary = onAccent2,
                tertiaryContainer = secondaryContainer, onTertiaryContainer = text,
                background = bg, onBackground = text,
                surface = surface, onSurface = text,
                surfaceVariant = surface2, onSurfaceVariant = text2,
                surfaceTint = accent,
                inverseSurface = text, inverseOnSurface = bg,
                outline = divider, outlineVariant = divider,
                surfaceBright = surface, surfaceDim = surface2,
                surfaceContainerLowest = surface, surfaceContainerLow = bg,
                surfaceContainer = between, surfaceContainerHigh = surface2,
                surfaceContainerHighest = highest,
            )
        }
    }
}

/** The spec gives no on-colour for accent2, so it is whichever of black or white reads. */
private fun contentOn(color: Color): Color =
    if (color.luminance() > 0.4f) Color(0xFF111111) else Color.White

/**
 * The theme being painted, or null for Classic. For the few surfaces whose
 * Classic colours are fixed values rather than colour-scheme roles: they keep
 * those values under Classic, so it stays exactly as it was, and take the
 * theme's own role under any other.
 */
val LocalSilverfyPalette = staticCompositionLocalOf<SilverfyPalette?> { null }

/** Every theme in docs/SILVERFY_THEMES.md except Classic, in table order. */
object SilverfyThemes {
    const val CLASSIC = AppSettings.DEFAULT_COLOR_THEME

    val all: List<SilverfyPalette> = listOf(
        SilverfyPalette(
            "kawaii", R.string.theme_kawaii, ThemeSection.SOFT_CUTE, dark = false,
            bg = Color(0xFFFFF4F8), surface = Color(0xFFFFFFFF), surface2 = Color(0xFFFFE3EE),
            text = Color(0xFF4A2B3A), text2 = Color(0xFF8A6676),
            accent = Color(0xFFFF5C9D), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFF8FD3FF),
            divider = Color(0xFFF5CFDD),
        ),
        SilverfyPalette(
            "cottagecore", R.string.theme_cottagecore, ThemeSection.SOFT_CUTE, dark = false,
            bg = Color(0xFFF6F1E4), surface = Color(0xFFFFFBF2), surface2 = Color(0xFFECE3CC),
            text = Color(0xFF3B3326), text2 = Color(0xFF6E6450),
            accent = Color(0xFF5E8146), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFFD98E73),
            divider = Color(0xFFDDD2B8),
        ),
        SilverfyPalette(
            "coquette", R.string.theme_coquette, ThemeSection.SOFT_CUTE, dark = false,
            bg = Color(0xFFFFF0F3), surface = Color(0xFFFFF9FA), surface2 = Color(0xFFF9D9E0),
            text = Color(0xFF5A1E2E), text2 = Color(0xFF9A5B6B),
            accent = Color(0xFFB5395B), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFFF4B6C2),
            divider = Color(0xFFF0CDD5),
        ),
        SilverfyPalette(
            "cyberpunk", R.string.theme_cyberpunk, ThemeSection.FUTURISTIC_RETRO, dark = true,
            bg = Color(0xFF0A0A12), surface = Color(0xFF131322), surface2 = Color(0xFF1D1D33),
            text = Color(0xFFEAF6FF), text2 = Color(0xFF8C9BB5),
            accent = Color(0xFFFCEE0A), onAccent = Color(0xFF0A0A12), accent2 = Color(0xFF00F0FF),
            divider = Color(0xFF2A2A44),
        ),
        SilverfyPalette(
            "vaporwave", R.string.theme_vaporwave, ThemeSection.FUTURISTIC_RETRO, dark = true,
            bg = Color(0xFF1B1035), surface = Color(0xFF261649), surface2 = Color(0xFF33205E),
            text = Color(0xFFFDF0FF), text2 = Color(0xFFC3A8E0),
            accent = Color(0xFFFF71CE), onAccent = Color(0xFF1B1035), accent2 = Color(0xFF01CDFE),
            divider = Color(0xFF3F2A6E),
        ),
        SilverfyPalette(
            "nineties", R.string.theme_nineties, ThemeSection.FUTURISTIC_RETRO, dark = false,
            bg = Color(0xFFFFF8E7), surface = Color(0xFFFFFFFF), surface2 = Color(0xFFE9F3FF),
            text = Color(0xFF1E1E3F), text2 = Color(0xFF55557A),
            accent = Color(0xFF7B2FF7), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFF00B8A0),
            divider = Color(0xFFE2DCCB),
        ),
        SilverfyPalette(
            "minimalist", R.string.theme_minimalist, ThemeSection.ARTISTIC_INDIE, dark = false,
            bg = Color(0xFFFAFAFA), surface = Color(0xFFFFFFFF), surface2 = Color(0xFFF0F0F0),
            text = Color(0xFF111111), text2 = Color(0xFF6B6B6B),
            accent = Color(0xFF111111), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFF9A9A9A),
            divider = Color(0xFFE6E6E6),
        ),
        SilverfyPalette(
            "indie", R.string.theme_indie, ThemeSection.ARTISTIC_INDIE, dark = false,
            bg = Color(0xFFFFF6DA), surface = Color(0xFFFFFDF4), surface2 = Color(0xFFFCE9B0),
            text = Color(0xFF262017), text2 = Color(0xFF6B5F45),
            accent = Color(0xFFF0532F), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFF2FA84F),
            divider = Color(0xFFEADBA8),
        ),
        SilverfyPalette(
            "boho", R.string.theme_boho, ThemeSection.ARTISTIC_INDIE, dark = false,
            bg = Color(0xFFF5EBDD), surface = Color(0xFFFBF5EC), surface2 = Color(0xFFEBDCC6),
            text = Color(0xFF3E2A1E), text2 = Color(0xFF7A6250),
            accent = Color(0xFFB85F37), onAccent = Color(0xFFFFFFFF), accent2 = Color(0xFF7F8F50),
            divider = Color(0xFFE0CFB6),
        ),
        SilverfyPalette(
            "witchcore", R.string.theme_witchcore, ThemeSection.MYSTICAL_FANTASY, dark = true,
            bg = Color(0xFF14111A), surface = Color(0xFF1E1926), surface2 = Color(0xFF2A2234),
            text = Color(0xFFEDE6F2), text2 = Color(0xFFA898B5),
            accent = Color(0xFFD4AF37), onAccent = Color(0xFF14111A), accent2 = Color(0xFF9B6BD6),
            divider = Color(0xFF352C40),
        ),
        SilverfyPalette(
            "zodiac", R.string.theme_zodiac, ThemeSection.MYSTICAL_FANTASY, dark = true,
            bg = Color(0xFF0B1026), surface = Color(0xFF131A3A), surface2 = Color(0xFF1C2550),
            text = Color(0xFFF1EEDD), text2 = Color(0xFFA9AFCF),
            accent = Color(0xFFE8C872), onAccent = Color(0xFF0B1026), accent2 = Color(0xFF8FA8FF),
            divider = Color(0xFF283266),
        ),
        SilverfyPalette(
            "glowwave", R.string.theme_glowwave, ThemeSection.MYSTICAL_FANTASY, dark = true,
            bg = Color(0xFF07141A), surface = Color(0xFF0E2129), surface2 = Color(0xFF153039),
            text = Color(0xFFE8FFFB), text2 = Color(0xFF8FC1BA),
            accent = Color(0xFF4DFFC3), onAccent = Color(0xFF07141A), accent2 = Color(0xFFFF4DD8),
            divider = Color(0xFF1E3E48),
        ),
    )

    /** The theme for a stored id, or null for Classic and for any id this build does not know. */
    fun byId(id: String): SilverfyPalette? = all.firstOrNull { it.id == id }
}
