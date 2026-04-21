package com.creative.letscook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.creative.letscook.domain.model.Ingredient
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.ui.components.RecipeTemplate
import com.creative.letscook.ui.components.TopAppBarRecipeListScreen
import com.creative.letscook.ui.navigation.Routes

@Composable
fun RecipeListScreen(
    recipes: List<Recipe>,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBarRecipeListScreen()
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "No recipes found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(recipes, key = { it.id ?: it.name }) { recipe ->
                    RecipeTemplate(
                        recipe = recipe,
                        onClick = {
                            navController.navigate("${Routes.FULL_RECIPE}/${recipe.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun RecipeListScreenPreview() {
    val controller = rememberNavController()
    RecipeListScreen(recipes, controller)
}


val ingredients = listOf(
    Ingredient(
        name = "Tomato",
        amount = "4"
    ),
    Ingredient(
        name = "Peas",
        amount = "100g"
    ),
    Ingredient(
        name = "Cabbage",
        amount = "100g"
    ),
    Ingredient(
        name = "Chillies",
        amount = "100g"
    ),
    Ingredient(
        name = "Pasta",
        amount = "100g"
    ),
    Ingredient(
        name = "Wheat",
        amount = "100g"
    ),
    Ingredient(
        name = "Water",
        amount = "100g"
    )
)

val instructions = listOf(
    "Step 1",
    "Step 2",
    "Step 3",
    "Step 4",
    "Step 5",
    "Step 6",
    "Step 7",
    "Step 8",
)

val recipes = listOf(
    Recipe(
        name = "Recipe 1",
        servings = 2,
        prepTime = 30,
        cookTime = 60,
        difficulty = "Easy",
        ingredients = ingredients,
        instructions = instructions,
        id = 0,
        favorite = false
    ),
    Recipe(
        name = "Recipe 2",
        servings = 3,
        prepTime = 40,
        cookTime = 90,
        difficulty = "Easy",
        ingredients = ingredients,
        instructions = instructions,
        id = 1,
        favorite = false
    ),
    Recipe(
        name = "Recipe 3",
        servings = 5,
        prepTime = 20,
        cookTime = 50,
        difficulty = "Easy",
        ingredients = ingredients,
        instructions = instructions,
        id = 2,
        favorite = false
    )
)
