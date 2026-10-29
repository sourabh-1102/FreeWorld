package com.music.bitchord.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

/**
 * Silverfy's play/pause glyph: an outlined triangle around "Ag" while paused,
 * two outlined bars either side of "Ag" while playing, crossfading between.
 *
 * Both are drawn in a 260-unit box centred on the origin. The silver gradient
 * is multiplied by [color], so the glyph takes on the colour of the surface it
 * sits on, like the icon it replaces.
 */
@Composable
fun SilverfyPlayMark(
    isPlaying: Boolean,
    color: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val playing by animateFloatAsState(if (isPlaying) 1f else 0f, label = "playMarkSwap")
    val paths = remember { MarkPaths() }
    val a11y = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            role = Role.Image
        }
    } else {
        Modifier
    }
    Canvas(modifier.then(a11y)) {
        val k = size.minDimension / BOX
        translate(size.width / 2f, size.height / 2f) {
            scale(k, k, pivot = Offset.Zero) {
                val metal = Brush.linearGradient(
                    0f to (METAL[0] * color), 0.5f to (METAL[1] * color), 1f to (METAL[2] * color),
                    start = Offset(-BOX / 2f, -BOX / 2f), end = Offset(BOX / 2f, BOX / 2f),
                )
                if (playing < 1f) drawPlay(paths, metal, 1f - playing)
                if (playing > 0f) drawPause(paths, metal, playing)
            }
        }
    }
}

private fun DrawScope.drawPlay(paths: MarkPaths, metal: Brush, alpha: Float) {
    drawPath(paths.triangle, metal, alpha, Stroke(width = STROKE, join = StrokeJoin.Round))
    drawPath(paths.playAg, metal, alpha)
}

private fun DrawScope.drawPause(paths: MarkPaths, metal: Brush, alpha: Float) {
    val stroke = Stroke(width = STROKE)
    for (left in listOf(-118f, 56f)) {
        drawRoundRect(metal, Offset(left, -117f), Size(62f, 235f), CornerRadius(31f), alpha, stroke)
    }
    drawPath(paths.pauseAg, metal, alpha)
}

private operator fun Color.times(tint: Color) =
    Color(red * tint.red, green * tint.green, blue * tint.blue, alpha * tint.alpha)

private val METAL = listOf(Color(0xFFF6F7F9), Color(0xFFC9CCD1), Color(0xFF8B8F96))

private const val BOX = 260f
private const val STROKE = 16f

