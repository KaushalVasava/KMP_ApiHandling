package com.kaushalvasava.apps.kmp_api_handling.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kaushalvasava.apps.kmp_api_handling.getUrlEncodedString
import com.kaushalvasava.apps.kmp_api_handling.model.ImageModel
import com.kaushalvasava.apps.kmp_api_handling.model.ImageUiState
import com.kaushalvasava.apps.kmp_api_handling.viewmodel.MainViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun HomeScreen(viewModel: MainViewModel, navigator: Navigator) {
    val uiState by viewModel.uiState.collectAsState()
    var page by rememberSaveable {
        mutableStateOf(1)
    }

    when (val state = uiState) {
        is ImageUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error)
            }
        }

        ImageUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ImageUiState.Success -> {
            AnimatedVisibility(state.images.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                    content = {
                        items(state.images) {
                            PhotoImage(it) {
                                val encodedUrl = getUrlEncodedString(it.urls.regular)
                                navigator.navigate("${NavigationItem.ViewImage.route}/$encodedUrl")
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                IconButton(onClick = {
                                    viewModel.updateImages(page - 1)
                                    if (page < 1)
                                        page = 1
                                    else
                                        page -= 1
                                }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                                }
                                IconButton(onClick = {
                                    viewModel.updateImages(page + 1)
                                    page += 1
                                }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoImage(image: ImageModel, onClick: () -> Unit) {
    KamelImage(
        asyncPainterResource(image.urls.regular),
        null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).clickable {
            onClick()
        }
    )
}