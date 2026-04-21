package com.creative.letscook.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int? = null,
    val name: String,
    val servings: Int,
    val prepTime: Int,
    val cookTime: Int,
    val difficulty: String,
    val countryName: String? = null,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val favorite: Boolean = false,
)
@Serializable
data class Ingredient(
    val name: String,
    val amount: String
)