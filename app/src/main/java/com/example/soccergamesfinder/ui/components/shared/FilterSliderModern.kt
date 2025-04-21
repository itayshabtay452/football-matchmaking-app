package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FilterSliderModern(
    label: String,
    value: Float?,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float?) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.let {
                Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text("$label: ${value?.toInt() ?: "לא נבחר"}", style = MaterialTheme.typography.labelSmall)
        }

        value?.let {
            Slider(
                value = it,
                onValueChange = { onValueChange(it) },
                valueRange = valueRange,
                steps = steps,
                modifier = Modifier.fillMaxWidth()
            )
        } ?: OutlinedButton(
            onClick = { onValueChange(valueRange.start) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("בחר ערך")
        }
    }
}
