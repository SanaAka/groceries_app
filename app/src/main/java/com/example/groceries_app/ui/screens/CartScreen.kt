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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

data class CartItem(
    val id: Int,
    val name: String,
    val weight: String,
    val price: Double,
    val imageRes: Int,
    var quantity: Int = 1
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onOrderPlaced: () -> Unit = {}
) {
    // Sample cart items
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(1, "Bell Pepper Red", "1kg, Price", 4.99, com.example.groceries_app.R.drawable.img_4, 1),
                CartItem(2, "Egg Chicken Red", "4pcs, Price", 1.99, com.example.groceries_app.R.drawable.img_1, 1),
                CartItem(3, "Organic Bananas", "12kg, Price", 3.00, com.example.groceries_app.R.drawable.img_2, 1),
                CartItem(4, "Ginger", "250gm, Price", 2.99, com.example.groceries_app.R.drawable.img_3, 1)
            )
        )
    }

    val totalAmount = cartItems.sumOf { it.price * it.quantity }
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
            items(cartItems) { item ->
                CartItemCard(
                    cartItem = item,
                    onQuantityChange = { newQuantity ->
                        cartItems = cartItems.map {
                            if (it.id == item.id) it.copy(quantity = newQuantity)
                            else it
                        }
                    },
                    onRemove = {
                        cartItems = cartItems.filter { it.id != item.id }
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
                onOrderPlaced()
            }
        )
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
        Image(
            painter = painterResource(id = cartItem.imageRes),
            contentDescription = cartItem.name,
            modifier = Modifier
                .size(70.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Fit
        )

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
                text = cartItem.weight,
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

