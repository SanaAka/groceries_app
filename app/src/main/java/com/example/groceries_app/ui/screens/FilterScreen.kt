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
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

data class FilterCategory(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)

data class FilterBrand(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onApplyFilter: (List<FilterCategory>, List<FilterBrand>) -> Unit = { _, _ -> }
) {
    // Sample data - you can pass this as parameters or fetch from ViewModel
    val categories = remember {
        mutableStateListOf(
            FilterCategory(1, "Eggs", isSelected = true),
            FilterCategory(2, "Noodles & Pasta", isSelected = false),
            FilterCategory(3, "Chips & Crisps", isSelected = false),
            FilterCategory(4, "Fast Food", isSelected = false)
        )
    }

    val brands = remember {
        mutableStateListOf(
            FilterBrand(1, "Individual Collection", isSelected = false),
            FilterBrand(2, "Cocola", isSelected = true),
            FilterBrand(3, "Ifad", isSelected = false),
            FilterBrand(4, "Kazi Farmas", isSelected = false)
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

