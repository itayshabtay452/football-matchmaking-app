package com.example.soccergamesfinder.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single item in the bottom navigation bar.
 *
 * @property name Display name of the tab (used as label)
 * @property route Navigation route associated with the tab
 * @property icon Icon to display in the tab
 */
data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)
