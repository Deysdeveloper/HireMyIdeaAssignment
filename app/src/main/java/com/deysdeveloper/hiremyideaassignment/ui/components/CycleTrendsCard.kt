package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.*
import kotlin.math.*

// ─── Data ───────────────────────────────────────────────────────────────────

data class CycleMonth(
    val month: String,
    val totalDays: Int,
    val periodDays: Int,      // pink bottom oval
    val follicularDays: Int,  // dark-green middle oval (ovulation zone)
    val lutealDays: Int       // lavender top section
)

private val cycleData = listOf(
    CycleMonth("Jan", 28, 5, 7, 16),
    CycleMonth("Feb", 30, 5, 8, 17),
    CycleMonth("Mar", 28, 5, 7, 16),
    CycleMonth("Apr", 32, 6, 8, 18),
    CycleMonth("May", 28, 5, 7, 16),
    CycleMonth("Jun", 28, 5, 7, 16)
)

// Colors specific to this chart (not in global theme)
private val CycleGreen  = Color(0xFF3D6B4A)   // dark forest green for ovulation oval
private val CyclePink   = Color(0xFFCF7070)   // muted dusty rose for period oval
private val CycleDash   = Color(0xFFCDD0E0)   // dashed guide line color

// ─── Card ───────────────────────────────────────────────────────────────────

@Composable
fun CycleTrendsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InsightsCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {

                // ── Left arrow (outline circle) ─────────────────────────────
                NavArrow(
                    label = "‹",
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                // ── Chart ───────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                ) {
                    // Dashed guide lines sit behind the bars
                    CycleDashedLines(
                        modifier = Modifier.matchParentSize()
                    )

                    // Bar columns, bottom-aligned
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        cycleData.forEach { data ->
                            CycleBarColumn(data = data)
                        }
                    }
                }

                // ── Right arrow (outline circle) ────────────────────────────
                NavArrow(
                    label = "›",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

// ─── Navigation arrow ───────────────────────────────────────────────────────

@Composable
private fun NavArrow(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(30.dp)
            .border(1.5.dp, InsightsLavender, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            color = InsightsLavender,
            fontWeight = FontWeight.Normal
        )
    }
}

// ─── Dashed lines ────────────────────────────────────────────────────────────

/**
 * Three horizontal dashed guide lines drawn at 15 %, 50 %, and 82 % of
 * the chart height. They are drawn on a transparent Canvas that sizes itself
 * to whatever the parent Box gives it (i.e. the bar-column area).
 */
@Composable
private fun CycleDashedLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        listOf(0.15f, 0.50f, 0.82f).forEach { fraction ->
            val y = size.height * fraction
            drawLine(
                color       = CycleDash,
                start       = Offset(0f, y),
                end         = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect  = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
            )
        }
    }
}

// ─── Bar column ──────────────────────────────────────────────────────────────

private val MaxDays     = 34f
private val BarMaxH     = 220.dp
private val BarWidth    = 20.dp

@Composable
private fun CycleBarColumn(data: CycleMonth) {
    val barHeight = BarMaxH * (data.totalDays / MaxDays)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Day count label
        Text(
            text       = data.totalDays.toString(),
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = InsightsDark
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Pill bar drawn entirely on Canvas
        Canvas(
            modifier = Modifier
                .width(BarWidth)
                .height(barHeight)
        ) {
            val w     = size.width
            val h     = size.height
            val pill  = CornerRadius(w / 2f)

            // Heights of each zone, proportional to cycle-phase days
            val lutealH = h * (data.lutealDays.toFloat()    / data.totalDays)
            val greenH  = h * (data.follicularDays.toFloat()/ data.totalDays)
            val pinkH   = h * (data.periodDays.toFloat()    / data.totalDays)

            // ── Full lavender pill (background) ─────────────────────────────
            drawRoundRect(
                color        = InsightsLavender,
                cornerRadius = pill,
                size         = Size(w, h)
            )

            // ── Dark-green oval (ovulation / follicular) ────────────────────
            val greenTop = lutealH
            drawRoundRect(
                color        = CycleGreen,
                topLeft      = Offset(0f, greenTop),
                size         = Size(w, greenH),
                cornerRadius = pill
            )
            // Gear icon centred in the green oval
            drawGearIcon(
                center      = Offset(w / 2f, greenTop + greenH / 2f),
                outerRadius = 5.5.dp.toPx()
            )

            // ── Pink oval (menstrual / period) ──────────────────────────────
            val pinkTop = lutealH + greenH
            drawRoundRect(
                color        = CyclePink,
                topLeft      = Offset(0f, pinkTop),
                size         = Size(w, pinkH),
                cornerRadius = pill
            )
            // Water-drop icon centred in the pink oval
            drawDropIcon(
                center    = Offset(w / 2f, pinkTop + pinkH / 2f),
                dropSize  = 5.dp.toPx()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Month label
        Text(
            text     = data.month,
            fontSize = 10.sp,
            color    = InsightsMedium
        )
    }
}

// ─── Canvas icon helpers ──────────────────────────────────────────────────────

/**
 * Draws a stylised gear / sun icon in white:
 * a small inner circle outline + 8 evenly-spaced dots around the perimeter.
 */
private fun DrawScope.drawGearIcon(center: Offset, outerRadius: Float) {
    val innerR  = outerRadius * 0.50f
    val dotR    = outerRadius * 0.20f
    val stroke  = outerRadius * 0.18f

    // Inner ring
    drawCircle(
        color  = Color.White,
        radius = innerR,
        center = center,
        style  = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
    )

    // Radial teeth / dots
    repeat(8) { i ->
        val angleRad = Math.toRadians(i * 45.0)
        val dotCenter = Offset(
            x = center.x + cos(angleRad).toFloat() * outerRadius,
            y = center.y + sin(angleRad).toFloat() * outerRadius
        )
        drawCircle(color = Color.White, radius = dotR, center = dotCenter)
    }
}

/**
 * Draws a teardrop / water-drop icon in white.
 * [dropSize] is roughly the "radius" of the fat bottom of the drop.
 */
private fun DrawScope.drawDropIcon(center: Offset, dropSize: Float) {
    val path = Path().apply {
        // Start at the sharp top point
        moveTo(center.x, center.y - dropSize * 1.2f)
        // Right curve down to the round bottom
        cubicTo(
            center.x + dropSize * 0.9f, center.y - dropSize * 0.3f,
            center.x + dropSize * 0.9f, center.y + dropSize * 0.8f,
            center.x, center.y + dropSize * 1.1f
        )
        // Left curve back up to the tip
        cubicTo(
            center.x - dropSize * 0.9f, center.y + dropSize * 0.8f,
            center.x - dropSize * 0.9f, center.y - dropSize * 0.3f,
            center.x, center.y - dropSize * 1.2f
        )
        close()
    }
    drawPath(path = path, color = Color.White)
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFECEBF8)
@Composable
fun CycleTrendsCardPreview() {
    CycleTrendsCard(modifier = Modifier.padding(16.dp))
}
