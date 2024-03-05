package com.techafresh.jetreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techafresh.jetreader.screens.*
import com.techafresh.jetreader.screens.details.DetailScreen
import com.techafresh.jetreader.screens.home.HomeScreen
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.login.LoginScreen
import com.techafresh.jetreader.screens.search.SearchScreen
import com.techafresh.jetreader.screens.search.SearchViewModel
import com.techafresh.jetreader.screens.stats.StatsScreen
import com.techafresh.jetreader.screens.update.UpdateScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.name,
        ) {

        composable(AppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(AppScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(navController = navController, searchViewModel)
        }

        val route = AppScreens.DetailScreen.name
        composable("$route/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })
        ) {
            it.arguments?.getString("bookId").let {
                DetailScreen(navController = navController, bookId = it.toString())
            }

        }

        composable(AppScreens.HomeScreen.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel)
        }

        composable(AppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(AppScreens.StatsScreen.name) {
            StatsScreen(navController = navController)
        }

        val updateRoute = AppScreens.UpdateScreen.name
        composable("$updateRoute/{bookItemId}", arguments = listOf(navArgument("bookItemId") {
            type = NavType.StringType
        })) {
            it.arguments?.getString("bookItemId").let {
                val viewModel = hiltViewModel<HomeScreenViewModel>()
                UpdateScreen(navController = navController, bookItemId = it.toString(), viewModel)
            }
        }
    }
}