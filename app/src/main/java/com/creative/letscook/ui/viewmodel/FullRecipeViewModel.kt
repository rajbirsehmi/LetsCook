package com.creative.letscook.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.domain.repo.KitchenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullRecipeViewModel @Inject constructor(
    private val repository: KitchenRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe = _recipe.asStateFlow()

    init {
        val recipeId: Int? = savedStateHandle["recipeId"]
        recipeId?.let { id ->
            viewModelScope.launch {
                repository.getRecipeByIdFlow(id).collect {
                    _recipe.value = it
                }
            }
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.favorite) {
                repository.markAsNotFavorite(recipe.id ?: return@launch)
            } else {
                repository.markAsFavorite(recipe.id ?: return@launch)
            }
        }
    }
}
