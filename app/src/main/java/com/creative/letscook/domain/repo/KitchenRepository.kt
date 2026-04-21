package com.creative.letscook.domain.repo

import com.creative.letscook.data.local.FoodItemEntity
import com.creative.letscook.data.local.RecipeResponse
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface KitchenRepository {
    fun getRecentRecipes(): Flow<List<Recipe>>
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    suspend fun removeAllRecentRecipes()
    suspend fun removeAllFavoriteRecipes()

    suspend fun markAsFavorite(id: Int)

    suspend fun markAsNotFavorite(id: Int)

    suspend fun getRecipeById(id: Int): Recipe?
    fun getRecipeByIdFlow(id: Int): Flow<Recipe?>
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun saveRecipes(recipes: List<Recipe>)

    fun getFoodItemByName(name: String): Flow<List<FoodItem>>

    // Groq Specific
    suspend fun generateRecipesFromIngredients(ingredients: List<String>): RecipeResponse?
}

