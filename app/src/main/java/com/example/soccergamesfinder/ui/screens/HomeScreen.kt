package com.example.soccergamesfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.data.FieldFilterState
import com.example.soccergamesfinder.ui.components.home.FieldListSection
import com.example.soccergamesfinder.ui.components.home.FilterBar
import com.example.soccergamesfinder.ui.components.home.LogoutButton
import com.example.soccergamesfinder.ui.components.home.UserProfileSection
import com.example.soccergamesfinder.viewmodel.AuthViewModel
import com.example.soccergamesfinder.viewmodel.FieldViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel,userViewModel: UserViewModel,
               fieldViewModel: FieldViewModel, navigateToLogin: () -> Unit,
                navigateToField: (String) -> Unit)
{

    val user by userViewModel.user.collectAsState()
    val fields by fieldViewModel.fields.collectAsState()
    val isLoading by fieldViewModel.isLoading.collectAsState()
    val filterState by fieldViewModel.filterState.collectAsState()

    LaunchedEffect(Unit){
        userViewModel.loadUser()
    }

    LaunchedEffect(user){
        if (user != null)
        {
            fieldViewModel.loadNearbyFields(user?.latitude, user?.longitude)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            UserProfileSection(user)
        }

        item {
            FilterSection(
                filterState = filterState,
                onLightingChanged = { fieldViewModel.updateLightingFilter(it) },
                onParkingChanged = { fieldViewModel.updateParkingFilter(it) },
                onFencingChanged = { fieldViewModel.updateFencingFilter(it) },
                onNameQueryChanged = { fieldViewModel.updateNameQuery(it) },
                onSizeChanged = { fieldViewModel.updateSizeFilter(it) },
                onMaxDistanceChanged = { fieldViewModel.updateMaxDistanceKm(it.toDoubleOrNull()) }
            )
        }

        FieldListSection(
            isLoading = isLoading,
            fields = fields,
            onLoadMore = { fieldViewModel.loadMoreFields() },
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
