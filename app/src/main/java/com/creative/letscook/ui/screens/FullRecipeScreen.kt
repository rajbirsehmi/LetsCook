package com.creative.letscook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.letscook.domain.model.Ingredient
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
            onDeleteClick = {
                viewModel.deleteRecipe(it)
                onNavigationBack()
            },
            onNavigationBack = onNavigationBack
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun FullRecipeContent(
    recipe: Recipe,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                onDeleteClick = onDeleteClick,
                onNavigationBack = onNavigationBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Overview Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    recipe.countryName?.let { country ->
                        Text(
                            text = country.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RecipeInfoItem(
                                Icons.Default.AccessTime,
                                "${recipe.prepTime + recipe.cookTime} min",
                                "Time"
                            )
                            Box(
                                modifier = Modifier
                                    .size(1.dp, 40.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                            RecipeInfoItem(
                                Icons.Default.Restaurant,
                                recipe.difficulty,
                                "Difficulty"
                            )
                            Box(
                                modifier = Modifier
                                    .size(1.dp, 40.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                            RecipeInfoItem(
                                Icons.Default.Group,
                                "${recipe.servings}",
                                "Servings"
                            )
                        }
                    }
                }
            }

            // 2. Ingredients Section
            item {
                SectionHeader("Ingredients", "${recipe.ingredients.size} items")
            }

            itemsIndexed(recipe.ingredients) { index, ingredient ->
                IngredientItem(ingredient)
                if (index < recipe.ingredients.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }

            // 3. Instructions Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("Instructions", "${recipe.instructions.size} steps")
            }

            itemsIndexed(recipe.instructions) { index, instruction ->
                InstructionStepItem(index + 1, instruction)
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun IngredientItem(ingredient: Ingredient) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(8.dp),
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {}
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = ingredient.amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun InstructionStepItem(stepNumber: Int, instruction: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Step $stepNumber",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = instruction,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RecipeInfoItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val previewIngredients = listOf(
    Ingredient("Tomato", "4"),
    Ingredient("Pasta", "200g"),
    Ingredient("Olive Oil", "2 tbsp"),
    Ingredient("Garlic", "3 cloves")
)

private val previewInstructions = listOf(
    "Boil water in a large pot and add salt.",
    "Cook the pasta according to package instructions until al dente.",
    "Meanwhile, sauté minced garlic in olive oil until fragrant.",
    "Add chopped tomatoes and cook until they soften.",
    "Toss the pasta with the tomato sauce and serve hot."
)

private val previewRecipe = Recipe(
    id = 0,
    name = "Classic Tomato Pasta",
    servings = 2,
    prepTime = 10,
    cookTime = 15,
    difficulty = "Easy",
    countryName = "Italy",
    ingredients = previewIngredients,
    instructions = previewInstructions,
    favorite = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun FullRecipeScreenPreview() {
    MaterialTheme {
        FullRecipeContent(
            recipe = previewRecipe,
            onFavoriteClick = {},
            onDeleteClick = {},
            onNavigationBack = {}
        )
    }
}
