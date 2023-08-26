package com.kaushalvasava.apps.kmp_api_handling.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kaushalvasava.apps.kmp_api_handling.getUrlDecodedString
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.navigation.Navigator


@Composable
fun ImageFullScreen(imageUrl: String, navigator: Navigator) {
    val url = getUrlDecodedString(imageUrl)
    Box(Modifier.fillMaxSize()) {
        KamelImage(
            asyncPainterResource(url),
            null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            onFailure = {
                Text("Error in image loading")
            }
        )
        IconButton(
            onClick = {
                navigator.popBackStack()
            },
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }
    }
}