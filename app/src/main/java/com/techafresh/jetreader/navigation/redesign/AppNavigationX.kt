package com.techafresh.jetreader.navigation.redesign

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techafresh.jetreader.screens.home.HomeScreenViewModel
import com.techafresh.jetreader.screens.redesign.DetailScreenX
import com.techafresh.jetreader.screens.redesign.HomeScreenX
import com.techafresh.jetreader.screens.redesign.LoginScreenX
import com.techafresh.jetreader.screens.redesign.ReviewScreen
import com.techafresh.jetreader.screens.redesign.SearchScreenX
import com.techafresh.jetreader.screens.redesign.SignUpScreenX
import com.techafresh.jetreader.screens.redesign.SplashScreenX
import com.techafresh.jetreader.screens.redesign.CollectionsScreenX
import com.techafresh.jetreader.screens.redesign.ProfileScreenX

@Composable
fun AppNavigationX(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreensX.SplashScreenX.name
    ){

        composable(AppScreensX.HomeScreenX.name){
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreenX(navController = navController, viewModel)
        }

        composable(AppScreensX.CollectionsScreenX.name) {
            CollectionsScreenX(navController = navController)
        }

        composable(AppScreensX.SplashScreenX.name) {
            SplashScreenX(navController = navController)
        }

        val route = AppScreensX.DetailsScreenX.name
        composable("$route/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            }) ){
            it.arguments?.getString("bookId").let {
                DetailScreenX(navController = navController,
                    bookId = it.toString()
                )
            }
        }

        composable(AppScreensX.LoginScreenX.name) {
            LoginScreenX(navController = navController)
        }

        composable(AppScreensX.SearchScreenX.name) {
            SearchScreenX(navController = navController)
        }

        val reviewRoute = AppScreensX.ReviewScreen.name
        composable("$reviewRoute/{book}", arguments = listOf(navArgument("book") {
            type = NavType.StringType
        })) {
            it.arguments?.getString("book").let {
                val viewModel = hiltViewModel<HomeScreenViewModel>()
                ReviewScreen(navController = navController, book = it.toString(), viewModel)
            }
        }

        composable(AppScreensX.SignupScreenX.name) {
            SignUpScreenX(navController = navController)
        }

        composable(AppScreensX.ProfileScreenX.name) {
            ProfileScreenX(navController = navController)
        }
    }
}