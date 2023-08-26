package com.kaushalvasava.apps.kmp_api_handling.viewmodel

import com.kaushalvasava.apps.kmp_api_handling.model.ImageModel
import com.kaushalvasava.apps.kmp_api_handling.model.ImageUiState
import com.kaushalvasava.apps.kmp_api_handling.util.AppConstants
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainViewModel : ViewModel() {
    private val _uiImageState =
        MutableStateFlow<ImageUiState>(ImageUiState.Loading)
    val uiState = _uiImageState.asStateFlow()
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // used for ignore partially decode a JSON response
            })
        }
    }

    init {
        updateImages(1)
    }

    fun updateImages(page: Int) {
        _uiImageState.value = ImageUiState.Loading
        viewModelScope.launch {
            try {
                val images = getImages(page)
                _uiImageState.value = ImageUiState.Success(images)
            } catch (e: Exception) {
                print(e.message.toString())
                _uiImageState.value = ImageUiState.Error(e.message.toString())
            }
        }
    }

    private suspend fun getImages(page: Int): List<ImageModel> {
        return httpClient
            .get("${AppConstants.BASE_URL}/photos") {
                // to use single header and headers{ } to use multiple headers
                header(
                    HttpHeaders.Authorization,
                    "Client-ID ${AppConstants.API_KEY}"
                )
                url { // query params
                    parameters.append("page", page.toString())
                    parameters.append("per_page", AppConstants.PAGE_SIZE.toString())
                }
            }.body()
    }

    override fun onCleared() {
        httpClient.close()
    }
}
