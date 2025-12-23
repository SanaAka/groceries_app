package com.example.groceries_app.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Login : Screen("login")
    data object LocationSelection : Screen("location_selection")
    data object Home : Screen("home")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object CategoryProducts : Screen("category_products/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: String, categoryName: String) = "category_products/$categoryId/$categoryName"
    }
    data object SearchResults : Screen("search_results?query={query}") {
        fun createRoute(query: String = "") = "search_results?query=$query"
    }
    data object Filter : Screen("filter")
    data object OrderAccepted : Screen("order_accepted")
    data object OrderFailed : Screen("order_failed")
    data object Payment : Screen("payment/{amount}") {
        fun createRoute(amount: Double) = "payment/$amount"
    }
    data object Orders : Screen("orders")
}

