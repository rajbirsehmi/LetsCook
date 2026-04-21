package com.creative.letscook.util

import com.creative.letscook.data.local.FoodItemEntity
import com.creative.letscook.data.local.RecipeEntity
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.data.local.Ingredient as IngredientEntity
import com.creative.letscook.domain.model.Ingredient as IngredientDomain

fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    name = name,
    servings = servings,
    prepTime = prepTime,
    cookTime = cookTime,
    difficulty = difficulty,
    countryName = countryName,
    ingredients = ingredients.map { it.toDomain() },
    instructions = instructions,
    favorite = favorite
)

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    servings = servings,
    prepTime = prepTime,
    cookTime = cookTime,
    difficulty = difficulty,
    countryName = countryName,
    ingredients = ingredients.map { it.toEntity() },
    instructions = instructions,
    favorite = favorite
)

fun IngredientEntity.toDomain(): IngredientDomain = IngredientDomain(
    name = name,
    amount = amount
)

fun IngredientDomain.toEntity(): IngredientEntity = IngredientEntity(
    name = name,
    amount = amount
)

fun FoodItemEntity.toDomain(): FoodItem = FoodItem(
    id = id?.toInt() ?: 0,
    name = name
)

fun FoodItem.toEntity(): FoodItemEntity = FoodItemEntity(
    id = id.toLong(),
    name = name
)

