package com.example.groceries_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.viewmodel.OrderViewModel

@Composable
fun OrderFailedDialog(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onDismiss: () -> Unit = {},
    onTryAgain: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF181725),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Illustration with circular background
                    Box(
                        modifier = Modifier.size(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Light green circle background
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9).copy(alpha = 0.5f))
                        )

                        // Grocery bag illustration (using a placeholder)
                        // You can replace this with your actual grocery bag drawable
                        Box(
                            modifier = Modifier
                                .size(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Placeholder for grocery bag illustration
                            // In production, use: Image(painter = painterResource(id = R.drawable.ic_grocery_bag), ...)
                            Text(
                                text = "🛒\n🥕🥖\n🍎🥤",
                                fontSize = 60.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 50.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Title
                    Text(
                        text = "Oops! Order Failed",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181725),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Subtitle with error message
                    Text(
                        text = errorMessage ?: "Something went terribly wrong.",
                        fontSize = 16.sp,
                        color = Color(0xFF7C7C7C),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Please Try Again Button
                    Button(
                        onClick = onTryAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(67.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NectarGreen
                        ),
                        shape = RoundedCornerShape(19.dp)
                    ) {
                        Text(
                            text = "Please Try Again",
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
    }
}

@Composable
fun OrderFailedScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onTryAgain: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    orderViewModel: OrderViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(true) }
    val orderError by orderViewModel.orderError.collectAsState()
    
    // Clear error state when leaving this screen
    DisposableEffect(Unit) {
        onDispose {
            orderViewModel.clearOrderState()
        }
    }

    if (showDialog) {
        OrderFailedDialog(
            errorMessage = orderError,
            onDismiss = {
                showDialog = false
                onDismiss()
            },
            onTryAgain = {
                showDialog = false
                onTryAgain()
            },
            onBackToHome = {
                showDialog = false
                onBackToHome()
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderFailedDialogPreview() {
    GSshopTheme {
        OrderFailedDialog()
    }
}

