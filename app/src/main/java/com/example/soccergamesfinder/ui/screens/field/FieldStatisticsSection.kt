package com.example.soccergamesfinder.ui.screens.field

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun FieldStatisticsSection(
    totalGames: Int,
    avgPlayers: Double,
    fullGames: Int,
    openGames: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("📊 סטטיסטיקות מגרש", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(title = "סה\"כ משחקים", value = "$totalGames", modifier = Modifier.weight(1f))
            StatCard(title = "ממוצע שחקנים", value = String.format("%.1f", avgPlayers.toFloat()) , modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(title = "משחקים מלאים", value = "$fullGames", modifier = Modifier.weight(1f))
            StatCard(title = "משחקים פתוחים", value = "$openGames", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
