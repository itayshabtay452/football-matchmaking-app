package com.example.soccergamesfinder.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Bottom navigation bar component for navigating between main app sections.
 *
 * Highlights the selected tab and optionally shows a badge on the "Notifications" tab.
 *
 * @param items List of BottomNavItem representing tabs
 * @param navController NavController to observe current destination
 * @param onItemClick Callback invoked when a tab is clicked
 * @param unreadCount Number of unread notifications (used only for "Notifications" tab)
 */
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit,
    unreadCount: Int = 0
) {
    // Observe the current route from the navigation back stack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = { onItemClick(item) },
                icon = @Composable {
                    // Show unread badge only on "Notifications" tab
                    if (item.name == "התראות" && unreadCount > 0) {
                        BadgedBox(badge = {
                            Badge {
                                Text(unreadCount.toString())
                            }
                        }) {
                            Icon(item.icon, contentDescription = item.name)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.name)
                    }
                },
                label = {
                    Text(item.name)
                }
            )
        }
    }
}
