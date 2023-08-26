package com.kaushalvasava.apps.kmp_api_handling

import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }


actual fun getUrlEncodedString(input: String): String {
    return encodeUrl(input)
}

actual fun getUrlDecodedString(input: String): String {
    return decodeUrl(input)
}


fun encodeUrl(url: String): String {
    val reservedCharacters = listOf("://", '/', '?', '#', '[', ']', '@', '!', '$', '&', "\'", '(', ')', '*', '+', ',', ';', '=')
    val encoded = StringBuilder()

    for (char in url) {
        if (char.isLetterOrDigit() || reservedCharacters.contains(char)) {
            encoded.append(char)
        } else {
            val bytes = char.toString().toByteArray(Charsets.UTF_8)
            for (byte in bytes) {
                encoded.append("%${String.format("%02X", byte)}")
            }
        }
    }

    return encoded.toString().replace("/", "%")
}

fun decodeUrl(encodedUrl: String): String {
    val decoded = StringBuilder()
    var i = 0

    while (i < encodedUrl.length) {
        val currentChar = encodedUrl[i]

        if (currentChar == '%') {
            // Check if there are enough characters remaining for a valid escape sequence
            if (i + 2 < encodedUrl.length) {
                try {
                    // Get the next two characters and convert them to a byte
                    val hexValue = encodedUrl.substring(i + 1, i + 3)
                    val decodedByte = hexValue.toInt(16).toByte()
                    decoded.append(decodedByte.toChar())
                    i += 2 // Skip the next two characters
                } catch (e: NumberFormatException) {
                    // Invalid escape sequence, treat '%' as a literal character
                    decoded.append('%')
                }
            } else {
                // '%' at the end of the string, treat as a literal character
                decoded.append('%')
            }
        } else {
            // Normal character, append it as is
            decoded.append(currentChar)
        }

        i++
    }

    return decoded.toString().replace("%", "/")
}