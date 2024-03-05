package com.techafresh.jetreader.navigation.redesign

import com.techafresh.jetreader.navigation.AppScreens

enum class AppScreensX {
    HomeScreenX,
    SplashScreenX,
    DetailsScreenX,
    LoginScreenX,
    SearchScreenX,
    SignupScreenX,
    CollectionsScreenX,
    ProfileScreenX,
    ReviewScreen,
    ;

    companion object {
        fun fromRoute(route: String?): AppScreensX = when (route?.substringBefore("/")){
            HomeScreenX.name -> HomeScreenX
            LoginScreenX.name -> LoginScreenX
            SignupScreenX.name ->SignupScreenX
            HomeScreenX.name -> HomeScreenX
            SearchScreenX.name ->SearchScreenX
            DetailsScreenX.name ->DetailsScreenX
            ProfileScreenX.name -> ProfileScreenX
            CollectionsScreenX.name -> CollectionsScreenX
            SplashScreenX.name -> SplashScreenX
            ReviewScreen.name -> ReviewScreen
            null -> HomeScreenX
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}