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
import com.example.groceries_app.ui.theme.GSshopTheme

data class ProductCategory(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val backgroundColor: Color,
    val borderColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (ProductCategory) -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        ProductCategory(
            1,
            "Fresh Fruits\n& Vegetable",
            com.example.groceries_app.R.drawable.img_4, // Using existing image
            Color(0xFFE8F5E9),
            Color(0xFF4CAF50)
        ),
        ProductCategory(
            2,
            "Cooking Oil\n& Ghee",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFF3E0),
            Color(0xFFFF9800)
        ),
        ProductCategory(
            3,
            "Meat & Fish",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFEBEE),
            Color(0xFFF48FB1)
        ),
        ProductCategory(
            4,
            "Bakery & Snacks",
            com.example.groceries_app.R.drawable.img_4,

            Color(0xFFF3E5F5),
            Color(0xFFCE93D8)
        ),
        ProductCategory(
            5,
            "Dairy & Eggs",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFFFFDE7),
            Color(0xFFFFEB3B)
        ),
        ProductCategory(
            6,
            "Beverages",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFE3F2FD),
            Color(0xFF42A5F5)
        ),
        ProductCategory(
            7,
            "Snacks & Munchies",
            com.example.groceries_app.R.drawable.img_4,
            Color(0xFFF1F8E9),
            Color(0xFF9CCC65)
        ),
        ProductCategory(
            8,
            "Beauty & Health",
            com.example.groceries_app.R.drawable.img_4,

            Color(0xFFFCE4EC),
            Color(0xFFF06292)
        )
    )

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
            // Category Image
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.name,
                modifier = Modifier
                    .size(100.dp)
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

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

