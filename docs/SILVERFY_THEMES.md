# Silverfy themes

The single source of truth for the theme set shared by the Android app, the web player and the browser extension.
Every platform uses the same ids, names and colours. Keep them in sync when one changes.

`classic` is the original look and stays the default, so existing users see no change until they pick a theme.

| Section | id | Name | Mode | bg | surface | surface2 | text | text2 | accent | onAccent | accent2 | divider | Web font |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| — | `classic` | Classic | follows system | (existing tokens) | | | | | | | | | DM Sans |
| Soft & Cute | `kawaii` | Kawaii | light | #FFF4F8 | #FFFFFF | #FFE3EE | #4A2B3A | #8A6676 | #FF5C9D | #FFFFFF | #8FD3FF | #F5CFDD | Quicksand |
| Soft & Cute | `cottagecore` | Cottagecore | light | #F6F1E4 | #FFFBF2 | #ECE3CC | #3B3326 | #6E6450 | #5E8146 | #FFFFFF | #D98E73 | #DDD2B8 | Lora |
| Soft & Cute | `coquette` | Coquette | light | #FFF0F3 | #FFF9FA | #F9D9E0 | #5A1E2E | #9A5B6B | #B5395B | #FFFFFF | #F4B6C2 | #F0CDD5 | Playfair Display |
| Futuristic & Retro | `cyberpunk` | Cyberpunk | dark | #0A0A12 | #131322 | #1D1D33 | #EAF6FF | #8C9BB5 | #FCEE0A | #0A0A12 | #00F0FF | #2A2A44 | Rajdhani |
| Futuristic & Retro | `vaporwave` | Vaporwave | dark | #1B1035 | #261649 | #33205E | #FDF0FF | #C3A8E0 | #FF71CE | #1B1035 | #01CDFE | #3F2A6E | Space Grotesk |
| Futuristic & Retro | `nineties` | 90s Kid | light | #FFF8E7 | #FFFFFF | #E9F3FF | #1E1E3F | #55557A | #7B2FF7 | #FFFFFF | #00B8A0 | #E2DCCB | Fredoka |
| Artistic & Indie | `minimalist` | Minimalist | light | #FAFAFA | #FFFFFF | #F0F0F0 | #111111 | #6B6B6B | #111111 | #FFFFFF | #9A9A9A | #E6E6E6 | Inter |
| Artistic & Indie | `indie` | Indie Kid | light | #FFF6DA | #FFFDF4 | #FCE9B0 | #262017 | #6B5F45 | #F0532F | #FFFFFF | #2FA84F | #EADBA8 | Rubik |
| Artistic & Indie | `boho` | Boho | light | #F5EBDD | #FBF5EC | #EBDCC6 | #3E2A1E | #7A6250 | #B85F37 | #FFFFFF | #7F8F50 | #E0CFB6 | Josefin Sans |
| Mystical & Fantasy | `witchcore` | Witchcore | dark | #14111A | #1E1926 | #2A2234 | #EDE6F2 | #A898B5 | #D4AF37 | #14111A | #9B6BD6 | #352C40 | Cormorant Garamond |
| Mystical & Fantasy | `zodiac` | Zodiac | dark | #0B1026 | #131A3A | #1C2550 | #F1EEDD | #A9AFCF | #E8C872 | #0B1026 | #8FA8FF | #283266 | Marcellus |
| Mystical & Fantasy | `glowwave` | Glowwave | dark | #07141A | #0E2129 | #153039 | #E8FFFB | #8FC1BA | #4DFFC3 | #07141A | #FF4DD8 | #1E3E48 | Exo 2 |

Roles:

- **bg**: page or app background. **surface**: cards, sheets, player bar. **surface2**: hover, selected and secondary fills.
- **text** / **text2**: primary and secondary text. Tertiary text is `text2` at about 70% opacity.
- **accent**: buttons, active controls, progress, selection. **onAccent**: text and icons on accent. **accent2**: secondary highlights (tags, gradients, focus glow).
- **divider**: hairlines and borders.
- **Mode**: whether the theme is light or dark. A themed choice overrides the system light/dark setting; `classic` keeps following it.

Decorative touches (web and extension only, all pure CSS, never images):

- `nineties`: thick 2px borders on cards and a subtle confetti/Memphis-pattern page background using `accent`/`accent2` at low opacity.
- `cyberpunk` and `glowwave`: soft neon glow (`box-shadow` in accent) on the primary buttons and the progress bar.
- `vaporwave`: a gradient page background from bg towards #2B1559 and a sunset gradient (accent→accent2) on the primary button.
- `zodiac` and `witchcore`: a faint star-dot background pattern.
- `kawaii` and `coquette`: larger corner radii (pill buttons, cards +6px).
- `minimalist`: no gradients or shadows; radii reduced.

Storage keys: web `silverfy:theme` (localStorage), extension `silverfy.theme` (browser.storage.local), Android the app's own settings store.
