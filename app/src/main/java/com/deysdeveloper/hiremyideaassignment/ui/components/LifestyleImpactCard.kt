package com.deysdeveloper.hiremyideaassignment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.theme.*

data class LifestyleRow(
    val label: String,
    val filledSquares: Int,
    val totalSquares: Int,
    val color: Color
)

private val lifestyleRows = listOf(
    LifestyleRow("Sleep", 5, 9, InsightsLavender),
    LifestyleRow("Hydrate", 4, 9, InsightsSalmon),
    LifestyleRow("Caffeine", 3, 9, InsightsSageGreen),
    LifestyleRow("Exercise", 4, 9, InsightsSalmon.copy(red = 0.85f))
)

@Composable
fun LifestyleImpactCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InsightsCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Lifestyle Impact",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InsightsDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8F7FB),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Correlation Strength",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = InsightsDark
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.height(28.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "4 months",
                                    fontSize = 11.sp,
                                    color = InsightsDark,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "⌄",
                                    fontSize = 11.sp,
                                    color = InsightsMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    lifestyleRows.forEach { row ->
                        CorrelationRow(row = row)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CorrelationRow(row: LifestyleRow) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = row.label,
            fontSize = 12.sp,
            color = InsightsMedium,
            modifier = Modifier.width(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0 until row.totalSquares) {
                val isFilled = i < row.filledSquares
                val alpha = if (isFilled) {
                    // Gradual fade from full to lighter
                    1f - (i.toFloat() / row.filledSquares) * 0.4f
                } else {
                    0f
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isFilled) row.color.copy(alpha = alpha)
                            else Color(0xFFE8E6EE)
                        )
                )
            }
        }
    }
}
