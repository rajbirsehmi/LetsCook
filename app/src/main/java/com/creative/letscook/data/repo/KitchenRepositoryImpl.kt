package com.creative.letscook.data.repo

import com.creative.letscook.BuildConfig
import com.creative.letscook.data.local.FoodItemDao
import com.creative.letscook.data.local.RecipeDao
import com.creative.letscook.data.local.RecipeResponse
import com.creative.letscook.data.local.RecipeResponseEntity
import com.creative.letscook.data.remote.GroqApiService
import com.creative.letscook.data.remote.GroqMessage
import com.creative.letscook.data.remote.GroqRequest
import com.creative.letscook.domain.enums.Countries
import com.creative.letscook.domain.enums.DietaryType
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.domain.repo.KitchenRepository
import com.creative.letscook.util.toDomain
import com.creative.letscook.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class KitchenRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val foodItemDao: FoodItemDao,
    private val groqApiService: GroqApiService,
    private val json: Json
) : KitchenRepository {
    override fun getRecentRecipes(): Flow<List<Recipe>> {
        return recipeDao.getRecentRecipes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun removeAllRecentRecipes() {
        recipeDao.removeAllRecentRecipes()
    }

    override suspend fun removeAllFavoriteRecipes() {
        recipeDao.removeAllFavoriteRecipes()
    }

    override suspend fun markAsFavorite(id: Int) {
        recipeDao.markAsFavorite(id)
    }

    override suspend fun markAsNotFavorite(id: Int) {
        recipeDao.markAsNotFavorite(id)
    }

    override suspend fun getRecipeById(id: Int): Recipe? =
        recipeDao.getRecipeById(id)?.toDomain()

    override fun getRecipeByIdFlow(id: Int): Flow<Recipe?> {
        return recipeDao.getRecipeByIdFlow(id).map { it?.toDomain() }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }

    // Food Items

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        recipes.forEach {
            recipeDao.insertRecipe(it.toEntity())
        }
    }

    override fun getFoodItemByName(name: String): Flow<List<FoodItem>> =
        foodItemDao.searchFoodItemsByName(name).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun generateRecipesFromIngredients(
        ingredients: List<String>,
        countries: List<Countries>,
        dietaryType: DietaryType?
    ): RecipeResponse? {
        val apiKey = "Bearer ${BuildConfig.GROQ_API_KEY}"

        val systemPrompt = """
            Role: You are a professional chef and data analyst.
            Task: Generate a list of 5+ recipes based on the provided ingredients.
            Rule: List all the supporting ingredients with their units. Also add other ingredients or seasoning if needed
            Rule: Be detailed with the instructions. You can create as manny instructions as needed.
            Output Format: Your response must be valid JSON only. Do not include any introductory text, markdown code blocks (like ```json), or concluding remarks.
            
            JSON Schema Requirements:
            
            recipes: A top-level array containing recipe objects.
            recipe_name: (String)
            servings: (Integer)
            prep_time: (Integer, representing minutes)
            cook_time: (Integer, representing minutes)
            difficulty: (String: "Easy", "Medium", or "Hard")
            country_name: (String, the cuisine type of the recipe)
            ingredients: An array of objects with keys "name" (String) and "amount" (String).
            instructions: An array of strings, where each string represents a single step.
            
            Example Structure to Follow:
            {
            "recipes": [
            {
            "recipe_name": "Name",
            "servings": 4,
            "prep_time": 15,
            "cook_time": 20,
            "difficulty": "Easy",
            "country_name": "Country",
            "ingredients": [
            { "name": "Item", "amount": "Quantity" }
            ],
            "instructions": [
            "Step 1",
            "Step 2"
            ]
            }
            ]
            }
        """.trimIndent()

        val countryPrompt = if (countries.isNotEmpty()) {
            " Preferred cuisines: ${countries.joinToString(", ") { it.displayName }}."
        } else ""

        val dietaryPrompt = dietaryType?.let { " Dietary restriction: ${it.displayName}." } ?: ""

        val userPrompt = "Available ingredients: ${ingredients.joinToString(",")}.$countryPrompt$dietaryPrompt"
        val request = GroqRequest(
            messages = listOf(
                GroqMessage("system", systemPrompt),
                GroqMessage("user", userPrompt)
            )
        )



        return try {
            val response = groqApiService.getCompletion(apiKey, request)
            val jsonContent = response.choices.firstOrNull()?.message?.content
                ?: return null
            val recipeResponseEntity = json.decodeFromString<RecipeResponseEntity>(jsonContent)
            val recipeEntities = recipeResponseEntity.recipes
            val newIds = recipeDao.insertRecipes(recipeEntities)
            val recipes = recipeEntities.mapIndexed { index, entity ->
                entity.copy(id = newIds[index].toInt()).toDomain()
            }
            RecipeResponse(recipes = recipes)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}