package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.ui.components.Product
import com.example.groceries_app.ui.components.ProductCard
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.viewmodel.NectarProductViewModel
import com.example.groceries_app.viewmodel.NectarProductState
import com.example.groceries_app.ui.components.BottomNavigationBar
import com.example.groceries_app.utils.toUiProducts

// Grocery category data model
data class GroceryCategory(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val backgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit = {},
    onAddToCart: (Product) -> Unit = {},
    onCategoryClick: (GroceryCategory) -> Unit = {},
    onLocationClick: () -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var searchQuery by remember { mutableStateOf("") }

    // Nectar API Integration
    val nectarViewModel: NectarProductViewModel = viewModel()
    val productsState by nectarViewModel.productsState.collectAsState()
    val isLoading by nectarViewModel.isLoading.collectAsState()
    val error by nectarViewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Carrot Icon and Location
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = com.example.groceries_app.R.drawable.img_4),
                contentDescription = "Carrot Logo",
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLocationClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Dhaka, Banassre",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onSearchClick(searchQuery) },
            placeholder = {
                Text(
                    text = "Search Store",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                IconButton(onClick = { onSearchClick(searchQuery) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F3F2),
                unfocusedContainerColor = Color(0xFFF2F3F2),
                disabledContainerColor = Color(0xFFF2F3F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onSearch = {
                    onSearchClick(searchQuery)
                }
            ),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                imeAction = androidx.compose.ui.text.input.ImeAction.Search
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Banner
        BannerCard()

        Spacer(modifier = Modifier.height(24.dp))

        // Exclusive Offer Section
        SectionHeader(title = "Exclusive Offer", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        // API Integration - Featured Products (Exclusive Offer)
        when (val state = nectarViewModel.productsState.collectAsState().value) {
            is NectarProductState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is NectarProductState.Success -> {
                val uiProducts = state.products.toUiProducts().take(6)

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { onAddToCart(product) }
                        )
                    }
                }
            }
            is NectarProductState.Error -> {
                // Show error or empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load products",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Best Selling Section
        SectionHeader(title = "Best Selling", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        // API Integration - Best Selling Products
        when (val state = nectarViewModel.productsState.collectAsState().value) {
            is NectarProductState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is NectarProductState.Success -> {
                val uiProducts = state.products.toUiProducts().drop(6).take(6)

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { onAddToCart(product) }
                        )
                    }
                }
            }
            is NectarProductState.Error -> {
                // Show error or empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load products",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Groceries Section
        SectionHeader(title = "Groceries", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        // Show categories from API products if available, otherwise show default
        when (productsState) {
            is NectarProductState.Success -> {
                val categories = (productsState as NectarProductState.Success).products
                    .mapNotNull { it.category }
                    .distinct()
                    .take(4)
                    .mapIndexed { index, categoryName ->
                        GroceryCategory(
                            id = index,
                            name = categoryName,
                            imageRes = com.example.groceries_app.R.drawable.img_4,
                            backgroundColor = if (index % 2 == 0) Color(0xFFF8A44C) else Color(0xFF53B175)
                        )
                    }
                
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        GroceryCategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }
            else -> {
                // Show nothing or a placeholder during loading/error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Products from Nectar API
        when (productsState) {
            is NectarProductState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is NectarProductState.Success -> {
                val apiProducts = (productsState as NectarProductState.Success).products
                val uiProducts = apiProducts.toUiProducts()

                if (uiProducts.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiProducts) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = { onProductClick(product) },
                                onAddToCart = { onAddToCart(product) }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }
            is NectarProductState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Failed to load products",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { nectarViewModel.loadProducts() },
                        colors = ButtonDefaults.buttonColors(containerColor = NectarGreen)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
    }
}

@Composable
fun BannerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F3F2))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Banner content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Fresh Vegetables",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Get Up To 40% OFF",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = NectarGreen
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = "See all",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = NectarGreen
            )
        }
    }
}

@Composable
fun GroceryCategoryCard(
    category: GroceryCategory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = category.backgroundColor.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🥘", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = category.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

// No more static data - all products fetched from nectar-API

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    GSshopTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar() }
        ) { paddingValues ->
            HomeScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

