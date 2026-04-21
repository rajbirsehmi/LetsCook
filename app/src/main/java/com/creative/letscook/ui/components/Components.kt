package com.creative.letscook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.creative.letscook.R
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Ingredient
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.util.testTagTopAppBar

// All TopAppBarHome Here

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome() {
    CenterAlignedTopAppBar(
        modifier = Modifier.testTag(testTagTopAppBar),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
@Preview
fun TopAppBarHomePreview() {
    TopAppBarHome()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPantryScreen(
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.testTag(""),
        title = {
            Text(stringResource(R.string.text_whats_in_your_pantry))
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
@Preview
fun TopAppBarPantryScreenPreview() {
    TopAppBarPantryScreen(
        {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarRecipeListScreen() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Chef's Recipes",
            )
        },
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
    )
}

@Composable
@Preview
fun TopAppBarRecipeListScreenPreview() {
    TopAppBarRecipeListScreen()
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarRecipeScreen(
    recipeName: String,
    isFavorite: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onFavoriteClick: () -> Unit,
    onNavigationBack: () -> Unit
) {
    LargeTopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = recipeName,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopAppBarRecipeScreenPreview() {
    TopAppBarRecipeScreen(
        recipeName = "Pasta",
        isFavorite = false,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        onFavoriteClick = {},
        onNavigationBack = {}
    )
}



// FloatingActionButton

@Composable
fun FloatingActionButtonCookingArea(
    onClick: () -> Unit = {},
) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(16.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        icon = {
            Icon(Icons.Default.RestaurantMenu, contentDescription = null)
        },
        text = {
            Text(
                text = "What's in my pantry?",
                style = MaterialTheme.typography.labelLarge
            )
        }
    )
}

@Composable
@Preview
fun FloatingActionButtonCookingAreaPreview() {
    FloatingActionButtonCookingArea()
}

// BottomNavigationBar

@Composable
fun RowScope.BottomNavBarItem(
    navController: NavHostController,
    currentDestination: NavDestination?,
    routesString: String,
    type: String,
    icon: ImageVector
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == routesString } == true,
        onClick = {
            navController.navigate(routesString) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = type
            )
        },
        label = {
            Text(text = type)
        }
    )
}

// Autocomplete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteIngredientField(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    textState: String,
    onTextChange: (String) -> Unit,
    filteredOptions: List<FoodItem>,
    onOptionSelected: (FoodItem) -> Unit,
    label: String,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(), // This links the menu to the text field
            value = textState,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // Changes the 'Enter' key to a Search icon
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {
                onTextChange(it)
                onExpandedChange(true) // Show suggestions as the user types
            },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )

        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                filteredOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = {
                            onOptionSelected(selectionOption)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

// Recipe Templates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeTemplate(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    color = when (recipe.difficulty.lowercase()) {
                        "easy" -> MaterialTheme.colorScheme.primaryContainer
                        "medium" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = recipe.difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = when (recipe.difficulty.lowercase()) {
                            "easy" -> MaterialTheme.colorScheme.onPrimaryContainer
                            "medium" -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RecipeInfoItem(
                    icon = Icons.Default.AccessTime,
                    label = "${recipe.prepTime + recipe.cookTime} min"
                )
                RecipeInfoItem(
                    icon = Icons.Default.People,
                    label = "${recipe.servings} servings"
                )
            }

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipe.ingredients) { ingredient ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = ingredient.name,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeInfoItem(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun RecipeTemplatePreview() {
    Column {
        recipes.forEach { item ->
            RecipeTemplate(item)
        }
    }
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
        difficulty = "Medium",
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
        difficulty = "Hard",
        ingredients = ingredients,
        instructions = instructions,
        id = 2,
        favorite = false
    )
)
