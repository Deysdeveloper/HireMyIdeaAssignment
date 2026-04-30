package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.*

// ─── Public composable ──────────────────────────────────────────────────────

@Composable
fun StabilitySummaryCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Description text ────────────────────────────────────────────
            Text(
                text = "Based on your recent logs and symptom patterns.",
                fontSize = 14.sp,
                color = InsightsMedium,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Stability Score ──────────────────────────────────────────────
                Text(
                    text = "Stability Score",
                    fontSize = 16.sp,
                    color = InsightsDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "78%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InsightsDark
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Flow chart with tooltip and indicator ────────────────────────
                StabilityFlowChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(138.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── X-axis labels ────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr").forEach { label ->
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (label == "Mar") FontWeight.Medium else FontWeight.Normal,
                            color = if (label == "Mar") InsightsDark else InsightsMedium
                        )
                    }
                }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ─── Flow chart ─────────────────────────────────────────────────────────────

/**
 * Chart section containing:
 * - background cone area (light lavender)
 * - filled data area (darker lavender, Jan → Mar)
 * - dashed indicator line at Mar
 * - indicator dot at Mar ≈ 28d
 * - "Stability Improving" tooltip above the indicator
 * - Y-axis labels (32d, 28d, 24d)
 *
 * Total height = tooltipAreaH + gap + chartH
 */
@Composable
private fun StabilityFlowChart(modifier: Modifier = Modifier) {
    val tooltipAreaH = 52.dp   // space at top for tooltip + tail
    val chartH       = 82.dp   // actual chart area height
    val leftPadDp    = 28.dp   // Y-axis labels width
    val rightPadDp   = 4.dp

    BoxWithConstraints(modifier = modifier) {
        val totalWidthDp  = maxWidth
        val chartWidthDp  = totalWidthDp - leftPadDp - rightPadDp
        val marXFraction  = 2f / 3f                      // Jan/Feb/Mar/Apr ⟹ 0, ⅓, ⅔, 1
        val marXDp        = leftPadDp + chartWidthDp * marXFraction
        val tooltipWidthDp = 80.dp

        // Canvas draws the graph (background cone, filled area, lines, dot)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lp          = leftPadDp.toPx()
            val rp          = rightPadDp.toPx()
            val cw          = size.width - lp - rp
            val topOffset   = tooltipAreaH.toPx()          // chart starts below tooltip
            val ch          = chartH.toPx()
            val chartTop    = topOffset
            val chartBottom = topOffset + ch
            val marXPx      = lp + cw * marXFraction
            val janX        = lp
            val aprX        = lp + cw

            // Map cycle days (24–32d) to Y pixel
            fun yVal(d: Float) = chartBottom - (d - 24f) / 8f * ch

            // ── Horizontal grid lines ────────────────────────────────────────
            listOf(32f, 28f, 24f).forEach { d ->
                drawLine(
                    color = InsightsBorder,
                    start = Offset(lp, yVal(d)),
                    end   = Offset(aprX, yVal(d)),
                    strokeWidth = 0.5.dp.toPx()
                )
            }

            // ── Background cone (Empty Section): Jan-bottom → Apr-32d ────────
            val emptyPath = Path().apply {
                moveTo(janX, chartBottom)                    // Jan, 24d (base)
                cubicTo(
                    janX + cw * 0.22f, chartBottom,          // gentle start
                    aprX - cw * 0.38f, yVal(29.5f),
                    aprX, yVal(32f)                          // Apr, 32d
                )
                lineTo(aprX, chartBottom)                    // Apr, 24d (base)
                close()
            }
            drawPath(
                emptyPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        InsightsLavenderLight.copy(alpha = 0.80f),
                        InsightsLavenderFill.copy(alpha = 0.40f)
                    ),
                    startY = yVal(32f),
                    endY   = chartBottom
                )
            )

            // ── Filled area (actual data, Jan → Mar ≈ 28d) ──────────────────
            val filledPath = Path().apply {
                moveTo(janX, chartBottom)                    // Jan, 24d
                cubicTo(
                    janX + cw * 0.15f, chartBottom,
                    marXPx - cw * 0.22f, yVal(26.5f),
                    marXPx, yVal(28f)                        // Mar, 28d (top)
                )
                lineTo(marXPx, chartBottom)                  // Mar, 24d (base)
                close()
            }
            drawPath(
                filledPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        InsightsLavender,                    // full opacity at peak
                        InsightsLavender.copy(alpha = 0.45f)
                    ),
                    startY = yVal(28f),
                    endY   = chartBottom
                )
            )

            // ── Dashed indicator line at Mar ─────────────────────────────────
            drawLine(
                color       = InsightsMedium.copy(alpha = 0.45f),
                start       = Offset(marXPx, chartTop),
                end         = Offset(marXPx, chartBottom),
                strokeWidth = 1.dp.toPx(),
                pathEffect  = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
            )

            // ── Indicator dot at (Mar, 28d) ──────────────────────────────────
            val dotY = yVal(28f)
            drawCircle(Color.White, radius = 6.dp.toPx(), center = Offset(marXPx, dotY))
            drawCircle(
                color  = InsightsSageGreen,
                radius = 4.dp.toPx(),
                center = Offset(marXPx, dotY)
            )
        }

        // ── Y-axis text labels ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = tooltipAreaH)
                .height(chartH)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("32d", "28d", "24d").forEach { label ->
                    Text(text = label, fontSize = 10.sp, color = InsightsMedium)
                }
            }
        }

        // ── "Stability Improving" tooltip, centered on Mar ───────────────────
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = marXDp - tooltipWidthDp / 2)
        ) {
            StabilityTooltip(width = tooltipWidthDp)
        }
    }
}

// ─── Tooltip ────────────────────────────────────────────────────────────────

@Composable
private fun StabilityTooltip(width: androidx.compose.ui.unit.Dp = 80.dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(width)
    ) {
        // Rounded dark background pill
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = InsightsDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stability\nImproving",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        // Downward-pointing triangle tail
        Canvas(modifier = Modifier.size(12.dp, 7.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(path, color = InsightsDark)
        }
    }
}

// ─── Preview ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF8F7F5)
@Composable
fun StabilitySummaryCardPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Stability Summary",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = InsightsDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        StabilitySummaryCard()
    }
}
