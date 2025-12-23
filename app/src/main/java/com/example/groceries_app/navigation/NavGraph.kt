package com.example.groceries_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.groceries_app.ui.screens.AccountScreen
import com.example.groceries_app.ui.screens.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceries_app.viewmodel.CartViewModel

/**
 * Navigation graph for the Groceries App
 * 
 * Integrated with nectar-api backend:
 * - Authentication: Uses email/password login and phoneNumber/name/gender/dob registration
 * - Products: Fetches from nectar-api with UUID-based identifiers
 * - Orders: Creates orders with orderNumber, totalPrice, status, and items
 * 
 * All API models are synchronized with the Spring Boot backend
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    // Shared CartViewModel across navigation
    val cartViewModel: CartViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome Screen
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        // Sign In Screen
        composable(route = Screen.SignIn.route) {
            SignInScreen(
                onGoogleSignIn = {
                    // After Google sign in, go to location selection
                    navController.navigate(Screen.LocationSelection.route)
                },
                onFacebookSignIn = {
                    // After Facebook sign in, go to location selection
                    navController.navigate(Screen.LocationSelection.route)
                },
                onEmailSignIn = {
                    navController.navigate(Screen.Login.route)
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        // Sign Up Screen
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { email, password, name ->
                    // After successful sign up, go to location selection or home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onSignInClick = {
                    navController.navigateUp()
                },
                onBackClick = {
                    navController.navigateUp()
                },
                onTermsClick = {
                    // TODO: Navigate to terms screen
                },
                onPrivacyClick = {
                    // TODO: Navigate to privacy screen
                }
            )
        }

        // Location Selection Screen
        composable(route = Screen.LocationSelection.route) {
            LocationSelectionScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onSubmitClick = { zone, area ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // TODO: Call AuthViewModel.login with nectar-api integration
                    // After successful login, go to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.navigateUp()
                },
                onForgotPasswordClick = {
                    // TODO: Navigate to forgot password screen
                },
                onSignupClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                onProductClick = { product ->
                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                },
                onAddToCart = { product ->
                    cartViewModel.addToCart(
                        CartItem(
                            id = product.id,
                            name = product.name ?: "Unknown",
                            size = product.weight ?: "",
                            weight = product.weight ?: "",
                            price = product.price,
                            imageRes = product.imageRes,
                            quantity = 1,
                            imageUrl = product.imageUrl,
                            productUuid = product.id  // Store product UUID for API
                        )
                    )
                },
                onSearchClick = { query ->
                    navController.navigate(Screen.SearchResults.createRoute(query))
                }
            )
        }

        // Explore Screen
        composable(route = "explore") {
            ExploreScreen(
                modifier = modifier,
                onCategoryClick = { category ->
                    navController.navigate(
                        Screen.CategoryProducts.createRoute(category.id, category.name.replace("\n", " "))
                    )
                },
                onSearchClick = { query ->
                    navController.navigate(Screen.SearchResults.createRoute(query))
                }
            )
        }

        // Cart Screen
        composable(route = "cart") {
            val cartItems by cartViewModel.cartItems.collectAsState()
            CartScreen(
                modifier = modifier,
                cartItems = cartItems,
                onNavigateToPayment = { amount ->
                    // Navigate to payment screen with amount
                    navController.navigate(Screen.Payment.createRoute(amount))
                },
                onOrderPlaced = {
                    // Navigate to order success screen
                    navController.navigate(Screen.OrderAccepted.route) {
                        popUpTo("cart") { inclusive = true }
                    }
                    // Clear cart after order placed
                    cartViewModel.clearCart()
                },
                onOrderFailed = {
                    // Navigate to order failed screen
                    navController.navigate(Screen.OrderFailed.route)
                }
            )
        }

        // Favourite Screen
        composable(route = "favourite") {
            FavouriteScreen(
                modifier = modifier,
                cartViewModel = cartViewModel,
                onProductClick = { favouriteItem ->
                    // Navigate to product detail
                    navController.navigate(Screen.ProductDetail.createRoute(favouriteItem.id))
                },
                onAddAllToCart = {
                    // Navigate to cart after adding all items
                    navController.navigate("cart") {
                        // Don't add to back stack, just switch tabs
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Account Screen
        composable(route = "account") {
            AccountScreen(
                modifier = modifier,
                onMenuItemClick = { route ->
                    // Navigate to different account sections
                    when (route) {
                        "orders" -> { /* Navigate to orders screen */
                        }

                        "my_details" -> { /* Navigate to my details screen */
                        }

                        "delivery_address" -> { /* Navigate to delivery address screen */
                        }

                        "payment_methods" -> { /* Navigate to payment methods screen */
                        }

                        "promo_card" -> { /* Navigate to promo card screen */
                        }

                        "notifications" -> { /* Navigate to notifications screen */
                        }

                        "help" -> { /* Navigate to help screen */
                        }

                        "about" -> { /* Navigate to about screen */
                        }

                        "edit_profile" -> { /* Navigate to edit profile screen */
                        }
                    }
                },
                onLogout = {
                    // Handle logout
                    // Clear user data and navigate to welcome screen
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Product Detail Screen
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""

            ProductDetailScreen(
                productId = productId,
                cartViewModel = cartViewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onAddToBasket = { quantity ->
                    // Optionally navigate to cart or just stay on detail page
                    // Removed auto-navigation to allow user to continue browsing
                }
            )
        }

        // Category Products Screen
        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                },
                navArgument("categoryName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Products"

            CategoryProductsScreen(
                categoryName = categoryName,
                onBackClick = {
                    navController.navigateUp()
                },
                onFilterClick = {
                    navController.navigate(Screen.Filter.route)
                },
                onProductClick = { product ->
                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                },
                onAddToCart = { beverageProduct ->
                    cartViewModel.addToCart(
                        CartItem(
                            id = beverageProduct.id,
                            name = beverageProduct.name,
                            size = beverageProduct.size,
                            weight = beverageProduct.size,
                            price = beverageProduct.price,
                            imageRes = beverageProduct.imageRes,
                            quantity = 1,
                            productUuid = beverageProduct.id  // Store product UUID for API
                        )
                    )
                }
            )
        }

        // Search Results Screen
        composable(
            route = Screen.SearchResults.route,
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""

            SearchResultsScreen(
                initialQuery = query,
                onBackClick = {
                    navController.navigateUp()
                },
                onFilterClick = {
                    navController.navigate(Screen.Filter.route)
                },
                onProductClick = { product ->
                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                },
                onAddToCart = { product ->
                    cartViewModel.addToCart(
                        CartItem(
                            id = product.id,
                            name = product.name,
                            size = product.size,
                            weight = product.size,
                            price = product.price,
                            imageRes = product.imageRes,
                            quantity = 1,
                            productUuid = product.id  // Store product UUID for API
                        )
                    )
                }
            )
        }

        // Filter Screen
        composable(route = Screen.Filter.route) {
            FilterScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onApplyFilter = { selectedCategories, selectedBrands ->
                    // Process filters and pass back to previous screen
                    val categoryNames = selectedCategories.joinToString(",") { it.name }
                    val brandNames = selectedBrands.joinToString(",") { it.name }

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedCategories", categoryNames)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedBrands", brandNames)

                    navController.navigateUp()
                }
            )
        }

        // Order Accepted Screen
        composable(route = Screen.OrderAccepted.route) {
            OrderAcceptedScreen(
                onTrackOrder = {
                    // TODO: Navigate to order tracking screen
                },
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Payment Screen
        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amount") ?: "0.0"
            val amount = amountStr.toDoubleOrNull() ?: 0.0

            PaymentScreen(
                amount = amount,
                onPaymentSuccess = {
                    // Navigate to order accepted screen
                    navController.navigate(Screen.OrderAccepted.route) {
                        popUpTo("cart") { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        // Order Failed Screen
        composable(route = Screen.OrderFailed.route) {
            OrderFailedScreen(
                onDismiss = {
                    navController.navigateUp()
                },
                onTryAgain = {
                    // Go back to cart to try again
                    navController.navigate("cart") {
                        popUpTo(Screen.OrderFailed.route) { inclusive = true }
                    }
                },
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

