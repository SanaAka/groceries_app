package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.viewmodel.ProductViewModel
import com.example.groceries_app.data.network.Resource

data class SearchProduct(
    val id: Int,
    val name: String,
    val size: String,
    val price: Double,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProductClick: (SearchProduct) -> Unit = {},
    onAddToCart: (SearchProduct) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }

    // API Integration - Get ViewModel
    val productViewModel: ProductViewModel = viewModel()
    val searchState by productViewModel.productsState.collectAsState()

    // Search when query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            productViewModel.searchProducts(searchQuery)
        }
    }

    // Sample products - fallback data
    val allProducts = remember {
        listOf(
            SearchProduct(1, "Egg Chicken Red", "4pcs, Price", 1.99, com.example.groceries_app.R.drawable.img_4),
            SearchProduct(2, "Egg Chicken White", "180g, Price", 1.50, com.example.groceries_app.R.drawable.img_4),
            SearchProduct(3, "Egg Pasta", "30gm, Price", 15.99, com.example.groceries_app.R.drawable.img_4),
            SearchProduct(4, "Egg Noodles", "2L, Price", 15.99, com.example.groceries_app.R.drawable.img_4),
            SearchProduct(5, "Mayonnais Eggless", "250ml, Price", 4.99, com.example.groceries_app.R.drawable.img_4),
            SearchProduct(6, "Egg Noodles", "500g, Price", 12.99, com.example.groceries_app.R.drawable.img_4)
        )
    }

    // Get filtered products from API or fallback
    val filteredProducts = when (searchState) {
        is Resource.Success -> {
            // Convert API products to SearchProduct
            searchState.data?.products?.map { apiProduct ->
                SearchProduct(
                    id = apiProduct.id,
                    name = apiProduct.productName,  // Use productName instead of name
                    size = apiProduct.unit ?: apiProduct.weight ?: "1kg",
                    price = apiProduct.price,
                    imageRes = com.example.groceries_app.R.drawable.img_4
                )
            } ?: allProducts
        }
        else -> {
            // Use local filtering as fallback
            if (searchQuery.isEmpty()) {
                allProducts
            } else {
                allProducts.filter { product ->
                    product.name.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search Bar Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Search Store",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E2E2),
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedContainerColor = Color(0xFFF2F3F2),
                    focusedContainerColor = Color(0xFFF2F3F2)
                ),
                shape = RoundedCornerShape(15.dp),
                singleLine = true
            )

            // Filter Button
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                    contentDescription = "Filter",
                    tint = Color.Black
                )
            }
        }

        // Products Grid
        if (filteredProducts.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No products found",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try searching with different keywords",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    SearchProductCard(
                        product = product,
                        onProductClick = { onProductClick(product) },
                        onAddToCart = { onAddToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchProductCard(
    product: SearchProduct,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE2E2E2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Name
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Product Size
            Text(
                text = product.size,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price and Add Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Add to Cart Button
                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            color = NectarGreen,
                            shape = RoundedCornerShape(17.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Add to cart",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchResultsScreenPreview() {
    GSshopTheme {
        SearchResultsScreen(
            initialQuery = "Egg"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchProductCardPreview() {
    GSshopTheme {
        SearchProductCard(
            product = SearchProduct(
                1,
                "Egg Chicken Red",
                "4pcs, Price",
                1.99,
                com.example.groceries_app.R.drawable.img_4
            ),
            onProductClick = {},
            onAddToCart = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchResultsScreenEmptyPreview() {
    GSshopTheme {
        SearchResultsScreen(
            initialQuery = "xyz"
        )
    }
}

