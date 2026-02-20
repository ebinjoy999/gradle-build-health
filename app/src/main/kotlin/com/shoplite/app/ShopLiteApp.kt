package com.shoplite.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoplite.feature.home.HomeScreen
import com.shoplite.feature.search.SearchScreen

/**
 * Navigation routes for the app.
 */
object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
}

/**
 * Main composable for the ShopLite app.
 * 
 * Sets up navigation between feature screens.
 */
@Composable
fun ShopLiteApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToSearch = {
                        navController.navigate(Routes.SEARCH)
                    }
                )
            }
            
            composable(Routes.SEARCH) {
                SearchScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
