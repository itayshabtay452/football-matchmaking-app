// AppTheme.kt – עיצוב מודרני ומעודכן

package com.example.soccergamesfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography

// צבעים מותאמים אישית
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4A47A3),
    onPrimary = Color.White,
    secondary = Color(0xFF84C9FB),
    onSecondary = Color.Black,
    background = Color(0xFFF7F9FC),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFEAEAEA),
    primaryContainer = Color(0xFFE0E7FF),
    secondaryContainer = Color(0xFFDFF6FF),
    outline = Color(0xFFCCCCCC)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB3BCF5),
    onPrimary = Color.Black,
    secondary = Color(0xFF9BD9FF),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    primaryContainer = Color(0xFF444D78),
    secondaryContainer = Color(0xFF315F72),
    outline = Color(0xFF666666)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(), // טיפוגרפיה ברירת מחדל
        content = content
    )
}
