package com.creative.letscook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
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
    contentDiscription: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(64.dp)
                    .testTag(testTagIcon),
                imageVector = icon,
                contentDescription = contentDiscription,
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .testTag(testTagText),
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier
                    .testTag(testTagTextHint),
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview
fun NoRecentsPreview() {
    EmptyScreen(
        icon = Icons.Default.History,
        text = stringResource(R.string.text_no_recent_search),
        hint = stringResource(R.string.text_your_search_history_will_appear_here),
        testTagIcon = testTagIconHistory,
        testTagText = testTagTextNoRecentSearch,
        testTagTextHint = testTagTextRecentSearchHint,
        contentDiscription = testTagIconHistory
    )
}

@Composable
@Preview
fun NoFavoritesPreview() {
    EmptyScreen(
        icon = Icons.Default.Favorite,
        text = stringResource(R.string.text_no_favorites),
        hint = stringResource(R.string.text_your_favorite_recipes_will_appear_here),
        testTagIcon = testTagIconFavorites,
        testTagText = testTagTextNoFavorites,
        testTagTextHint = testTagTextFavoritesHint,
        contentDiscription = testTagIconFavorites
    )
}
