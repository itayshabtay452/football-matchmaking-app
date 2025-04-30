package com.example.soccergamesfinder.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soccergamesfinder.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit,
    unreadCount: Int = 0
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = { onItemClick(item) },
                icon = @Composable {
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
