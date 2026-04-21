package com.creative.letscook.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.creative.letscook.R
import com.creative.letscook.ui.components.TopAppBarHome
import com.creative.letscook.ui.navigation.Routes.FULL_RECIPE
import com.creative.letscook.ui.screens.FullRecipeScreen
import com.creative.letscook.ui.screens.IngredientInputScreen
import com.creative.letscook.ui.screens.LoadingRecipeScreen
import com.creative.letscook.ui.screens.RecentAndFavoriteScreen
import com.creative.letscook.ui.screens.RecipeListScreen
import com.creative.letscook.ui.viewmodel.IngredientsViewModel
import com.creative.letscook.ui.viewmodel.RecipeUiState
import com.creative.letscook.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    ingredientsViewModel: IngredientsViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "main_home"
    ) {
        composable("main_home") {
            val uiState by recipeViewModel.uiState.collectAsStateWithLifecycle()
            MainScaffoldScreen(navController, uiState)
        }
        composable(Routes.INGREDIENT_INPUT) {
            IngredientInputScreen(
                navController = navController,
                viewModel = ingredientsViewModel,
                onNavigationBack = { navController.popBackStack() }
            )
        }
        composable(Routes.RECIPE_LIST) {
            val isLoading by ingredientsViewModel.isLoading.collectAsStateWithLifecycle()
            val recipes by ingredientsViewModel.recipes.collectAsStateWithLifecycle()
            
            if (isLoading) {
                LoadingRecipeScreen()
            } else {
                RecipeListScreen(
                    recipes = recipes,
                    navController = navController
                )
            }
        }
        composable(
            route = "${FULL_RECIPE}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) {
            FullRecipeScreen(
                onNavigationBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScaffoldScreen(
    rootNavController: NavHostController,
    uiState: RecipeUiState
) {
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBarHome()
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    rootNavController.navigate(Routes.INGREDIENT_INPUT)
                },
                text = {
                    Text(text = "Pantry")
                },
                icon = {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = "Cook")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    NavigationItem(
                        route = Routes.RECENT,
                        label = stringResource(R.string.text_recents),
                        icon = Icons.AutoMirrored.Filled.List,
                        badgeCount = uiState.recents.size
                    ),
                    NavigationItem(
                        route = Routes.FAVORITES,
                        label = stringResource(R.string.text_favorites),
                        icon = Icons.Default.Favorite,
                        badgeCount = uiState.favorites.size
                    )
                )

                items.forEach { item ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            nestedNavController.navigate(item.route) {
                                popUpTo(nestedNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(item.label) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount > 0) {
                                        Badge { Text(item.badgeCount.toString()) }
                                    }
                                }
                            ) {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nestedNavController,
            startDestination = Routes.RECENT,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.RECENT) {
                RecentAndFavoriteScreen(
                    recipes = uiState.recents,
                    isFavorite = false,
                    onRecipeClick = { recipe ->
                        rootNavController.navigate("${FULL_RECIPE}/${recipe.id}")
                    }
                )
            }
            composable(Routes.FAVORITES) {
                RecentAndFavoriteScreen(
                    recipes = uiState.favorites,
                    isFavorite = true,
                    onRecipeClick = { recipe ->
                        rootNavController.navigate("${FULL_RECIPE}/${recipe.id}")
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
@Preview
fun MainScaffoldScreenPreview() {
    MainScaffoldScreen(
        rootNavController = rememberNavController(),
        uiState = RecipeUiState()
    )
}