package com.kaushalvasava.apps.kmp_api_handling

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.kaushalvasava.apps.kmp_api_handling.ui.AppNavHost
import com.kaushalvasava.apps.kmp_api_handling.viewmodel.MainViewModel
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun App() {
    MaterialTheme {
        val navigator = rememberNavigator()
        val mainViewModel = MainViewModel()
        AppNavHost(mainViewModel, navigator)
    }
}

// these must have to implement in you android, ios, web, desktop files with
// actual fun name to use code of particular platform
expect fun getUrlEncodedString(input: String): String
expect fun getUrlDecodedString(input: String): String
expect fun getPlatformName(): String

/**
 * DeviceTypes:
 * 0 -> Phone
 * 1 -> Tablet
 * 2 -> Desktop
 * */
expect fun getDeviceType(): Int