package com.creative.letscook.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creative.letscook.domain.enums.Countries
import com.creative.letscook.domain.enums.DietaryType
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.domain.repo.KitchenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val repository: KitchenRepository
) : ViewModel() {
    var expanded by mutableStateOf(false)
    var textState by mutableStateOf("")
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    var filteredOptions by mutableStateOf(listOf<FoodItem>())

    var selectedOptions = mutableStateListOf<String>()

    var selectedCountries = mutableStateListOf<Countries>()

    var selectedDietaryType by mutableStateOf<DietaryType?>(null)

    fun onDietaryTypeSelected(dietaryType: DietaryType) {
        selectedDietaryType = if (selectedDietaryType == dietaryType) null else dietaryType
    }

    fun toggleCountrySelection(country: Countries) {
        if (selectedCountries.contains(country)) {
            selectedCountries.remove(country)
        } else {
            selectedCountries.add(country)
        }
    }

    fun generateRecipe() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.generateRecipesFromIngredients(
                    ingredients = selectedOptions,
                    countries = selectedCountries,
                    dietaryType = selectedDietaryType
                )
                if (response != null && response.recipes.isNotEmpty()) {
                    _recipes.value = response.recipes
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                selectedOptions.clear()
                selectedCountries.clear()
                selectedDietaryType = null
            }
        }
    }

    fun onTextChange(newText: String) {
        textState = newText
        if (newText.length >= 3) {
            viewModelScope.launch {
                repository.getFoodItemByName(newText).collectLatest { list ->
                    filteredOptions = list.ifEmpty {
                        listOf(FoodItem(id = 0, name = newText))
                    }
                }
            }
        } else {
            filteredOptions = emptyList()
        }
    }

    fun onOptionSelected(option: FoodItem) {
        selectedOptions.add(option.name)
        textState = ""
        expanded = false
    }
}