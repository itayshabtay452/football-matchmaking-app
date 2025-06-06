package com.example.soccergamesfinder.ai

import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.model.User
import java.text.SimpleDateFormat
import java.util.*

data class GameWithDetails(
    val game: Game,
    val field: Field,
    val joinedPlayers: List<User>
)

fun buildGameRecommendationPrompt(
    user: User,
    games: List<GameWithDetails>
): String {
    val userAge = calculateAgeFromString(user.birthDate)?.toString() ?: "לא ידוע"
    val preferredDays = if (user.preferredDays.isNotEmpty())
        user.preferredDays.joinToString(", ")
    else "אין ימים מועדפים"

    val preferredHours = if (user.startHour != null && user.endHour != null) {
        "בין השעות ${user.startHour}:00 ל־${user.endHour}:00"
    } else {
        "אין שעות מועדפות"
    }

    val locationText = user.address?.let {
        "מתגורר בקרבת מיקום: $it"
    } ?: "מתגורר בקואורדינטות (${user.latitude}, ${user.longitude})"

    val userInfo = """
    👤 פרטי המשתמש:
    המשתמש בן $userAge. $locationText.
    ימים מועדפים: $preferredDays.
    שעות מועדפות: $preferredHours.
""".trimIndent()
    val dateFormat = SimpleDateFormat("EEEE", Locale("he"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale("he"))

    val gameList = games.mapIndexed { index, g ->
        val game = g.game
        val field = g.field

        val fieldName = field.name.ifBlank { "מגרש ללא שם" }
        val address = field.address?.takeIf { it.isNotBlank() } ?: "כתובת לא ידועה"
        val hasLighting = if (field.lighting) "יש תאורה" else "אין תאורה"
        val isFollowed = if (field.id in user.fieldsFollowed) " (מגרש אהוב)" else ""
        val distanceText = field.distance?.let { "במרחק %.1f ק\"מ".format(it) } ?: "מרחק לא ידוע"

        val day = dateFormat.format(game.startTime.toDate())
        val start = timeFormat.format(game.startTime.toDate())
        val end = timeFormat.format(game.endTime.toDate())

        val ages = g.joinedPlayers.mapNotNull { calculateAgeFromString(it.birthDate) }
        val ageText = if (ages.isNotEmpty()) {
            val avgAge = ages.average().toInt()
            "$avgAge בממוצע"
        } else {
            "אין שחקנים עדיין"
        }

        "משחק ID: ${game.id} – ביום $day בין $start ל־$end, במגרש $fieldName, $address. $hasLighting, $distanceText$isFollowed. משתתפים: ${g.joinedPlayers.size}, גיל: $ageText"
    }.joinToString("\n")

    val gamesInfo = if (gameList.isNotBlank()) "🎯 משחקים פתוחים:\n$gameList"
    else "לא נמצאו משחקים מתאימים להצגה."

    val question = """
❓ איזה משחק הכי מתאים למשתמש ולמה?
אנא סיים את תשובתך בשורה נפרדת כך:
[GAME_ID: <id של המשחק המתאים ביותר>]
""".trimIndent()

    return "$userInfo\n\n$gamesInfo\n\n$question"
}


fun calculateAgeFromString(dateString: String?): Int? {
    if (dateString.isNullOrBlank()) return null
    val formats = listOf(
        "yyyy-MM-dd",  // פורמט קודם
        "dd/MM/yyyy"   // הפורמט שלך בפועל
    )
    for (format in formats) {
        try {
            val sdf = SimpleDateFormat(format, Locale.US)
            val date = sdf.parse(dateString)
            if (date != null) return calculateAge(date)
        } catch (_: Exception) {
        }
    }
    return null
}


fun calculateAge(birthDate: Date): Int {
    val today = Calendar.getInstance()
    val dob = Calendar.getInstance().apply { time = birthDate }
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age
}
