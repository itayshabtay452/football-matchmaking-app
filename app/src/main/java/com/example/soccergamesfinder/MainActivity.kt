package com.example.soccergamesfinder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.soccergamesfinder.ui.navigation.AppNavigation
import com.example.soccergamesfinder.ui.theme.SoccerGamesFinderTheme
import com.example.soccergamesfinder.data.FieldModel
import com.example.soccergamesfinder.data.FieldRepository

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

private fun uploadSampleFields() {
    val repository = FieldRepository()

    val fields = listOf(
        FieldModel(
            name = "מגרש שכונת הדר",
            latitude = 32.0853,
            longitude = 34.7818,
            hasLighting = true,
            requiresOwnerApproval = false,
            isPaid = false,
            fieldSize = "5x5",
            fieldType = "דשא סינטטי"
        ),
        FieldModel(
            name = "מגרש בן יהודה",
            latitude = 32.0795,
            longitude = 34.7747,
            hasLighting = false,
            requiresOwnerApproval = true,
            isPaid = true,
            fieldSize = "7x7",
            fieldType = "בטון"
        )
    )

    fields.forEach { field ->
        repository.uploadField(field) { success ->
            if (success)
                Log.d("Upload", "הועלה בהצלחה: ${field.name}")
            else
                Log.e("Upload", "שגיאה בהעלאה: ${field.name}")
        }
    }
}
