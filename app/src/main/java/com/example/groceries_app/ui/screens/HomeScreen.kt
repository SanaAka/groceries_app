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
import com.example.groceries_app.viewmodel.ProductViewModel
import com.example.groceries_app.data.network.Resource
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
    onCategoryClick: (GroceryCategory) -> Unit = {},
    onLocationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // API Integration - Get ViewModels
    val productViewModel: ProductViewModel = viewModel()
    val productsState by productViewModel.productsState.collectAsState()
    val featuredState by productViewModel.featuredProductsState.collectAsState()
    val bestSellingState by productViewModel.bestSellingProductsState.collectAsState()

    // Fetch data from API when screen loads
    LaunchedEffect(Unit) {
        productViewModel.getProducts(page = 1, limit = 10)
        productViewModel.getFeaturedProducts(limit = 5)
        productViewModel.getBestSellingProducts(limit = 5)
    }

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
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = "Search Store",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F3F2),
                unfocusedContainerColor = Color(0xFFF2F3F2),
                disabledContainerColor = Color(0xFFF2F3F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Banner
        BannerCard()

        Spacer(modifier = Modifier.height(24.dp))

        // Exclusive Offer Section
        SectionHeader(title = "Exclusive Offer", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        // API Integration - Featured Products
        when (featuredState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is Resource.Success -> {
                val apiProducts = featuredState.data?.products ?: emptyList()
                val uiProducts = apiProducts.toUiProducts()

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
                    }
                }
            }
            is Resource.Error -> {
                // Fallback to mock data on error
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getExclusiveProducts()) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Best Selling Section
        SectionHeader(title = "Best Selling", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        // API Integration - Best Selling Products
        when (bestSellingState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is Resource.Success -> {
                val apiProducts = bestSellingState.data?.products ?: emptyList()
                val uiProducts = apiProducts.toUiProducts()

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
                    }
                }
            }
            is Resource.Error -> {
                // Fallback to mock data on error
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getBestSellingProducts()) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Groceries Section
        SectionHeader(title = "Groceries", onSeeAllClick = {})

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getGroceryCategories()) { category ->
                GroceryCategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // More Grocery Products
        when (productsState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NectarGreen)
                }
            }
            is Resource.Success -> {
                val apiProducts = productsState.data?.products ?: emptyList()
                val uiProducts = apiProducts.take(5).toUiProducts() // Take first 5 products

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
                    }
                }
            }
            is Resource.Error -> {
                // Fallback to mock data on error
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getGroceryProducts()) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { /* Handle add to cart */ }
                        )
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

// Sample data functions matching the new Product model
fun getExclusiveProducts(): List<Product> {
    return listOf(
        Product(1, "Organic Bananas", "7pcs, Priceg", 4.99, com.example.groceries_app.R.drawable.img_2),
        Product(2, "Red Apple", "1kg, Priceg", 4.99, com.example.groceries_app.R.drawable.img_4),
        Product(3, "Orange", "1kg, Priceg", 3.99, com.example.groceries_app.R.drawable.img_3)
    )
}

fun getBestSellingProducts(): List<Product> {
    return listOf(
        Product(4, "Bell Pepper Red", "1kg, Priceg", 4.99, com.example.groceries_app.R.drawable.img_4),
        Product(5, "Ginger", "250gm, Priceg", 2.99, com.example.groceries_app.R.drawable.img_3),
        Product(6, "Fresh Lettuce", "1kg, Priceg", 3.49, com.example.groceries_app.R.drawable.img_1)
    )
}

fun getGroceryCategories(): List<GroceryCategory> {
    return listOf(
        GroceryCategory(1, "Pulses", com.example.groceries_app.R.drawable.img_4, Color(0xFFF8A44C)),
        GroceryCategory(2, "Rice", com.example.groceries_app.R.drawable.img_4, Color(0xFF53B175))
    )
}

fun getGroceryProducts(): List<Product> {
    return listOf(
        Product(7, "Beef Bone", "1kg, Priceg", 5.99, com.example.groceries_app.R.drawable.img_4),
        Product(8, "Broiler Chicken", "1kg, Priceg", 6.99, com.example.groceries_app.R.drawable.img_1)
    )
}

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

