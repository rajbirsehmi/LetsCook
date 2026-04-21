package com.creative.letscook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.creative.letscook.R
import com.creative.letscook.domain.model.Recipe
import com.creative.letscook.ui.components.RecipeTemplate
import com.creative.letscook.util.testTagIconFavorites
import com.creative.letscook.util.testTagIconHistory
import com.creative.letscook.util.testTagTextFavoritesHint
import com.creative.letscook.util.testTagTextNoFavorites
import com.creative.letscook.util.testTagTextNoRecentSearch
import com.creative.letscook.util.testTagTextRecentSearchHint

@Composable
fun RecentAndFavoriteScreen(
    recipes: List<Recipe>,
    isFavorite: Boolean,
    onRecipeClick: (Recipe) -> Unit
) {
    if (recipes.isEmpty()) {
        EmptyScreen(
            icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.History,
            text = stringResource(
                if (isFavorite) R.string.text_no_favorites 
                else R.string.text_no_recent_search
            ),
            hint = stringResource(
                if (isFavorite) R.string.text_your_favorite_recipes_will_appear_here 
                else R.string.text_your_search_history_will_appear_here
            ),
            testTagIcon = if (isFavorite) testTagIconFavorites else testTagIconHistory,
            testTagText = if (isFavorite) testTagTextNoFavorites else testTagTextNoRecentSearch,
            testTagTextHint = if (isFavorite) testTagTextFavoritesHint else testTagTextRecentSearchHint,
            contentDiscription = if (isFavorite) "No Favorites" else "No Recents"
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(recipes, key = { it.id ?: it.name }) { recipe ->
                RecipeTemplate(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) }
                )
            }
        }
    }
}

