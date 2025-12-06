package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.ui.screens.CartItem
import com.example.groceries_app.viewmodel.CartViewModel
import com.example.groceries_app.viewmodel.FavoriteProduct
import com.example.groceries_app.viewmodel.FavoritesViewModel
import com.example.groceries_app.viewmodel.NectarProductViewModel
import com.example.groceries_app.viewmodel.NectarProductState
import com.example.groceries_app.utils.toUiProducts
import java.util.Locale

data class ProductDetail(
    val id: String,  // Changed from Int to String to match Product.uuid
    val name: String,
    val weight: String,
    val price: Double,
    val imageRes: Int,
    val description: String,
    val nutritionInfo: String = "100gr",
    val imageUrl: String? = null  // Support for API image URLs
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAddToBasket: (Int) -> Unit = {},
    cartViewModel: CartViewModel? = null
) {
    val context = LocalContext.current
    val favoritesViewModel = remember { FavoritesViewModel(context) }
    val productViewModel: NectarProductViewModel = viewModel()
    val productDetailState by productViewModel.productDetailState.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val favorites by favoritesViewModel.favorites.collectAsState()
    
    var quantity by remember { mutableStateOf(1) }
    val isFavorite = favorites.any { it.id == productId }
    var isProductDetailExpanded by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    // Fetch product details when screen loads
    LaunchedEffect(productId) {
        productViewModel.getProductByUuid(productId)
    }
    
    // Convert API Product to ProductDetail
    val product = productDetailState?.let { apiProduct ->
        ProductDetail(
            id = apiProduct.uuid ?: productId,
            name = apiProduct.name ?: "Unknown Product",
            weight = apiProduct.category ?: "N/A",  // Use category as weight
            price = apiProduct.price,
            imageRes = com.example.groceries_app.R.drawable.img_4, // Fallback
            description = apiProduct.description ?: "No description available",
            nutritionInfo = "100gr",
            imageUrl = apiProduct.imageUrl
        )
    }
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = NectarGreen)
        }
        return
    }
    
    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Product not found", fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share action */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    product?.let { prod ->
                        cartViewModel?.addToCart(
                            CartItem(
                                id = prod.id,
                                name = prod.name,
                                size = prod.weight,
                                weight = prod.weight,
                                price = prod.price,
                                imageRes = prod.imageRes,
                                quantity = quantity,
                                imageUrl = prod.imageUrl,
                                productUuid = prod.id
                            )
                        )
                    }
                    onAddToBasket(quantity)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NectarGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Add To Basket",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Image Carousel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFFF2F3F2))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (product.imageUrl != null && product.imageUrl.isNotEmpty()) {
                            // Load image from URL using Coil
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = product.imageRes),
                                error = painterResource(id = product.imageRes)
                            )
                        } else {
                            // Fallback to local image
                            Image(
                                painter = painterResource(id = product.imageRes),
                                contentDescription = product.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                // Page Indicator
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (pagerState.currentPage == index) NectarGreen
                                    else Color(0xFFD9D9D9)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product Title and Favorite
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.weight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = {
                        product?.let {
                            favoritesViewModel.toggleFavorite(
                                FavoriteProduct(
                                    id = it.id,
                                    name = it.name,
                                    price = it.price,
                                    imageUrl = it.imageUrl,
                                    category = it.weight
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quantity Selector and Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Minus Button
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                    ) {
                        Text(
                            text = "−",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    }

                    // Quantity Display
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = quantity.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }

                    // Plus Button
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            color = NectarGreen
                        )
                    }
                }

                // Price
                Text(
                    text = "$${String.format(Locale.US, "%.2f", product.price)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Product Detail Section
            ProductDetailItem(
                title = "Product Detail",
                isExpanded = isProductDetailExpanded,
                onExpandClick = { isProductDetailExpanded = !isProductDetailExpanded }
            ) {
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Nutritions Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nutritions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEBEBEB),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = product.nutritionInfo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Nutritions",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Review Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Review",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Star Rating
                    repeat(5) {
                        Text(
                            text = "⭐",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Reviews",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProductDetailItem(
    title: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            IconButton(onClick = onExpandClick) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (isExpanded) {
            content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailScreenPreview() {
    GSshopTheme {
        ProductDetailScreen(
            productId = "sample-uuid-123"
        )
    }
}