private class MarkPaths {
    val triangle = Path().apply {
        moveTo(-106f, -116f)
        lineTo(104f, 0f)
        lineTo(-106f, 116f)
        close()
    }
    // Inter Bold "Ag", outlined at each glyph's size and place.
    val playAg = parse("M-67.05 29L-79.23 29L-60.84-24.84L-46.38-24.84L-27.52 29L-39.77 29L-43.93 16.50L-63.04 16.50L-67.05 29ZM-60.30 7.86L-46.78 7.86L-48.41 3.02Q-49.96-2.00-51.55-8L-51.55-8Q-52.60-11.87-53.72-16.20L-53.72-16.20Q-54.77-11.83-55.74-7.93L-55.74-7.93Q-57.26-1.89-58.74 3.02L-58.74 3.02L-60.30 7.86ZM-4.69 44.97L-4.69 44.97Q-9.71 44.97-13.47 43.69Q-17.22 42.41-19.57 40.17Q-21.92 37.92-22.82 35.07L-22.82 35.07L-13.50 32.47Q-13.00 33.59-11.93 34.65Q-10.86 35.72-9.09 36.41Q-7.32 37.09-4.72 37.09L-4.72 37.09Q-0.57 37.09 1.80 35.20Q4.17 33.30 4.17 29.29L4.17 29.29L4.17 21.85L3.34 21.85Q2.61 23.47 1.24 24.99Q-0.13 26.51-2.36 27.46Q-4.58 28.42-7.87 28.42L-7.87 28.42Q-12.42 28.42-16.14 26.29Q-19.86 24.16-22.08 19.79Q-24.31 15.41-24.31 8.73L-24.31 8.73Q-24.31 1.90-22.05-2.69Q-19.79-7.28-16.05-9.59Q-12.31-11.90-7.83-11.90L-7.83-11.90Q-4.43-11.90-2.12-10.76Q0.19-9.63 1.62-7.91Q3.05-6.19 3.77-4.60L3.77-4.60L4.24-4.60L4.24-11.40L14.90-11.40L14.90 28.67Q14.90 34.13 12.39 37.76Q9.88 41.39 5.47 43.18Q1.06 44.97-4.69 44.97ZM-4.47 20.18L-4.47 20.18Q-1.69 20.18 0.25 18.81Q2.18 17.44 3.19 14.85Q4.20 12.27 4.20 8.66L4.20 8.66Q4.20 5.08 3.19 2.41Q2.18-0.27 0.25-1.75Q-1.69-3.23-4.47-3.23L-4.47-3.23Q-7.32-3.23-9.26-1.69Q-11.19-0.16-12.18 2.51Q-13.18 5.19-13.18 8.66L-13.18 8.66Q-13.18 12.20-12.18 14.78Q-11.19 17.37-9.24 18.77Q-7.29 20.18-4.47 20.18Z")
    val pauseAg = parse("M-29.81 23L-40.01 23L-24.60-22.11L-12.49-22.11L3.31 23L-6.95 23L-10.43 12.53L-26.45 12.53L-29.81 23ZM-24.14 5.29L-12.82 5.29L-14.18 1.23Q-15.49-2.97-16.82-8L-16.82-8Q-17.70-11.24-18.63-14.87L-18.63-14.87Q-19.51-11.21-20.33-7.94L-20.33-7.94Q-21.60-2.88-22.84 1.23L-22.84 1.23L-24.14 5.29ZM22.45 36.38L22.45 36.38Q18.24 36.38 15.09 35.31Q11.94 34.23 9.98 32.35Q8.01 30.48 7.25 28.09L7.25 28.09L15.06 25.91Q15.48 26.84 16.38 27.74Q17.27 28.63 18.75 29.21Q20.24 29.78 22.42 29.78L22.42 29.78Q25.90 29.78 27.88 28.19Q29.86 26.60 29.86 23.24L29.86 23.24L29.86 17.01L29.17 17.01Q28.56 18.37 27.41 19.64Q26.26 20.91 24.40 21.71Q22.54 22.52 19.78 22.52L19.78 22.52Q15.97 22.52 12.85 20.73Q9.73 18.94 7.87 15.28Q6.01 11.62 6.01 6.02L6.01 6.02Q6.01 0.29 7.90-3.55Q9.79-7.39 12.93-9.33Q16.06-11.27 19.81-11.27L19.81-11.27Q22.66-11.27 24.60-10.32Q26.53-9.36 27.73-7.92Q28.93-6.49 29.53-5.15L29.53-5.15L29.93-5.15L29.93-10.85L38.86-10.85L38.86 22.73Q38.86 27.30 36.75 30.34Q34.65 33.38 30.95 34.88Q27.26 36.38 22.45 36.38ZM22.63 15.61L22.63 15.61Q24.96 15.61 26.58 14.46Q28.20 13.31 29.05 11.15Q29.90 8.98 29.90 5.96L29.90 5.96Q29.90 2.96 29.05 0.72Q28.20-1.52 26.58-2.76Q24.96-4.00 22.63-4.00L22.63-4.00Q20.24-4.00 18.62-2.72Q17.00-1.43 16.17 0.81Q15.33 3.05 15.33 5.96L15.33 5.96Q15.33 8.92 16.17 11.09Q17.00 13.25 18.63 14.43Q20.27 15.61 22.63 15.61Z")

    private fun parse(d: String) = PathParser().parsePathString(d).toPath()
}
