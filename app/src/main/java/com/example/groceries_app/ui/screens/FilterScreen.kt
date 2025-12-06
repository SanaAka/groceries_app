package com.example.groceries_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.viewmodel.NectarProductViewModel
import com.example.groceries_app.viewmodel.NectarProductState

data class FilterCategory(
    val id: String,  // Changed to String to support UUID or name
    val name: String,
    var isSelected: Boolean = false
)

data class FilterBrand(
    val id: String,  // Changed to String to support UUID or name
    val name: String,
    var isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onApplyFilter: (List<FilterCategory>, List<FilterBrand>) -> Unit = { _, _ -> },
    viewModel: NectarProductViewModel = viewModel()
) {
    val state by viewModel.productsState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }
    
    // Extract unique categories and brands from API products
    val categories = remember {
        mutableStateListOf<FilterCategory>()
    }
    
    val brands = remember {
        mutableStateListOf<FilterBrand>()
    }
    
    // Populate categories and brands from API data
    LaunchedEffect(state) {
        if (state is NectarProductState.Success) {
            val products = (state as NectarProductState.Success).products
            
            // Extract unique categories with UUIDs if available
            val apiCategories = products
                .mapNotNull { product -> 
                    product.category?.let { catName ->
                        FilterCategory(
                            id = product.uuid, // Use product UUID as category ID
                            name = catName,
                            isSelected = false
                        )
                    }
                }
                .distinctBy { it.name }
            
            // Extract unique brands (using first word of product name as brand approximation)
            val apiBrands = products
                .mapNotNull { product ->
                    product.name?.split(" ")?.firstOrNull()?.let { brandName ->
                        FilterBrand(
                            id = product.uuid,
                            name = brandName,
                            isSelected = false
                        )
                    }
                }
                .distinctBy { it.name }
                .take(10) // Limit to 10 brands
            
            if (categories.isEmpty() && apiCategories.isNotEmpty()) {
                categories.clear()
                categories.addAll(apiCategories)
            }
            
            if (brands.isEmpty() && apiBrands.isNotEmpty()) {
                brands.clear()
                brands.addAll(apiBrands)
            }
        }
    }
    
    // Fallback to default data if API fails
    if (categories.isEmpty()) {
        categories.addAll(
            listOf(
                FilterCategory("eggs", "Eggs", isSelected = true),
                FilterCategory("noodles", "Noodles & Pasta", isSelected = false),
                FilterCategory("chips", "Chips & Crisps", isSelected = false),
                FilterCategory("fastfood", "Fast Food", isSelected = false)
            )
        )
    }
    
    if (brands.isEmpty()) {
        brands.addAll(
            listOf(
                FilterBrand("individual", "Individual Collection", isSelected = false),
                FilterBrand("cocola", "Cocola", isSelected = true),
                FilterBrand("ifad", "Ifad", isSelected = false),
                FilterBrand("kazi", "Kazi Farmas", isSelected = false)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Filters",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    // Empty space to center the title
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    onApplyFilter(
                        categories.filter { it.isSelected },
                        brands.filter { it.isSelected }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "Apply Filter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        containerColor = Color(0xFFF2F3F2)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Categories Section
            FilterSection(
                title = "Categories",
                items = categories.map { it.name },
                selectedStates = categories.map { it.isSelected },
                onItemClick = { index ->
                    categories[index] = categories[index].copy(isSelected = !categories[index].isSelected)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Brand Section
            FilterSection(
                title = "Brand",
                items = brands.map { it.name },
                selectedStates = brands.map { it.isSelected },
                onItemClick = { index ->
                    brands[index] = brands[index].copy(isSelected = !brands[index].isSelected)
                }
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun FilterSection(
    title: String,
    items: List<String>,
    selectedStates: List<Boolean>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181725),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        items.forEachIndexed { index, item ->
            FilterItem(
                text = item,
                isSelected = selectedStates[index],
                onClick = { onItemClick(index) }
            )
            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FilterItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (isSelected) NectarGreen else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .then(
                    if (!isSelected) {
                        Modifier.background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(2.dp)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFB3B3B3))
                    ) {}
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) NectarGreen else Color(0xFF181725)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FilterScreenPreview() {
    GSshopTheme {
        FilterScreen()
    }
}

