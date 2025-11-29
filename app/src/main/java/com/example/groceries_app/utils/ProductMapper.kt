package com.example.groceries_app.utils

import com.example.groceries_app.R
import com.example.groceries_app.data.model.Product as ApiProduct
import com.example.groceries_app.ui.components.Product as UiProduct

/**
 * Extension function to convert API Product to UI Product
 * Safely handles null values from API and supports DummyJSON format
 */
fun ApiProduct.toUiProduct(): UiProduct? {
    // Get the product name (handles both "title" and "name" fields)
    val productName = this.productName

    // Return null if essential fields are invalid
    if (productName.isEmpty() || productName == "Unknown Product" || this.price <= 0) {
        return null
    }

    return UiProduct(
        id = this.id,
        name = productName,
        weight = this.unit ?: this.weight ?: "1kg",  // Use unit, weight, or default
        price = this.price,
        imageRes = R.drawable.img_2  // Default image, you can map based on category
    )
}

/**
 * Convert list of API Products to UI Products
 * Filters out any null results (invalid products)
 */
fun List<ApiProduct>.toUiProducts(): List<UiProduct> {
    return this.mapNotNull { it.toUiProduct() }
}

