package com.example.groceries_app.utils

import com.example.groceries_app.R
import com.example.groceries_app.data.model.Product as ApiProduct
import com.example.groceries_app.ui.components.Product as UiProduct

/**
 * Extension function to convert API Product to UI Product
 * Maps nectar-api ProductResponse to UI Product
 */
fun ApiProduct.toUiProduct(): UiProduct? {
    // Get the product name - handle null or empty
    val productName = this.name?.takeIf { it.isNotEmpty() } ?: return null

    // Return null if essential fields are invalid
    if (productName == "Unknown Product" || this.price <= 0) {
        return null
    }

    return UiProduct(
        id = this.uuid,
        name = productName,
        weight = this.category ?: "General",  // Use category as weight placeholder
        price = this.price,
        imageRes = R.drawable.img_2,  // Default image
        imageUrl = this.imageUrl  // Pass API image URL
    )
}

/**
 * Convert list of API Products to UI Products
 * Filters out any null results (invalid products)
 */
fun List<ApiProduct>.toUiProducts(): List<UiProduct> {
    return this.mapNotNull { it.toUiProduct() }
}


