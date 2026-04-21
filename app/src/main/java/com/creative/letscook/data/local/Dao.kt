package com.creative.letscook.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.creative.letscook.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foodItems: List<FoodItemEntity>)

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :name || '%'")
    fun searchFoodItemsByName(name: String): Flow<List<FoodItemEntity>>
}

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE favorite = false ORDER BY id DESC")
    fun getRecentRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE favorite = true ORDER BY id DESC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("DELETE FROM recipes where favorite = false")
    suspend fun removeAllRecentRecipes()

    @Query("DELETE FROM recipes WHERE favorite = true")
    suspend fun removeAllFavoriteRecipes()

    @Query("update recipes set favorite = true where id = :recipeId")
    suspend fun markAsFavorite(recipeId: Int)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>) : List<Long>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeByIdFlow(id: Int): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Query("update recipes set favorite = false where id = :id")
    suspend fun markAsNotFavorite(id: Int)
}