package com.example.groceries_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.groceries_app.ui.screens.AccountScreen
import com.example.groceries_app.R
import com.example.groceries_app.ui.screens.*

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
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
                onPhoneSignIn = {
                    navController.navigate(Screen.MobileNumber.route)
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
                onSignUpClick = { username, email, password ->
                    // After successful sign up, go to location selection
                    navController.navigate(Screen.LocationSelection.route)
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

        // Mobile Number Screen
        composable(route = Screen.MobileNumber.route) {
            MobileNumberScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNextClick = { phoneNumber ->
                    navController.navigate(Screen.VerificationCode.route)
                }
            )
        }

        // Verification Code Screen
        composable(route = Screen.VerificationCode.route) {
            VerificationCodeScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onVerifyClick = { code ->
                    navController.navigate(Screen.LocationSelection.route)
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
            CartScreen(
                modifier = modifier,
                onOrderPlaced = {
                    // Navigate to order success screen
                    navController.navigate(Screen.OrderAccepted.route) {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }

        // Favourite Screen
        composable(route = "favourite") {
            FavouriteScreen(
                modifier = modifier,
                onProductClick = { product ->
                    // Navigate to product detail
                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                },
                onAddAllToCart = {
                    // TODO: Add all favourite items to cart
                    // Then navigate to cart
                    navController.navigate("cart")
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
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0

            // Sample product data - in real app, you'd fetch this based on productId
            val product = ProductDetail(
                id = productId,
                name = "Natural Red Apple",
                weight = "1kg, Price",
                price = 4.99,
                imageRes = R.drawable.img_4,
                description = "Apples Are Nutritious. Apples May Be Good For Weight Loss. Apples May Be Good For Your Heart. As Part Of A Healtful And Varied Diet."
            )

            ProductDetailScreen(
                product = product,
                onBackClick = {
                    navController.navigateUp()
                },
                onAddToBasket = { quantity ->
                    // Handle add to basket
                }
            )
        }

        // Category Products Screen
        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.IntType
                },
                navArgument("categoryName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
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
                onAddToCart = { product ->
                    // Handle add to cart
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
                    // Handle add to cart
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
                    val categoryIds = selectedCategories.joinToString(",") { it.id.toString() }
                    val brandIds = selectedBrands.joinToString(",") { it.id.toString() }

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedCategories", categoryIds)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedBrands", brandIds)

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
    }
}

