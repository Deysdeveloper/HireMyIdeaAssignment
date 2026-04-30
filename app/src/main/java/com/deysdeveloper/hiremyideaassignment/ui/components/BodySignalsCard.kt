package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true, backgroundColor = 0xFFF8F7F5)
@Composable
fun BodySignalsCardPreview() {
    BodySignalsCard(modifier = Modifier.padding(16.dp))
}

data class SymptomSegment(
    val label: String,
    val percentage: Int,
    val color: Color
)

// Clockwise order starting from ~164° (lower-left) to match Figma label positions:
// Mood wraps over the top (upper-left label), Bloating on right (upper-right),
// Fatigue at lower-right, Acne at lower-left
private val segments = listOf(
    SymptomSegment("Mood", 30, InsightsSalmon),
    SymptomSegment("Bloating", 31, InsightsLavender),
    SymptomSegment("Fatigue", 21, InsightsCoralRed),
    SymptomSegment("Acne", 17, InsightsSageGreen)
)

@Composable
fun BodySignalsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InsightsCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Body Signals",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InsightsDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Inner card matching Figma's white card with subtle fill
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F7FB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Symptom Trends",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InsightsDark
                    )
                    Text(
                        text = "Compared to last cycle",
                        fontSize = 12.sp,
                        color = InsightsMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart(
                            segments = segments,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    segments: List<SymptomSegment>,
    modifier: Modifier = Modifier
) {
    // Bubble size and offset constants matching Figma proportions
    val bubbleSizeDp = 58.dp
    // Offset from center to bubble center (accounts for donut radius + half stroke)
    val labelOffsetDp = 95.dp

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        // Donut arcs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 46.dp.toPx()
            val margin = 54.dp.toPx()
            val radius = (size.minDimension / 2f) - margin
            val center = Offset(size.width / 2f, size.height / 2f)
            val total = segments.sumOf { it.percentage }.toFloat()
            var startAngle = 164f
            segments.forEach { segment ->
                val sweepAngle = (segment.percentage / total) * 360f
                drawArc(
                    color = segment.color,
                    startAngle = startAngle + 2f,
                    sweepAngle = sweepAngle - 4f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f)
                )
                startAngle += sweepAngle
            }
        }

        // White circular bubble labels — one per segment, positioned at midpoint angle
        segments.forEachIndexed { index, segment ->
            val total = segments.sumOf { it.percentage }.toFloat()

            var startAngle = 164.0
            for (i in 0 until index) {
                startAngle += (segments[i].percentage / total) * 360.0
            }
            val midAngle = startAngle + (segment.percentage / total) * 180.0
            val angleRad = Math.toRadians(midAngle)

            val xOffset = (cos(angleRad) * labelOffsetDp.value).toFloat()
            val yOffset = (sin(angleRad) * labelOffsetDp.value).toFloat()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center, unbounded = true)
            ) {
                // White circular bubble — mirrors Figma's Ellipse 506
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(bubbleSizeDp)
                        .offset(x = xOffset.dp, y = yOffset.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "${segment.percentage}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = InsightsDark
                        )
                        Text(
                            text = segment.label,
                            fontSize = 10.sp,
                            color = InsightsMedium,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}
