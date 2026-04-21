package com.creative.letscook.util

import androidx.room.TypeConverter
import com.creative.letscook.data.local.Ingredient
import kotlinx.serialization.json.Json

class RecipeConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredients(value: List<Ingredient>) = json.encodeToString(value)

    @TypeConverter
    fun toIngredients(value: String) = json.decodeFromString<List<Ingredient>>(value)

    @TypeConverter
    fun fromInstructions(value: List<String>) = json.encodeToString(value)

    @TypeConverter
    fun toInstructions(value: String) = json.decodeFromString<List<String>>(value)
}