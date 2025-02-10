// קובץ: Theme.kt
package com.example.soccergamesfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// הגדרות צבעים מותאמות לאווירה של כדורגל שכונתי
val SoccerGreen = Color(0xFF388E3C)    // ירוק כהה, מזכיר דשא
val LightGreen = Color(0xFFC8E6C9)     // ירוק בהיר, רקע נעים
val SoccerBlue = Color(0xFF1976D2)      // כחול למגע ניגודי ואייקונים
val SoccerRed = Color(0xFFD32F2F)       // אדום להודעות שגיאה
val SoccerWhite = Color(0xFFFFFFFF)     // לבן לטקסטים על רקעים כהים

// נגדיר ערכת צבעים לאפליקציה (נשתמש בערכת צבעים בהירה כאן)
private val LightColorScheme = lightColorScheme(
    primary = SoccerGreen,
    onPrimary = SoccerWhite,
    secondary = SoccerBlue,
    onSecondary = SoccerWhite,
    background = LightGreen,
    onBackground = Color.Black,
    surface = SoccerWhite,
    onSurface = Color.Black,
    error = SoccerRed,
    onError = SoccerWhite
)

// אפשר להגדיר גם ערכת צבעים חשוכה – ניתן להרחיב בהמשך אם תרצה
private val DarkColorScheme = darkColorScheme(
    primary = SoccerGreen,
    onPrimary = SoccerWhite,
    secondary = SoccerBlue,
    onSecondary = SoccerWhite,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White,
    error = SoccerRed,
    onError = SoccerWhite
)

// טיפוגרפיה וצורות – כאן אפשר לשדרג בהתאמה, כרגע נשתמש בהגדרות ברירת מחדל
private val AppTypography = Typography()
private val AppShapes = Shapes()

@Composable
fun SoccerGamesFinderTheme(
    // אם רוצים לאפשר למשתמש לעבור בין מצב בהיר לחשוך, אפשר להוסיף פרמטר isDarkTheme
    // כרגע נשתמש בהגדרת המערכת:
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
