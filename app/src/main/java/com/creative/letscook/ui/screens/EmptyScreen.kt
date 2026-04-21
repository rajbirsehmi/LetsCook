package com.creative.letscook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creative.letscook.R
import com.creative.letscook.util.testTagIconFavorites
import com.creative.letscook.util.testTagIconHistory
import com.creative.letscook.util.testTagTextFavoritesHint
import com.creative.letscook.util.testTagTextNoFavorites
import com.creative.letscook.util.testTagTextNoRecentSearch
import com.creative.letscook.util.testTagTextRecentSearchHint

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    hint: String,
    testTagIcon: String,
    testTagText: String,
    testTagTextHint: String,
    contentDescription: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .testTag(testTagIcon),
                        imageVector = icon,
                        contentDescription = contentDescription,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.testTag(testTagText),
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.testTag(testTagTextHint),
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun NoRecentsPreview() {
    EmptyScreen(
        icon = Icons.Default.History,
        text = stringResource(R.string.text_no_recent_search),
        hint = stringResource(R.string.text_your_search_history_will_appear_here),
        testTagIcon = testTagIconHistory,
        testTagText = testTagTextNoRecentSearch,
        testTagTextHint = testTagTextRecentSearchHint,
        contentDescription = "No Recents"
    )
}

@Composable
@Preview(showBackground = true)
fun NoFavoritesPreview() {
    EmptyScreen(
        icon = Icons.Default.Favorite,
        text = stringResource(R.string.text_no_favorites),
        hint = stringResource(R.string.text_your_favorite_recipes_will_appear_here),
        testTagIcon = testTagIconFavorites,
        testTagText = testTagTextNoFavorites,
        testTagTextHint = testTagTextFavoritesHint,
        contentDescription = "No Favorites"
    )
}
