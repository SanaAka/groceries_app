package com.example.groceries_app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.groceries_app.ui.components.BottomNavigationBar

@Composable
fun AppScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens that should show bottom navigation
    val screensWithBottomNav = listOf(
        Screen.Home.route,
        "explore",
        "cart",
        "favourite",
        "account"
    )

    val shouldShowBottomBar = currentRoute in screensWithBottomNav

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute ?: Screen.Home.route,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination to avoid building up a large stack
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    cartItemCount = 0 // TODO: Connect to cart state
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

