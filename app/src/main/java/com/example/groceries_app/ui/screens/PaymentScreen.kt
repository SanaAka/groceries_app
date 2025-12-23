package com.example.groceries_app.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.viewmodel.PaymentState
import com.example.groceries_app.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    amount: Double,
    onPaymentSuccess: () -> Unit = {},
    onBack: () -> Unit = {},
    paymentViewModel: PaymentViewModel = viewModel()
) {
    val paymentState by paymentViewModel.paymentState.collectAsState()
    val timeRemaining by paymentViewModel.timeRemaining.collectAsState()

    // Generate QR code when screen loads
    LaunchedEffect(amount) {
        paymentViewModel.generateQrCode(amount)
    }

    // Handle payment success
    LaunchedEffect(paymentState) {
        if (paymentState is PaymentState.PaymentSuccess) {
            // Wait a bit to show success message, then navigate
            kotlinx.coroutines.delay(2000)
            onPaymentSuccess()
        }
    }

    // Clean up when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            paymentViewModel.stopPolling()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Payment",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        paymentViewModel.resetPayment()
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = paymentState) {
                is PaymentState.Loading -> {
                    LoadingView()
                }
                is PaymentState.QrGenerated -> {
                    QrCodeView(
                        qrBitmap = state.qrImage,
                        amount = amount,
                        timeRemaining = timeRemaining
                    )
                }
                is PaymentState.PaymentSuccess -> {
                    PaymentSuccessView(transactionData = state.transactionData)
                }
                is PaymentState.PaymentFailed -> {
                    PaymentFailedView(
                        message = state.message,
                        onRetry = { paymentViewModel.generateQrCode(amount) }
                    )
                }
                is PaymentState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { paymentViewModel.generateQrCode(amount) }
                    )
                }
                else -> {
                    // Idle state - should not happen as we generate QR on load
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = NectarGreen,
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "Generating QR Code...",
                fontSize = 16.sp,
                color = Color(0xFF7C7C7C)
            )
        }
    }
}

@Composable
fun QrCodeView(
    qrBitmap: Bitmap?,
    amount: Double,
    timeRemaining: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Amount display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F3F2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Amount to Pay",
                    fontSize = 14.sp,
                    color = Color(0xFF7C7C7C)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$%.2f".format(amount),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = NectarGreen
                )
            }
        }

        // Timer
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        Text(
            text = "Time remaining: %d:%02d".format(minutes, seconds),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (timeRemaining < 60) Color.Red else Color(0xFF181725)
        )

        // QR Code
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "QR Code could not be loaded",
                        fontSize = 14.sp,
                        color = Color(0xFF7C7C7C),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Instructions
        Text(
            text = "Scan this QR code with your banking app to complete the payment",
            fontSize = 14.sp,
            color = Color(0xFF7C7C7C),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )

        Text(
            text = "Waiting for payment confirmation...",
            fontSize = 14.sp,
            color = Color(0xFF7C7C7C),
            textAlign = TextAlign.Center
        )

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = NectarGreen,
            trackColor = Color(0xFFE2E2E2)
        )
    }
}

@Composable
fun PaymentSuccessView(transactionData: com.example.groceries_app.data.model.BakongTransactionData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = NectarGreen,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Payment Successful!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181725)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Thank you for your payment",
            fontSize = 16.sp,
            color = Color(0xFF7C7C7C),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Transaction details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F3F2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TransactionDetail("Amount", "$%.2f".format(transactionData.amount))
                TransactionDetail("Currency", transactionData.currency)
                if (transactionData.externalRef != null) {
                    TransactionDetail("Reference", transactionData.externalRef)
                }
            }
        }
    }
}

@Composable
fun TransactionDetail(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF7C7C7C)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF181725)
        )
    }
}

@Composable
fun PaymentFailedView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Payment Failed",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181725)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            color = Color(0xFF7C7C7C),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NectarGreen
            ),
            shape = RoundedCornerShape(19.dp)
        ) {
            Text(
                text = "Try Again",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "❌",
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Error",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181725)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            color = Color(0xFF7C7C7C),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NectarGreen
            ),
            shape = RoundedCornerShape(19.dp)
        ) {
            Text(
                text = "Retry",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

