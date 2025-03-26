package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.data.FieldFilterState
import com.example.soccergamesfinder.ui.components.home.FieldListSection
import com.example.soccergamesfinder.ui.components.home.FilterBar
import com.example.soccergamesfinder.ui.components.home.LogoutButton
import com.example.soccergamesfinder.ui.components.home.UserProfileSection
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldListViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel,userViewModel: UserViewModel,
               navigateToLogin: () -> Unit,
                navigateToField: (String) -> Unit)
{
    val fieldListViewModel: FieldListViewModel = hiltViewModel()

    val user by userViewModel.user.collectAsState()
    val uiState by fieldListViewModel.uiState.collectAsState()


    LaunchedEffect(Unit){
        userViewModel.loadUser()
    }

    LaunchedEffect(user){
        if (user != null)
        {
            fieldListViewModel.loadNearbyFields(user?.latitude, user?.longitude)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            UserProfileSection(user)
        }

        item {
            FilterSection(
                filterState = uiState.filterState,
                onLightingChanged = { fieldListViewModel.updateLighting(it) },
                onParkingChanged = { fieldListViewModel.updateParking(it) },
                onFencingChanged = { fieldListViewModel.updateFencing(it) },
                onNameQueryChanged = { fieldListViewModel.updateNameQuery(it) },
                onSizeChanged = { fieldListViewModel.updateSize(it) },
                onMaxDistanceChanged = { fieldListViewModel.updateMaxDistance(it.toDoubleOrNull()) }
            )
        }

        FieldListSection(
            isLoading = uiState.isLoading,
            fields = uiState.fields,
            onLoadMore = { fieldListViewModel.loadMoreFields() },
            onFieldClick = navigateToField
        )


        item {
            Spacer(modifier = Modifier.height(16.dp))
            LogoutButton(authViewModel = authViewModel, userViewModel = userViewModel,
                onLogout = navigateToLogin)

        }
    }
}

@Composable
fun FilterSection(
    filterState: FieldFilterState,
    onLightingChanged: (Boolean) -> Unit,
    onParkingChanged: (Boolean) -> Unit,
    onFencingChanged: (Boolean) -> Unit,
    onNameQueryChanged: (String) -> Unit,
    onSizeChanged: (String?) -> Unit,
    onMaxDistanceChanged: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    FilterBar(
        filterState = filterState,
        onLightingChanged = onLightingChanged,
        onParkingChanged = onParkingChanged,
        onFencingChanged = onFencingChanged,
        onNameQueryChanged = onNameQueryChanged,
        onSizeChanged = onSizeChanged,
        onMaxDistanceChanged = onMaxDistanceChanged
    )
}
