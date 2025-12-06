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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.viewmodel.NectarProductViewModel
import com.example.groceries_app.viewmodel.CategoryState

data class ProductCategory(
    val id: String,
    val name: String,
    val imageRes: Int,
    val backgroundColor: Color,
    val borderColor: Color,
    val imageUrl: String? = null  // Support for API image URLs
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (ProductCategory) -> Unit = {},
    onSearchClick: (String) -> Unit = {},
    viewModel: NectarProductViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val categoryState by viewModel.categoriesState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }
    
    // Convert API categories to UI categories
    val categories = when (categoryState) {
        is CategoryState.Success -> {
            val apiCategories = (categoryState as CategoryState.Success).categories
            apiCategories.mapIndexed { index, apiCategory ->
                ProductCategory(
                    id = apiCategory.uuid,
                    name = apiCategory.name,
                    imageRes = com.example.groceries_app.R.drawable.img_4,
                    backgroundColor = getCategoryBackgroundColor(index),
                    borderColor = getCategoryBorderColor(index),
                    imageUrl = apiCategory.imageUrl
                )
            }
        }
        is CategoryState.Loading -> {
            emptyList() // Show loading state
        }
        is CategoryState.Error -> {
            getDefaultCategories() // Fallback to default
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Text(
            text = "Find Products",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .clickable {
                    onSearchClick(searchQuery)
                },
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
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E2E2),
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedContainerColor = Color(0xFFF2F3F2),
                focusedContainerColor = Color(0xFFF2F3F2)
            ),
            shape = RoundedCornerShape(15.dp),
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

        // Category Grid
        if (categoryState is CategoryState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: ProductCategory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = category.borderColor.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Category Image - Use AsyncImage for API URLs
            if (category.imageUrl != null && category.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = category.imageUrl,
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(100.dp)
                        .weight(1f),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = category.imageRes),
                    error = painterResource(id = category.imageRes)
                )
            } else {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(100.dp)
                        .weight(1f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Name
            Text(
                text = category.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExploreScreenPreview() {
    GSshopTheme {
        ExploreScreen()
    }
}

// Helper functions for category colors
fun getCategoryBackgroundColor(index: Int): Color {
    val colors = listOf(
        Color(0xFFE8F5E9),  // Green
        Color(0xFFFFF3E0),  // Orange
        Color(0xFFFFEBEE),  // Pink
        Color(0xFFF3E5F5),  // Purple
        Color(0xFFFFFDE7),  // Yellow
        Color(0xFFE3F2FD),  // Blue
        Color(0xFFF1F8E9),  // Light Green
        Color(0xFFFCE4EC)   // Light Pink
    )
    return colors[index % colors.size]
}

fun getCategoryBorderColor(index: Int): Color {
    val colors = listOf(
        Color(0xFF4CAF50),  // Green
        Color(0xFFFF9800),  // Orange
        Color(0xFFF48FB1),  // Pink
        Color(0xFFCE93D8),  // Purple
        Color(0xFFFFEB3B),  // Yellow
        Color(0xFF42A5F5),  // Blue
        Color(0xFF9CCC65),  // Light Green
        Color(0xFFF06292)   // Light Pink
    )
    return colors[index % colors.size]
}

fun getDefaultCategories(): List<ProductCategory> {
    return listOf(
        ProductCategory(
            "1",
            "Fresh Fruits\n& Vegetable",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFE8F5E9),
            Color(0xFF4CAF50)
        ),
        ProductCategory(
            "2",
            "Cooking Oil\n& Ghee",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFF3E0),
            Color(0xFFFF9800)
        ),
        ProductCategory(
            "3",
            "Meat & Fish",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFEBEE),
            Color(0xFFF48FB1)
        ),
        ProductCategory(
            "4",
            "Bakery & Snacks",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFF3E5F5),
            Color(0xFFCE93D8)
        ),
        ProductCategory(
            "5",
            "Dairy & Eggs",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFFDE7),
            Color(0xFFFFEB3B)
        ),
        ProductCategory(
            "6",
            "Beverages",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFE3F2FD),
            Color(0xFF42A5F5)
        )
    )
}

