package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.ui.components.home.FieldListSection
import com.example.soccergamesfinder.ui.components.home.FilterBar
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.FieldDetailsViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    navigateToLogin: () -> Unit,
    navigateToField: (String) -> Unit,
    navigateToAddField: () -> Unit
) {
    val fieldListViewModel: FieldListViewModel = hiltViewModel()
    val user by userViewModel.user.collectAsState()
    val uiState by fieldListViewModel.uiState.collectAsState()
    var filtersExpanded by remember { mutableStateOf(false) } // 🔻 כברירת מחדל סגור

    // טען משתמש
    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    // טען מגרשים לפי מיקום
    LaunchedEffect(user) {
        user?.let {
            fieldListViewModel.loadNearbyFields(it.latitude, it.longitude)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddField,
                content = { Text("📤 מכיר מגרש שלא קיים אצלנו?") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                UserHeader(user = user) {
                    authViewModel.logout()
                    userViewModel.logout()
                    navigateToLogin()
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                FilterToggleHeader(
                    expanded = filtersExpanded,
                    onToggle = { filtersExpanded = !filtersExpanded }
                )
                if (filtersExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FilterBar(
                        filterState = uiState.filterState,
                        onCityChanged = { fieldListViewModel.updateCity(it) },
                        onLightingChanged = { fieldListViewModel.updateLighting(it) },
                        onSizeChanged = { fieldListViewModel.updateSize(it) },
                        onMaxDistanceChanged = { fieldListViewModel.updateMaxDistance(it) },
                        onResetFilters = { fieldListViewModel.resetFilters() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Text("🗺️ מגרשים באיזור שלך", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            FieldListSection(
                isLoading = uiState.isLoading,
                fields = uiState.fields,
                onLoadMore = { fieldListViewModel.loadMoreFields() },
                onFieldClick = navigateToField
            )
        }
    }
}


@Composable
fun FilterToggleHeader(expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("🔍 סינון מגרשים", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onToggle) {
            Text(if (expanded) "הסתר" else "הצג")
        }
    }
}

@Composable
fun UserHeader(user: User?, onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (user?.profileImageUrl != null) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp)
                )
            }
            Text("שלום, ${user?.nickname ?: "משתמש"}", style = MaterialTheme.typography.titleMedium)
        }

        TextButton(onClick = onLogout) {
            Text("🚪 התנתק")
        }
    }
}

