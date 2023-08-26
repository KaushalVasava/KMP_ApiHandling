package com.kaushalvasava.apps.kmp_api_handling.model

sealed class ImageUiState {
    class Success(val images: List<ImageModel>) : ImageUiState()
    class Error(val error: String) : ImageUiState()
    object Loading : ImageUiState()
}