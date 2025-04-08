// 📁 ui/components/permissions/LocationPermissionHandler.kt
package com.example.soccergamesfinder.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Composable helper for requesting location permission.
 *
 * @param onPermissionGranted Callback to execute when permission is granted
 * @param onPermissionDenied (Optional) Callback if denied (e.g., show dialog)
 */
@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: (() -> Unit)? = null
) {
    val context = LocalContext.current

    var permissionRequested by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied?.invoke()
        }
    }

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            onPermissionGranted()
        } else if (!permissionRequested) {
            permissionRequested = true
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
