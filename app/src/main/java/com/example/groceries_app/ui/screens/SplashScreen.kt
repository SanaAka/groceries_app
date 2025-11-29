package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.R
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.ui.theme.NectarGreenLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateToWelcome: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds delay
        onNavigateToWelcome()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NectarGreen,
                        NectarGreenLight
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Carrot Icon - Using a custom composable since we don't have the actual drawable
            CarrotIcon(modifier = Modifier.size(80.dp))

            Spacer(modifier = Modifier.height(4.dp))

            // Nectar Text
            Text(
                text = "nectar",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "online groceriet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun CarrotIcon(modifier: Modifier = Modifier) {
    // Simple carrot icon using Canvas
    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = "Nectar Logo",
        modifier = Modifier.height(50.dp)
    )
    }



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    GSshopTheme {
        SplashScreen()
    }
}

