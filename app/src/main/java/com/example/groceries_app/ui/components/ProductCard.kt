package com.example.groceries_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groceries_app.R
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen

data class Product(
    val id: Int = 0,
    val name: String? = "Unknown",
    val weight: String? = "",
    val price: Double = 0.0,
    val imageRes: Int = com.example.groceries_app.R.drawable.img_2
)

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(173.dp)
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E2E2),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Product Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name ?: "product",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Product Name
                Text(
                    text = product.name ?: "Unknown Product",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181725),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Product Weight
                Text(
                    text = product.weight ?: "",
                    fontSize = 14.sp,
                    color = Color(0xFF7C7C7C),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price and Add Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Text(
                        text = "$%.2f".format(product.price),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181725)
                    )

                    // Add Button
                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(RoundedCornerShape(17.dp))
                            .background(NectarGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    GSshopTheme {
        ProductCard(
            product = Product(
                id = 1,
                name = "Organic Bananas",
                weight = "7pcs, Priceg",
                price = 4.99,
                imageRes = R.drawable.img_2
            )
        )
    }
}
