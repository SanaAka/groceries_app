package com.example.groceries_app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.groceries_app.ui.theme.GSshopTheme

/**
 * Demo/Preview file for FilterScreen
 *
 * This file demonstrates how to use and preview the FilterScreen component.
 * You can run this preview in Android Studio to see the UI.
 */

@Preview(
    name = "Filter Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FilterScreenPreviewLight() {
    GSshopTheme {
        FilterScreen(
            onBackClick = {
                // Handle back navigation
                println("Back clicked")
            },
            onApplyFilter = { selectedCategories, selectedBrands ->
                // Handle filter application
                println("Selected Categories: ${selectedCategories.map { it.name }}")
                println("Selected Brands: ${selectedBrands.map { it.name }}")
            }
        )
    }
}

@Preview(
    name = "Filter Screen - With Custom Data",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FilterScreenPreviewWithCustomData() {
    GSshopTheme {
        FilterScreen(
            onBackClick = {
                println("Back clicked")
            },
            onApplyFilter = { selectedCategories, selectedBrands ->
                println("Filters applied!")
                println("Categories: ${selectedCategories.joinToString { it.name }}")
                println("Brands: ${selectedBrands.joinToString { it.name }}")
            }
        )
    }
}

/**
 * Example of how to integrate FilterScreen with a ViewModel
 */
@Composable
fun FilterScreenWithViewModelExample() {
    // In a real app, you would use:
    // val viewModel: FilterViewModel = viewModel()
    // val categories by viewModel.categories.collectAsState()
    // val brands by viewModel.brands.collectAsState()

    FilterScreen(
        onBackClick = {
            // Navigate back
            // navController.navigateUp()
        },
        onApplyFilter = { selectedCategories, selectedBrands ->
            // Pass to ViewModel or handle directly
            // viewModel.applyFilters(selectedCategories, selectedBrands)

            // Example: Log the selections
            println("=== Filter Applied ===")
            println("Selected Categories (${selectedCategories.size}):")
            selectedCategories.forEach { category ->
                println("  - ${category.name} (ID: ${category.id})")
            }
            println("\nSelected Brands (${selectedBrands.size}):")
            selectedBrands.forEach { brand ->
                println("  - ${brand.name} (ID: ${brand.id})")
            }
        }
    )
}

