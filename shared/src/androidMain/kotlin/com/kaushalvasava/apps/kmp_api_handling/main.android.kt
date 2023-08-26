package com.kaushalvasava.apps.kmp_api_handling

import androidx.compose.runtime.Composable
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

actual fun getPlatformName(): String = "Android"

@Composable
fun MainView() = App()

actual fun getUrlEncodedString(input: String): String {
    return URLEncoder.encode(input, StandardCharsets.UTF_8.toString())
}

actual fun getUrlDecodedString(input: String): String {
    return URLDecoder.decode(input, StandardCharsets.UTF_8.toString())
}
