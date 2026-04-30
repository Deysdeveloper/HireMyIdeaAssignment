package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.*

private val weightDataMonthly = listOf(52f, 54f, 58f, 63f, 68f, 65f, 62f, 64f, 63f, 61f, 59f)
private val weightDataWeekly = listOf(61f, 62f, 60f, 63f, 61f, 62f, 60f)
private val xLabelsMonthly = listOf("Jan", "Feb", "Mar", "Apr", "May")

@Composable
fun BodyMetabolicCard(modifier: Modifier = Modifier) {
    var isMonthly by remember { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InsightsCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Body & Metabolic Trends",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InsightsDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Your weight",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = InsightsDark
                    )
                    Text(
                        text = "in kg",
                        fontSize = 11.sp,
                        color = InsightsMedium
                    )
                }

                // Monthly / Weekly toggle
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF0EFF6),
                    modifier = Modifier.height(32.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ToggleChip(label = "Monthly", selected = isMonthly) { isMonthly = true }
                        ToggleChip(label = "Weekly", selected = !isMonthly) { isMonthly = false }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val data = if (isMonthly) weightDataMonthly else weightDataWeekly

            WeightChart(
                data = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // X-axis labels
            val xLabels = if (isMonthly) xLabelsMonthly else listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                xLabels.forEach { label ->
                    Text(text = label, fontSize = 10.sp, color = InsightsMedium)
                }
            }
        }
    }
}

@Composable
private fun ToggleChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (selected) InsightsDark else Color.Transparent,
        modifier = Modifier
            .height(28.dp)
            .padding(horizontal = 2.dp),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) Color.White else InsightsMedium
            )
        }
    }
}

@Composable
private fun WeightChart(data: List<Float>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val leftPadding = 28.dp.toPx()
            val rightPadding = 8.dp.toPx()
            val topPadding = 8.dp.toPx()
            val bottomPadding = 4.dp.toPx()
            val chartWidth = size.width - leftPadding - rightPadding
            val chartHeight = size.height - topPadding - bottomPadding

            // Grid lines for 25, 50, 75
            listOf(25f, 50f, 75f).forEach { v ->
                val y = topPadding + (1f - (v - 0f) / 80f) * chartHeight
                drawLine(
                    color = InsightsBorder,
                    start = Offset(leftPadding, y),
                    end = Offset(leftPadding + chartWidth, y),
                    strokeWidth = 0.5.dp.toPx()
                )
            }

            fun xForIndex(i: Int) = leftPadding + (i.toFloat() / (data.size - 1)) * chartWidth
            fun yForValue(v: Float) = topPadding + (1f - (v - 0f) / 80f) * chartHeight

            val points = data.mapIndexed { i, v -> Offset(xForIndex(i), yForValue(v)) }

            // Fill path
            val fillPath = Path().apply {
                moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size) {
                    val cp1 = Offset((points[i - 1].x + points[i].x) / 2, points[i - 1].y)
                    val cp2 = Offset((points[i - 1].x + points[i].x) / 2, points[i].y)
                    cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, points[i].x, points[i].y)
                }
                lineTo(points.last().x, topPadding + chartHeight)
                lineTo(points.first().x, topPadding + chartHeight)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(InsightsSalmon.copy(alpha = 0.35f), InsightsSalmonFill.copy(alpha = 0.05f)),
                    startY = topPadding,
                    endY = topPadding + chartHeight
                )
            )

            // Line
            val linePath = Path().apply {
                moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size) {
                    val cp1 = Offset((points[i - 1].x + points[i].x) / 2, points[i - 1].y)
                    val cp2 = Offset((points[i - 1].x + points[i].x) / 2, points[i].y)
                    cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, points[i].x, points[i].y)
                }
            }
            drawPath(
                path = linePath,
                color = InsightsSalmon,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Peak dot
            val maxIdx = data.indexOf(data.max())
            val peakPoint = points[maxIdx]
            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = peakPoint)
            drawCircle(
                color = InsightsSalmon, radius = 5.dp.toPx(), center = peakPoint,
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(color = InsightsSalmon, radius = 2.5.dp.toPx(), center = peakPoint)
        }

        // Y-axis labels
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(top = 8.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("75", "50", "25").forEach { label ->
                Text(text = label, fontSize = 9.sp, color = InsightsMedium)
            }
        }
    }
}
