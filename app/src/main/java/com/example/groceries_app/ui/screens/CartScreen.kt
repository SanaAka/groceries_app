package com.example.groceries_app.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.utils.SessionManager
import com.example.groceries_app.viewmodel.OrderViewModel
import com.example.groceries_app.data.model.OrderItemRequest
import com.example.groceries_app.data.model.OrderRequest
import java.math.BigDecimal
import java.util.UUID

data class CartItem(
    val id: String,  // Changed from Int to String for UUID support
    val name: String,
    val size: String = "",
    val weight: String = "",
    val price: Double,
    val imageRes: Int,
    var quantity: Int = 1,
    val imageUrl: String? = null,
    val productUuid: String? = null  // Added to link to actual product
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onOrderPlaced: () -> Unit = {},
    onOrderFailed: () -> Unit = {},
    cartItems: List<CartItem> = emptyList() // Cart items passed from parent/ViewModel
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val orderViewModel: OrderViewModel = viewModel()
    val isCreatingOrder by orderViewModel.isCreatingOrder.collectAsState()
    val orderError by orderViewModel.orderError.collectAsState()
    val createdOrder by orderViewModel.createdOrder.collectAsState()
    
    // Cart items managed externally
    var items by remember { mutableStateOf(cartItems) }
    
    // Update when cartItems prop changes
    LaunchedEffect(cartItems) {
        items = cartItems
    }
    
    // Handle successful order creation
    LaunchedEffect(createdOrder) {
        if (createdOrder != null) {
            onOrderPlaced()
        }
    }
    
    // Handle order creation error
    LaunchedEffect(orderError) {
        if (orderError != null) {
            onOrderFailed()
        }
    }

    val totalAmount = items.sumOf { it.price * it.quantity }
    var showCheckoutSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Cart",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { showCheckoutSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Go to Checkout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Surface(
                        color = Color(0xFF489E67),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "$%.2f".format(totalAmount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items) { item ->
                CartItemCard(
                    cartItem = item,
                    onQuantityChange = { newQuantity ->
                        items = items.map {
                            if (it.id == item.id) it.copy(quantity = newQuantity)
                            else it
                        }
                    },
                    onRemove = {
                        items = items.filter { it.id != item.id }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFE2E2E2),
                    thickness = 1.dp
                )
            }
        }
    }

    // Show Checkout Bottom Sheet
    if (showCheckoutSheet) {
        CheckoutBottomSheet(
            totalCost = totalAmount,
            onDismiss = { showCheckoutSheet = false },
            onSelectDeliveryMethod = {
                // TODO: Navigate to delivery method selection
            },
            onSelectPaymentMethod = {
                // TODO: Navigate to payment method selection
            },
            onApplyPromoCode = {
                // TODO: Navigate to promo code screen
            },
            onPlaceOrder = {
                showCheckoutSheet = false
                
                // Create order via API
                val orderItems = items.map { cartItem ->
                    OrderItemRequest(
                        productUuid = cartItem.productUuid ?: UUID.randomUUID().toString(),
                        quantity = cartItem.quantity,
                        price = BigDecimal.valueOf(cartItem.price)
                    )
                }
                
                val orderRequest = OrderRequest(
                    orderNumber = "ORD-${System.currentTimeMillis()}",
                    totalPrice = BigDecimal.valueOf(totalAmount),
                    status = com.example.groceries_app.data.model.Status.PENDING,
                    items = orderItems
                )
                
                // Get token from SessionManager
                val token = sessionManager.getAccessToken() ?: ""
                orderViewModel.createOrder(orderRequest, token)
            }
        )
    }
    
    // Show loading indicator while creating order
    if (isCreatingOrder) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = NectarGreen)
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        if (cartItem.imageUrl != null) {
            AsyncImage(
                model = cartItem.imageUrl,
                contentDescription = cartItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = cartItem.imageRes),
                error = painterResource(id = cartItem.imageRes)
            )
        } else {
            Image(
                painter = painterResource(id = cartItem.imageRes),
                contentDescription = cartItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Product Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cartItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181725)
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color(0xFFB3B3B3),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = cartItem.size.ifEmpty { cartItem.weight },
                fontSize = 14.sp,
                color = Color(0xFF7C7C7C),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity Controls and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    IconButton(
                        onClick = {
                            if (cartItem.quantity > 1) {
                                onQuantityChange(cartItem.quantity - 1)
                            }
                        },
                        modifier = Modifier.size(45.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(17.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E2E2)),
                            color = Color.White
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Minus line
                                Box(
                                    modifier = Modifier
                                        .width(15.dp)
                                        .height(2.dp)
                                        .background(Color(0xFFB3B3B3))
                                )
                            }
                        }
                    }

                    // Quantity Text
                    Text(
                        text = cartItem.quantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF181725),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // Plus Button
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) },
                        modifier = Modifier.size(45.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(17.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E2E2)),
                            color = Color.White
                        ) {
                            Box(
                                modifier = Modifier.size(45.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = NectarGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Price
                Text(
                    text = "$%.2f".format(cartItem.price),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181725)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    GSshopTheme {
        CartScreen()
    }
}

