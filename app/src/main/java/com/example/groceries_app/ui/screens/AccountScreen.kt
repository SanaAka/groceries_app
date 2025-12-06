package com.example.groceries_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.groceries_app.ui.theme.GSshopTheme
import com.example.groceries_app.ui.theme.NectarGreen
import com.example.groceries_app.utils.SessionManager

data class AccountMenuItem(
    val icon: ImageVector,
    val title: String,
    val route: String
)

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    
    // Get user info from session
    val userName = remember { sessionManager.getUsername() ?: "Guest User" }
    val userEmail = remember { sessionManager.getEmail() ?: "guest@example.com" }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        ProfileHeader(
            name = userName,
            email = userEmail,
            profileImageRes = com.example.groceries_app.R.drawable.img_1,
            onEditClick = { onMenuItemClick("edit_profile") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            color = Color(0xFFE2E2E2),
            thickness = 1.dp
        )

        // Menu Items
        val menuItems = listOf(
            AccountMenuItem(Icons.Outlined.ShoppingCart, "Orders", "orders"),
            AccountMenuItem(Icons.Outlined.Person, "My Details", "my_details"),
            AccountMenuItem(Icons.Outlined.LocationOn, "Delivery Address", "delivery_address"),
            AccountMenuItem(Icons.Outlined.AccountBox, "Payment Methods", "payment_methods"),
            AccountMenuItem(Icons.Outlined.Star, "Promo Card", "promo_card"),
            AccountMenuItem(Icons.Outlined.Notifications, "Notifecations", "notifications"),
            AccountMenuItem(Icons.Outlined.Settings, "Help", "help"),
            AccountMenuItem(Icons.Outlined.Info, "About", "about")
        )

        menuItems.forEach { item ->
            AccountMenuItemRow(
                icon = item.icon,
                title = item.title,
                onClick = { onMenuItemClick(item.route) }
            )
            HorizontalDivider(
                color = Color(0xFFE2E2E2),
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Button
        Button(
            onClick = {
                sessionManager.clearSession()
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(67.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF2F3F2)
            ),
            shape = RoundedCornerShape(19.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Logout",
                    tint = NectarGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Log Out",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NectarGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    profileImageRes: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image with gradient border
        Box(
            modifier = Modifier.size(64.dp)
        ) {
            // Gradient border
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF64B5F6),
                                Color(0xFFBA68C8)
                            )
                        )
                    )
                    .padding(2.dp)
            ) {
                // Profile Image
                Image(
                    painter = painterResource(id = profileImageRes),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Name and Email
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181725)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = NectarGreen,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onEditClick)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 16.sp,
                color = Color(0xFF7C7C7C)
            )
        }
    }
}

@Composable
fun AccountMenuItemRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 25.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF181725),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF181725),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = Color(0xFF181725),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    GSshopTheme {
        AccountScreen()
    }
}

