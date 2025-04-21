package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun FilterSwitchModern(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leadingIcon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
