package com.creative.letscook.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.creative.letscook.data.local.FoodItemDao
import com.creative.letscook.data.local.FoodItemEntity
import com.creative.letscook.data.local.LetsCookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<FoodItemDao>
    ): LetsCookDatabase {
        return databaseBuilder(
            context,
            LetsCookDatabase::class.java,
            "lets_cook_db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val jsonString = context.assets.open("food_list.json").bufferedReader().use { it.readText() }
                        val foodItems = Json.decodeFromString<List<FoodItemEntity>>(jsonString)
                        provider.get().insertAll(foodItems)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }).build()
    }

    @Provides
    fun provideRecipeDao(database: LetsCookDatabase) = database.recipeDao()

    @Provides
    fun provideFoodItemDao(database: LetsCookDatabase) = database.foodItemDao()

}
