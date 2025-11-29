package com.example.groceries_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@Composable
fun OrderAcceptedScreen(
    modifier: Modifier = Modifier,
    onTrackOrder: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFFFE8E8).copy(alpha = 0.3f),
                        Color(0xFFE8F5E9).copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon with decorative elements
            Box(
                modifier = Modifier.size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                // Decorative circles and shapes
                // Top left green circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = (-60).dp, y = (-80).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )

                // Top red dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .offset(x = 20.dp, y = (-70).dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                )

                // Top right orange curve
                Box(
                    modifier = Modifier
                        .size(60.dp, 8.dp)
                        .offset(x = 90.dp, y = (-40).dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFF9966))
                )

                // Left orange circle outline
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .offset(x = (-90).dp, y = (-10).dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .then(
                            Modifier.padding(3.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFFFB366).copy(alpha = 0.3f))
                    )
                }

                // Right purple circle outline
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .offset(x = 95.dp, y = 20.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .then(
                            Modifier.padding(3.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFBB86FC).copy(alpha = 0.3f))
                    )
                }

                // Left blue curve
                Box(
                    modifier = Modifier
                        .size(70.dp, 10.dp)
                        .offset(x = (-80).dp, y = 60.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF6B9EFF))
                )

                // Bottom left green circle outline
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = (-20).dp, y = 90.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .then(
                            Modifier.padding(3.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50).copy(alpha = 0.3f))
                    )
                }

                // Bottom green dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .offset(x = 25.dp, y = 100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )

                // Bottom right blue dot
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = 80.dp, y = 95.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6B9EFF))
                )

                // Bottom right orange curve
                Box(
                    modifier = Modifier
                        .size(60.dp, 8.dp)
                        .offset(x = 85.dp, y = 70.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFB366))
                )

                // Main success circle
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(NectarGreen),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer ring
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .padding(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .padding(1.dp)
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(3.dp, Color.White.copy(alpha = 0.5f)),
                                color = Color.Transparent
                            ) {}
                        }
                    }

                    // Check icon
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        modifier = Modifier.size(90.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Title
            Text(
                text = "Your Order has been\naccepted",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181725),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle
            Text(
                text = "Your items has been placcd and is on\nit's way to being processed",
                fontSize = 16.sp,
                color = Color(0xFF7C7C7C),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Track Order Button
            Button(
                onClick = onTrackOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Track Order",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Back to Home Button
            TextButton(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back to home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF181725)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderAcceptedScreenPreview() {
    GSshopTheme {
        OrderAcceptedScreen()
    }
}

