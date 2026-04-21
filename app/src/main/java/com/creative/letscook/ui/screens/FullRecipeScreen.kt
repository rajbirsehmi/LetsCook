package com.creative.letscook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.ui.components.TopAppBarRecipeScreen
import com.creative.letscook.ui.viewmodel.FullRecipeViewModel

@ExperimentalMaterial3Api
@Composable
fun FullRecipeScreen(
    onNavigationBack: () -> Unit,
    viewModel: FullRecipeViewModel = hiltViewModel()
) {
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()
    recipe?.let {
        FullRecipeContent(
            recipe = it,
            onFavoriteClick = { viewModel.toggleFavorite(it) },
            onNavigationBack = onNavigationBack
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun FullRecipeContent(
    recipe: Recipe,
    onFavoriteClick: () -> Unit,
    onNavigationBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarRecipeScreen(
                recipeName = recipe.name,
                isFavorite = recipe.favorite,
                scrollBehavior = scrollBehavior,
                onFavoriteClick = onFavoriteClick,
                onNavigationBack = onNavigationBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Overview Section (Time, Difficulty, Servings)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RecipeInfoItem(
                            Icons.Default.AccessTime,
                            "${recipe.prepTime + recipe.cookTime} min"
                        )
                        RecipeInfoItem(Icons.Default.Restaurant, recipe.difficulty)
                        RecipeInfoItem(Icons.Default.Group, "${recipe.servings} ser.")
                    }
                }
            }

            // 2. Ingredients Section
            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            itemsIndexed(recipe.ingredients) { index, ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "•",
                        modifier = Modifier.width(24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = ingredient.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = ingredient.amount,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // 3. Instructions Section
            item {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            itemsIndexed(recipe.instructions) { index, instruction ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Step ${index + 1}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = instruction.toString(), // Assumes instruction has a meaningful toString()
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )
                    if (index < recipe.instructions.size - 1) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun RecipeInfoItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun FullRecipeScreenPreview() {
    FullRecipeContent(
        recipe = recipe,
        onFavoriteClick = {},
        onNavigationBack = {}
    )
}

val recipe = Recipe(
    id = 0,
    name = "Recipe 1",
    servings = 2,
    prepTime = 30,
    cookTime = 60,
    difficulty = "Easy",
    ingredients = ingredients,
    instructions = instructions,
    favorite = false
)
