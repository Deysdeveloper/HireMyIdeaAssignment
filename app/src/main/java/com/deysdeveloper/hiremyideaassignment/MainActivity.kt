package com.deysdeveloper.hiremyideaassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deysdeveloper.hiremyideaassignment.ui.screens.InsightsScreen
import com.deysdeveloper.hiremyideaassignment.ui.theme.HireMyIdeaAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HireMyIdeaAssignmentTheme {
                InsightsScreen()
            }
        }
    }
}
