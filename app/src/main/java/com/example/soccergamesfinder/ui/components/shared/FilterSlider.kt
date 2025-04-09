package com.example.soccergamesfinder.ui.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun FilterSlider(
    label: String,
    value: Float?,
    onValueChange: (Float?) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    modifier: Modifier = Modifier
) {
    var sliderValue by remember { mutableStateOf(value ?: valueRange.start) }

    Column(modifier = modifier) {
        Text("$label: ${"%.1f".format(sliderValue)} ק״מ")
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onValueChange(it)
            },
            valueRange = valueRange,
            steps = steps
        )
    }
}
