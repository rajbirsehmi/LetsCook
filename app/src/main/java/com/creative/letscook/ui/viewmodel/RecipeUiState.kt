package com.creative.letscook.ui.viewmodel

import com.creative.letscook.domain.model.Recipe

data class RecipeUiState(
    val recents: List<Recipe> = emptyList(),
    val favorites: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: String? = null
)
