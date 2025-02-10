package com.example.soccergamesfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.soccergamesfinder.ui.navigation.AppNavigation
import com.example.soccergamesfinder.ui.theme.SoccerGamesFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoccerGamesFinderTheme {
                AppNavigation()
            }
        }
    }
}
