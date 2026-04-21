package com.creative.letscook.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.creative.letscook.R
import com.creative.letscook.domain.model.FoodItem
import com.creative.letscook.ui.components.AutocompleteIngredientField
import com.creative.letscook.ui.components.TopAppBarPantryScreen
import com.creative.letscook.ui.navigation.Routes
import com.creative.letscook.ui.viewmodel.IngredientsViewModel

@Composable
fun IngredientInputScreen(
    navController: NavHostController,
    viewModel: IngredientsViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    IngredientInputContent(
        ingredients = viewModel.selectedOptions,
        expanded = viewModel.expanded,
        onExpandedChange = { viewModel.expanded = it },
        textState = viewModel.textState,
        onTextChange = { viewModel.onTextChange(it) },
        filteredOptions = viewModel.filteredOptions,
        onOptionSelected = { viewModel.onOptionSelected(it) },
        onRemoveIngredient = { viewModel.selectedOptions.remove(it) },
        onNavigationBack = onNavigationBack,
        onCookClick = {
            viewModel.generateRecipe()
            navController.navigate(Routes.RECIPE_LIST)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientInputContent(
    ingredients: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    textState: String,
    onTextChange: (String) -> Unit,
    filteredOptions: List<FoodItem>,
    onOptionSelected: (FoodItem) -> Unit,
    onRemoveIngredient: (String) -> Unit,
    onNavigationBack: () -> Unit,
    onCookClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarPantryScreen(
                onClick = onNavigationBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add ingredients you have on hand to find the perfect recipe.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            AutocompleteIngredientField(
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                textState = textState,
                onTextChange = onTextChange,
                filteredOptions = filteredOptions,
                onOptionSelected = onOptionSelected,
                label = stringResource(R.string.text_enter_ingredient_e_g_tomato)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Ingredients",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (ingredients.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = ingredients.size.toString(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                if (ingredients.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outlineVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No ingredients added yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ingredients.forEach { ingredient ->
                            InputChip(
                                selected = true,
                                onClick = { /* No-op */ },
                                label = {
                                    Text(
                                        text = ingredient,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable {
                                                onRemoveIngredient(ingredient)
                                            }
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    selectedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                border = null
                            )
                        }
                    }
                }
            }

            // Cook Button Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Button(
                    onClick = onCookClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = ingredients.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find Recipes", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientInputScreenPreview() {
    IngredientInputContent(
        ingredients = listOf("Tomato", "Onion", "Garlic"),
        expanded = false,
        onExpandedChange = {},
        textState = "",
        onTextChange = {},
        filteredOptions = emptyList(),
        onOptionSelected = {},
        onRemoveIngredient = {},
        onNavigationBack = {},
        onCookClick = {}
    )
}