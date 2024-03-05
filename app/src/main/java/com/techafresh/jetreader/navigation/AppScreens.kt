package com.techafresh.jetreader.navigation

enum class AppScreens {
    SplashScreen,
    LoginScreen,
    SearchScreen,
    SignupScreen,
    UpdateScreen,
    StatsScreen,
    DetailScreen,
    HomeScreen;

    companion object {
        fun fromRoute(route: String?): AppScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            SignupScreen.name -> SignupScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}