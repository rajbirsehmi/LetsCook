package com.creative.letscook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.letscook.domain.repo.KitchenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: KitchenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            launch {
                repository.getRecentRecipes().collect { recipes ->
                    _uiState.update { it.copy(recents = recipes, isLoading = false) }
                }
            }
            launch {
                repository.getFavoriteRecipes().collect { recipes ->
                    _uiState.update { it.copy(favorites = recipes, isLoading = false) }
                }
            }
        }
    }
}
