package com.creative.letscook.data.repo

import com.creative.letscook.BuildConfig
import com.creative.letscook.data.local.FoodItemDao
import com.creative.letscook.data.local.RecipeDao
import com.creative.letscook.data.local.RecipeResponse
import com.creative.letscook.data.local.RecipeResponseEntity
import com.creative.letscook.data.remote.GroqApiService
import com.creative.letscook.data.remote.GroqMessage
import com.creative.letscook.data.remote.GroqRequest
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

    override suspend fun generateRecipesFromIngredients(ingredients: List<String>): RecipeResponse? {
        val apiKey = "Bearer ${BuildConfig.GROQ_API_KEY}"

        val systemPrompt = """
            Role: You are a professional chef and data analyst.
            Task: Generate a list of recipes based on the provided ingredients.
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
        val userPrompt = "Available ingredients: ${ingredients.joinToString(",")}"
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

//            val jsonContent = "{\n" +
//                    "  \"recipes\" : [ {\n" +
//                    "    \"recipe_name\" : \"Paneer Matar Masala\",\n" +
//                    "    \"servings\" : 4,\n" +
//                    "    \"prep_time\" : 30,\n" +
//                    "    \"cook_time\" : 25,\n" +
//                    "    \"difficulty\" : \"Medium\",\n" +
//                    "    \"ingredients\" : [ {\n" +
//                    "      \"name\" : \"Paneer\",\n" +
//                    "      \"amount\" : \"250 grams\"\n" +
//                    "    }, {\n" +
//                    "      \"name\" : \"Matar\",\n" +
//                    "      \"amount\" : \"1 cup\"\n" +
//                    "    } ],\n" +
//                    "    \"instructions\" : [ \"Heat oil in a pan and sauté onions, ginger, and garlic.\", \"Add the Matar and cook until it's tender. Then add tomatoes and cook for another 5 minutes.\", \"Add Paneer, mix well, and cook for 5 minutes. Season with salt and spices.\" ]\n" +
//                    "  }, {\n" +
//                    "    \"recipe_name\" : \"Aloo Matar Sabzi\",\n" +
//                    "    \"servings\" : 4,\n" +
//                    "    \"prep_time\" : 20,\n" +
//                    "    \"cook_time\" : 20,\n" +
//                    "    \"difficulty\" : \"Easy\",\n" +
//                    "    \"ingredients\" : [ {\n" +
//                    "      \"name\" : \"Aloo\",\n" +
//                    "      \"amount\" : \"2 medium\"\n" +
//                    "    }, {\n" +
//                    "      \"name\" : \"Matar\",\n" +
//                    "      \"amount\" : \"1 cup\"\n" +
//                    "    } ],\n" +
//                    "    \"instructions\" : [ \"Boil the Aloo until it's tender, then peel and chop.\", \"Heat oil in a pan and sauté onions, ginger, and garlic.\", \"Add the Matar and cook until it's tender. Then add Aloo and cook for another 5 minutes.\" ]\n" +
//                    "  }, {\n" +
//                    "    \"recipe_name\" : \"Paneer Aloo Tikka\",\n" +
//                    "    \"servings\" : 4,\n" +
//                    "    \"prep_time\" : 25,\n" +
//                    "    \"cook_time\" : 20,\n" +
//                    "    \"difficulty\" : \"Medium\",\n" +
//                    "    \"ingredients\" : [ {\n" +
//                    "      \"name\" : \"Paneer\",\n" +
//                    "      \"amount\" : \"250 grams\"\n" +
//                    "    }, {\n" +
//                    "      \"name\" : \"Aloo\",\n" +
//                    "      \"amount\" : \"2 medium\"\n" +
//                    "    } ],\n" +
//                    "    \"instructions\" : [ \"Marinate Paneer and Aloo in spices and yogurt for 30 minutes.\", \"Grill or bake the marinated Paneer and Aloo until it's cooked through.\", \"Serve with a side of salad or raita.\" ]\n" +
//                    "  } ]\n" +
//                    "}"

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