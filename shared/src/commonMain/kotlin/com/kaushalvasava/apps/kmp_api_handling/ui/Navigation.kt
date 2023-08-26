package com.kaushalvasava.apps.kmp_api_handling.ui


enum class Screen {
    HOME,
    VIEW_IMAGE,
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object ViewImage : NavigationItem(Screen.VIEW_IMAGE.name)
}