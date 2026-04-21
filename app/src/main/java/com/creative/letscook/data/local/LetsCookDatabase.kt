package com.creative.letscook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.creative.letscook.util.RecipeConverters

@Database(
    entities = [FoodItemEntity::class, RecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipeConverters::class)
abstract class LetsCookDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun recipeDao(): RecipeDao
}