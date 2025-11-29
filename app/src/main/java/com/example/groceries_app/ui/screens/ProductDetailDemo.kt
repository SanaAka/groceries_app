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
        // Convert Product to ProductDetail
        val product = selectedProduct!!
        val productDetail = ProductDetail(
            id = product.id,
            name = product.name ?: "Unknown Product",
            weight = product.weight ?: "",
            price = product.price,
            imageRes = product.imageRes,
            description = "Apples Are Nutritious. Apples May Be Good For Weight Loss. Apples May Be Good For Your Heart. As Part Of A Healtful And Varied Diet.",
            nutritionInfo = "100gr"
        )

        ProductDetailScreen(
            product = productDetail,
            onBackClick = {
                showProductDetail = false
                selectedProduct = null
            },
            onAddToBasket = { quantity ->
                // Handle add to basket
                println("Added $quantity ${productDetail.name} to basket")
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
