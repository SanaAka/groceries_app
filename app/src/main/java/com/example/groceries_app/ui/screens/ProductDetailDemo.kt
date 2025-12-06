package com.example.groceries_app.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.groceries_app.ui.components.Product
import com.example.groceries_app.ui.theme.GSshopTheme

/**
 * Demo composable to showcase navigation between HomeScreen and ProductDetailScreen
 * This demonstrates how to integrate the ProductDetailScreen into your app
 */
@Composable
fun ProductDetailDemo() {
    var showProductDetail by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    if (showProductDetail && selectedProduct != null) {
        // Show product detail by ID
        val productId = selectedProduct!!.id

        ProductDetailScreen(
            productId = productId,
            onBackClick = {
                showProductDetail = false
                selectedProduct = null
            },
            onAddToBasket = { quantity ->
                // Handle add to basket
                println("Added $quantity items to basket")
            }
        )
    } else {
        HomeScreen(
            onProductClick = { product ->
                selectedProduct = product
                showProductDetail = true
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailDemoPreview() {
    GSshopTheme {
        ProductDetailDemo()
    }
}
