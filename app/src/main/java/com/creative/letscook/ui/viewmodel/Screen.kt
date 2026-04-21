package com.creative.letscook.ui.viewmodel

import com.creative.letscook.util.routeCookingArea
import com.creative.letscook.util.routeFavorites
import com.creative.letscook.util.routeRecents

sealed class Screen(val route: String) {
    object Recents: Screen(routeRecents)
    object Favorites: Screen(routeFavorites)
    object CookingArea: Screen(routeCookingArea)
    object Kitchen: Screen("kitchen/{recipeId}") {
        fun createRoute(recipeId: Int) = "kitchen/$recipeId"
    }
}
