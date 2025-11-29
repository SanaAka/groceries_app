package com.example.groceries_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.groceries_app.navigation.AppScaffold
import com.example.groceries_app.ui.theme.GSshopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GSshopTheme {
                val navController = rememberNavController()
                AppScaffold(navController = navController)
            }
        }
    }
}
