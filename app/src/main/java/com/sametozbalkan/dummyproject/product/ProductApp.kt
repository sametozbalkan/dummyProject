package com.sametozbalkan.dummyproject.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ProductApp() {
    val navController = rememberNavController()
    val viewModel = remember { ProductViewModel() }

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ProductListScreen(viewModel, navController)
        }
        composable("details/{id}") { backStackEntry ->
            ProductDetailScreen(
                navBackStackEntry = backStackEntry,
                navController = navController
            )
        }
    }
}