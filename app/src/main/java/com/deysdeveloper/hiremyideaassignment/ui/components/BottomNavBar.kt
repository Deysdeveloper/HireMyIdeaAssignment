package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.InsightsDark
import com.deysdeveloper.hiremyideaassignment.ui.theme.InsightsMedium
import kotlin.math.cos
import kotlin.math.sin

enum class NavItem { HOME, TRACK, INSIGHTS }

// ─── Public composable ───────────────────────────────────────────────────────

@Composable
fun BottomNavBar(
    selectedItem: NavItem = NavItem.INSIGHTS,
    onItemSelected: (NavItem) -> Unit = {}
) {
    // Transparent outer container — screen background shows through
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ── Pill: Home | Track | Insights ────────────────────────────────
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(50.dp),   // full pill
            color = Color.White,
            shadowElevation = 8.dp,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    label    = "Home",
                    selected = selectedItem == NavItem.HOME,
                    onClick  = { onItemSelected(NavItem.HOME) }
                ) {
                    HomeIcon(
                        color = if (selectedItem == NavItem.HOME) InsightsDark else InsightsMedium
                    )
                }

                NavTabItem(
                    label    = "Track",
                    selected = selectedItem == NavItem.TRACK,
                    onClick  = { onItemSelected(NavItem.TRACK) }
                ) {
                    ClockIcon(
                        color = if (selectedItem == NavItem.TRACK) InsightsDark else InsightsMedium
                    )
                }

                NavTabItem(
                    label    = "Insights",
                    selected = selectedItem == NavItem.INSIGHTS,
                    onClick  = { onItemSelected(NavItem.INSIGHTS) }
                ) {
                    BarChartIcon(
                        color = if (selectedItem == NavItem.INSIGHTS) InsightsDark else InsightsMedium
                    )
                }
            }
        }

        // ── "+" circle button ─────────────────────────────────────────────
        Surface(
            modifier      = Modifier.size(54.dp),
            shape         = CircleShape,
            color         = Color(0xFFF0EFF6),
            shadowElevation = 8.dp,
            tonalElevation  = 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(18.dp)) {
                    val cx  = size.width / 2f
                    val cy  = size.height / 2f
                    val len = size.width * 0.44f
                    val sw  = 2.dp.toPx()
                    drawLine(InsightsMedium, Offset(cx - len, cy), Offset(cx + len, cy), sw, StrokeCap.Round)
                    drawLine(InsightsMedium, Offset(cx, cy - len), Offset(cx, cy + len), sw, StrokeCap.Round)
                }
            }
        }
    }
}

// ─── Single tab item ──────────────────────────────────────────────────────────

@Composable
private fun NavTabItem(
    label    : String,
    selected : Boolean,
    onClick  : () -> Unit,
    iconContent: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        iconContent()
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text       = label,
            fontSize   = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color      = if (selected) InsightsDark else InsightsMedium
        )
    }
}

// ─── Icons ────────────────────────────────────────────────────────────────────

@Composable
private fun HomeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val cx     = size.width / 2f
        val roof   = size.height * 0.12f
        val mid    = size.height * 0.46f
        val bottom = size.height * 0.88f
        val left   = size.width  * 0.10f
        val right  = size.width  * 0.90f
        val dLeft  = cx - size.width * 0.13f
        val dRight = cx + size.width * 0.13f

        // Filled roof triangle
        val roofPath = Path().apply {
            moveTo(cx, roof); lineTo(right, mid); lineTo(left, mid); close()
        }
        drawPath(path = roofPath, color = color)

        // Filled walls
        val bodyPath = Path().apply {
            moveTo(left, mid); lineTo(left, bottom)
            lineTo(right, bottom); lineTo(right, mid); close()
        }
        drawPath(path = bodyPath, color = color)

        // Door cut-out in white
        val doorTop = bottom - size.height * 0.28f
        val doorPath = Path().apply {
            moveTo(dLeft, bottom); lineTo(dLeft, doorTop)
            lineTo(dRight, doorTop); lineTo(dRight, bottom); close()
        }
        drawPath(path = doorPath, color = Color.White)
    }
}

@Composable
private fun ClockIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.width / 2f - 1.5.dp.toPx()
        val sw     = 1.8.dp.toPx()

        drawCircle(color = color, radius = radius, center = center, style = Stroke(width = sw))

        val hourAngle = Math.toRadians(-60.0)
        drawLine(
            color = color,
            start = center,
            end   = Offset(
                (center.x + radius * 0.48f * cos(hourAngle)).toFloat(),
                (center.y + radius * 0.48f * sin(hourAngle)).toFloat()
            ),
            strokeWidth = sw, cap = StrokeCap.Round
        )
        val minAngle = Math.toRadians(-90.0)
        drawLine(
            color = color,
            start = center,
            end   = Offset(
                (center.x + radius * 0.68f * cos(minAngle)).toFloat(),
                (center.y + radius * 0.68f * sin(minAngle)).toFloat()
            ),
            strokeWidth = sw, cap = StrokeCap.Round
        )
    }
}

@Composable
private fun BarChartIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val barW    = 3.5.dp.toPx()
        val gap     = 3.dp.toPx()
        val totalW  = barW * 3 + gap * 2
        val startX  = (size.width - totalW) / 2f
        val bottom  = size.height - 2.dp.toPx()
        val heights = listOf(0.45f, 0.85f, 0.60f)
        val avH     = size.height - 4.dp.toPx()

        heights.forEachIndexed { i, frac ->
            val barH = avH * frac
            val left = startX + i * (barW + gap)
            drawRoundRect(
                color       = color,
                topLeft     = Offset(left, bottom - barH),
                size        = Size(barW, barH),
                cornerRadius = CornerRadius(1.5.dp.toPx())
            )
        }

        // Trending-up line connecting bar tops
        val pts = heights.mapIndexed { i, frac ->
            val left = startX + i * (barW + gap) + barW / 2f
            Offset(left, bottom - avH * frac)
        }
        drawLine(color = color, start = pts[0], end = pts[1], strokeWidth = 1.2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color = color, start = pts[1], end = pts[2], strokeWidth = 1.2.dp.toPx(), cap = StrokeCap.Round)
        // Small dot at the peak
        drawCircle(color = color, radius = 2.dp.toPx(), center = pts[1])
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF8F7F5)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(selectedItem = NavItem.INSIGHTS, onItemSelected = {})
}
