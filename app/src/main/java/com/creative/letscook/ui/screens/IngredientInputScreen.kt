package com.creative.letscook.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.creative.letscook.R
import com.creative.letscook.domain.enums.Countries
import com.creative.letscook.domain.enums.DietaryType
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
        selectedCountries = viewModel.selectedCountries,
        selectedDietaryType = viewModel.selectedDietaryType,
        expanded = viewModel.expanded,
        onExpandedChange = { viewModel.expanded = it },
        textState = viewModel.textState,
        onTextChange = { viewModel.onTextChange(it) },
        filteredOptions = viewModel.filteredOptions,
        onOptionSelected = { viewModel.onOptionSelected(it) },
        onRemoveIngredient = { viewModel.selectedOptions.remove(it) },
        onToggleCountry = { viewModel.toggleCountrySelection(it) },
        onDietaryTypeSelected = { viewModel.onDietaryTypeSelected(it) },
        onNavigationBack = onNavigationBack,
        onCookClick = {
            viewModel.generateRecipe()
            navController.navigate(Routes.RECIPE_LIST)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun IngredientInputContent(
    ingredients: List<String>,
    selectedCountries: List<Countries>,
    selectedDietaryType: DietaryType?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    textState: String,
    onTextChange: (String) -> Unit,
    filteredOptions: List<FoodItem>,
    onOptionSelected: (FoodItem) -> Unit,
    onRemoveIngredient: (String) -> Unit,
    onToggleCountry: (Countries) -> Unit,
    onDietaryTypeSelected: (DietaryType) -> Unit,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AutocompleteIngredientField(
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                textState = textState,
                onTextChange = onTextChange,
                filteredOptions = filteredOptions,
                onOptionSelected = onOptionSelected,
                label = stringResource(R.string.text_enter_ingredient_e_g_tomato)
            )

            if (ingredients.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ingredients.forEach { ingredient ->
                        InputChip(
                            selected = true,
                            onClick = { /* No-op */ },
                            label = { Text(ingredient) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { onRemoveIngredient(ingredient) }
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Preferences Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Preferences",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    var dietaryDropdownExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = dietaryDropdownExpanded,
                        onExpandedChange = { dietaryDropdownExpanded = !dietaryDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDietaryType?.displayName ?: "Dietary Restriction",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dietaryDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        ExposedDropdownMenu(
                            expanded = dietaryDropdownExpanded,
                            onDismissRequest = { dietaryDropdownExpanded = false }
                        ) {
                            DietaryType.entries.forEach { dietaryType ->
                                DropdownMenuItem(
                                    text = { Text(dietaryType.displayName) },
                                    onClick = {
                                        onDietaryTypeSelected(dietaryType)
                                        dietaryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Preferred Cuisines",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(Countries.entries) { country ->
                            val isSelected = selectedCountries.contains(country)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onToggleCountry(country) },
                                label = { Text(country.displayName) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onCookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = ingredients.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Find Recipes", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientInputScreenPreview() {
    IngredientInputContent(
        ingredients = listOf("Tomato", "Onion", "Garlic"),
        selectedCountries = listOf(Countries.ITALIAN, Countries.MEXICAN),
        selectedDietaryType = DietaryType.VEGAN,
        expanded = false,
        onExpandedChange = {},
        textState = "",
        onTextChange = {},
        filteredOptions = emptyList(),
        onOptionSelected = {},
        onRemoveIngredient = {},
        onToggleCountry = {},
        onDietaryTypeSelected = {},
        onNavigationBack = {},
        onCookClick = {}
    )
}
