package com.example.groceries_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    modifier: Modifier = Modifier,
    totalCost: Double = 13.97,
    onDismiss: () -> Unit = {},
    onSelectDeliveryMethod: () -> Unit = {},
    onSelectPaymentMethod: () -> Unit = {},
    onApplyPromoCode: () -> Unit = {},
    onPlaceOrder: () -> Unit = {},
    onNavigateToPayment: ((Double) -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(top = 25.dp, bottom = 30.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Checkout",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181725)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF181725)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            // Delivery Option
            CheckoutOption(
                label = "Delivery",
                value = "Select Method",
                onClick = onSelectDeliveryMethod
            )

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            // Payment Option
            CheckoutOption(
                label = "Pament",
                value = "💳",
                onClick = onSelectPaymentMethod
            )

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            // Promo Code Option
            CheckoutOption(
                label = "Promo Code",
                value = "Pick discount",
                onClick = onApplyPromoCode
            )

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            // Total Cost Option
            CheckoutOption(
                label = "Total Cost",
                value = "$%.2f".format(totalCost),
                onClick = {},
                showArrow = true
            )

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Terms and Conditions Text
            Text(
                text = buildAnnotatedString {
                    append("By placing an order you agree to our\n")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181725)
                        )
                    ) {
                        append("Terms")
                    }
                    append(" And ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181725)
                        )
                    ) {
                        append("Conditions")
                    }
                },
                fontSize = 14.sp,
                color = Color(0xFF7C7C7C),
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Place Order Button
            Button(
                onClick = {
                    onDismiss()
                    // Navigate to payment screen with total amount
                    onNavigateToPayment?.invoke(totalCost) ?: onPlaceOrder()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Place Order",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CheckoutOption(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF7C7C7C)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF181725)
                )
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color(0xFF181725),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutBottomSheetPreview() {
    GSshopTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
        ) {
            CheckoutBottomSheet(
                totalCost = 13.97,
                onDismiss = {},
                onSelectDeliveryMethod = {},
                onSelectPaymentMethod = {},
                onApplyPromoCode = {},
                onPlaceOrder = {}
            )
        }
    }
}

