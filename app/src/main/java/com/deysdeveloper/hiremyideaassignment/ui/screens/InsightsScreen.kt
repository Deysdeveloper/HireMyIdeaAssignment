package com.deysdeveloper.hiremyideaassignment.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deysdeveloper.hiremyideaassignment.ui.components.*
import com.deysdeveloper.hiremyideaassignment.ui.theme.InsightsDark
import com.deysdeveloper.hiremyideaassignment.ui.theme.InsightsBackground
import com.deysdeveloper.hiremyideaassignment.ui.theme.InsightsMedium

@Composable
fun InsightsScreen() {
    var selectedNav by remember { mutableStateOf(NavItem.INSIGHTS) }

    Scaffold(
        containerColor = InsightsBackground,
        topBar = { InsightsTopBar() },
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedNav,
                onItemSelected = { selectedNav = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // "Stability Summary" section header is outside the card in the Figma design
            Text(
                text = "Stability Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InsightsDark
            )
            StabilitySummaryCard()
            Text(
                text = "Cycle Trends",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InsightsDark
            )
            CycleTrendsCard()
            BodyMetabolicCard()
            BodySignalsCard()
            LifestyleImpactCard()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsightsTopBar() {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Insights",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InsightsDark
                )
            }
        },
        navigationIcon = {
            // Grid/apps icon drawn with Canvas
            AppGridIcon()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = InsightsBackground
        )
    )
}

@Composable
private fun AppGridIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(20.dp)) {
            val dotSize = 4.dp.toPx()
            val gap = 3.dp.toPx()
            val step = dotSize + gap

            for (row in 0..1) {
                for (col in 0..1) {
                    drawCircle(
                        color = InsightsMedium,
                        radius = dotSize / 2,
                        center = androidx.compose.ui.geometry.Offset(
                            col * step + dotSize / 2,
                            row * step + dotSize / 2
                        )
                    )
                }
            }
        }
    }
}
