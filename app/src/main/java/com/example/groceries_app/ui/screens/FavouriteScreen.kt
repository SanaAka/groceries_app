package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.ui.screens.CartItem
import com.example.groceries_app.viewmodel.CartViewModel
import com.example.groceries_app.viewmodel.FavoritesViewModel

data class FavouriteItem(
    val id: String,
    val name: String,
    val size: String,
    val price: Double,
    val imageRes: Int,
    val imageUrl: String? = null  // Support for API image URLs
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    onProductClick: (FavouriteItem) -> Unit = {},
    onAddAllToCart: () -> Unit = {},
    cartViewModel: CartViewModel? = null
) {
    val context = LocalContext.current
    val favoritesViewModel = remember { FavoritesViewModel(context) }
    val favorites by favoritesViewModel.favorites.collectAsState()
    
    // Convert FavoriteProduct to FavouriteItem for display
    val favouriteItems = favorites.map { favorite ->
        FavouriteItem(
            id = favorite.id,
            name = favorite.name,
            size = favorite.category ?: "N/A",
            price = favorite.price,
            imageRes = com.example.groceries_app.R.drawable.img_4,
            imageUrl = favorite.imageUrl
        )
    }
    val isLoading = false

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favourite",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    // Add all favorite items to cart
                    favouriteItems.forEach { item ->
                        cartViewModel?.addToCart(
                            CartItem(
                                id = item.id,
                                name = item.name,
                                size = item.size,
                                weight = item.size,
                                price = item.price,
                                imageRes = item.imageRes,
                                quantity = 1,
                                imageUrl = item.imageUrl,
                                productUuid = item.id
                            )
                        )
                    }
                    onAddAllToCart()
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
                    text = "Add All To Cart",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NectarGreen)
            }
        } else if (favouriteItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Favourites Yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add items to your favourites to see them here",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(favouriteItems) { item ->
                    FavouriteItemCard(
                        favouriteItem = item,
                        onItemClick = { onProductClick(item) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFE2E2E2),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun FavouriteItemCard(
    favouriteItem: FavouriteItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        if (favouriteItem.imageUrl != null && favouriteItem.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = favouriteItem.imageUrl,
                contentDescription = favouriteItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 20.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = favouriteItem.imageRes),
                error = painterResource(id = favouriteItem.imageRes)
            )
        } else {
            Image(
                painter = painterResource(id = favouriteItem.imageRes),
                contentDescription = favouriteItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 20.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Product Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = favouriteItem.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181725)
            )

            Text(
                text = favouriteItem.size,
                fontSize = 14.sp,
                color = Color(0xFF7C7C7C),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Price and Arrow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$%.2f".format(favouriteItem.price),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF181725)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View details",
                tint = Color(0xFF181725),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavouriteScreenPreview() {
    GSshopTheme {
        FavouriteScreen()
    }
}

