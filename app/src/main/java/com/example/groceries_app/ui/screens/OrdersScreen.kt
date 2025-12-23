package com.example.groceries_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.data.model.OrderResponse
import com.example.groceries_app.data.model.Status
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.utils.SessionManager
import com.example.groceries_app.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBack: () -> Unit = {},
    orderViewModel: OrderViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val orders by orderViewModel.orders.collectAsState()
    val orderError by orderViewModel.orderError.collectAsState()
    val currentUserName = remember { sessionManager.getUsername() ?: "" }

    // Load orders when screen opens
    LaunchedEffect(Unit) {
        val token = sessionManager.getAccessToken()
        if (token != null) {
            orderViewModel.loadOrders(token)
        }
    }

    // Filter orders by current user (match createdBy with username)
    val userOrders = remember(orders, currentUserName) {
        orders.filter { order ->
            order.audit?.createdBy == currentUserName
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        containerColor = Color(0xFFF2F3F2)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                orderError != null -> {
                    // Error state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to load orders",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181725)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = orderError ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF7C7C7C)
                        )
                    }
                }
                userOrders.isEmpty() -> {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🛒",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Orders Yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181725)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your order history will appear here",
                            fontSize = 14.sp,
                            color = Color(0xFF7C7C7C)
                        )
                    }
                }
                else -> {
                    // Orders list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(userOrders) { order ->
                            OrderCard(order = order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181725)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(order.audit?.createdAt),
                        fontSize = 12.sp,
                        color = Color(0xFF7C7C7C)
                    )
                }

                // Status badge
                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Order items
            if (order.items.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    order.items.take(3).forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.productName}",
                                fontSize = 14.sp,
                                color = Color(0xFF181725),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$%.2f".format(item.price.toDouble()),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF181725)
                            )
                        }
                    }

                    if (order.items.size > 3) {
                        Text(
                            text = "+ ${order.items.size - 3} more items",
                            fontSize = 12.sp,
                            color = Color(0xFF7C7C7C)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    color = Color(0xFFE2E2E2),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    color = Color(0xFF7C7C7C)
                )
                Text(
                    text = "$%.2f".format(order.totalPrice.toDouble()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NectarGreen
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: Status) {
    val (backgroundColor, textColor, statusText) = when (status) {
        Status.PENDING -> Triple(Color(0xFFFFF3E0), Color(0xFFFF8F00), "Pending")
        Status.PAID -> Triple(Color(0xFFE8F5E9), NectarGreen, "Paid")
        Status.SHIPPED -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "Shipped")
        Status.COMPLETED -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Completed")
        Status.CANCELLED -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Cancelled")
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = statusText,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

fun formatDate(dateString: String?): String {
    if (dateString == null) return "N/A"

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString.substringBefore("."))

        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString.substringBefore("T")
    }
}

