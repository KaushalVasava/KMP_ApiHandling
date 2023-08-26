package com.kaushalvasava.apps.kmp_api_handling.ui

import androidx.compose.runtime.Composable
import com.kaushalvasava.apps.kmp_api_handling.viewmodel.MainViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel,
    navigator: Navigator,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        navigator = navigator,
        navTransition = NavTransition(),
        initialRoute = startDestination
    ) {
        scene(
            route = NavigationItem.Home.route,
            navTransition = NavTransition()
        ) {
            HomeScreen(mainViewModel, navigator)
        }
        scene(route = "${NavigationItem.ViewImage.route}/{url}") { backStackEntry ->
            val imageUrl = backStackEntry.path<String>("url")
            imageUrl?.let {
                ImageFullScreen(it, navigator)
            }
        }
    }
}