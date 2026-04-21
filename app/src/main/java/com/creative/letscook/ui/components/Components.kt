package com.creative.letscook.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.creative.letscook.R
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.domain.model.Ingredient
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.util.testTagTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome() {
    CenterAlignedTopAppBar(
        modifier = Modifier.testTag(testTagTopAppBar),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPantryScreen(
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.text_whats_in_your_pantry),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarRecipeListScreen(
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Recipes for You",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarRecipeScreen(
    recipeName: String,
    isFavorite: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigationBack: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    LargeTopAppBar(
        title = {
            Text(
                text = recipeName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Delete Recipe") },
                        onClick = {
                            showMenu = false
                            onDeleteClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun FloatingActionButtonCookingArea(
    onClick: () -> Unit = {},
) {
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(16.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp),
        icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null) },
        text = {
            Text(
                text = "What's in my pantry?",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

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
            Text(text = type, fontWeight = FontWeight.Medium)
        }
    )
}

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
                .menuAnchor(),
            value = textState,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {
                onTextChange(it)
                onExpandedChange(true)
            },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                filteredOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = { onOptionSelected(selectionOption) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    recipe.countryName?.let { country ->
                        Text(
                            text = country.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 28.sp
                    )
                }

                Surface(
                    color = when (recipe.difficulty.lowercase()) {
                        "easy" -> MaterialTheme.colorScheme.secondaryContainer
                        "medium" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFFFEBEE)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = recipe.difficulty,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        color = when (recipe.difficulty.lowercase()) {
                            "easy" -> MaterialTheme.colorScheme.onSecondaryContainer
                            "medium" -> Color(0xFFE65100)
                            else -> Color(0xFFC62828)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    RecipeInfoItemSmall(
                        icon = Icons.Default.AccessTime,
                        label = "${recipe.prepTime + recipe.cookTime} min"
                    )
                    RecipeInfoItemSmall(
                        icon = Icons.Default.People,
                        label = "${recipe.servings} servings"
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeInfoItemSmall(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun RecipeTemplatePreview() {
    LetsCookTheme {
        Column {
            recipes.forEach { item ->
                RecipeTemplate(item)
            }
        }
    }
}

private val ingredients = listOf(Ingredient("Tomato", "4"))
private val instructions = listOf("Step 1")
private val recipes = listOf(
    Recipe(0, "Pasta Surprise", 2, 15, 20, "Easy", "Italian", ingredients, instructions, false),
    Recipe(1, "Spicy Tacos", 4, 20, 15, "Medium", "Mexican", ingredients, instructions, true)
)

@Composable
fun LetsCookTheme(content: @Composable () -> Unit) {
    // Helper for preview
    MaterialTheme(content = content)
}
