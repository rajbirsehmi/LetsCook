package com.creative.letscook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.util.foodTableName
import com.creative.letscook.util.recipesTableName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = foodTableName)
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String
)

@Serializable
@Entity(tableName = recipesTableName)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerialName("recipe_name") val name: String,
    @SerialName("servings") val servings: Int,
    @SerialName("prep_time") val prepTime: Int,
    @SerialName("cook_time") val cookTime: Int,
    @SerialName("difficulty") val difficulty: String,
    @SerialName("ingredients") val ingredients: List<Ingredient>,
    @SerialName("instructions") val instructions: List<String>,
    val favorite: Boolean = false,
)
@Serializable
data class Ingredient(
    val name: String,
    val amount: String
)

@Serializable
data class RecipeResponseEntity(
    val recipes: List<RecipeEntity>
)

@Serializable
data class RecipeResponse(
    val recipes: List<Recipe>
)