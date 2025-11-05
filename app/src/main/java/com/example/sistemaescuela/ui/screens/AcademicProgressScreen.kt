package com.example.sistemaescuela.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcademicProgressScreen() {
    // Dummy data
    val overallProgress = 0.6f // 60%
    val averageGrade = 8.8
    val semesters = listOf(
        "Semestre 1" to 1f, "Semestre 2" to 1f, "Semestre 3" to 1f,
        "Semestre 4" to 1f, "Semestre 5" to 0.7f, "Semestre 6" to 0.2f
    )
    
    val animatedProgress by animateFloatAsState(targetValue = overallProgress, label = "Overall Progress Animation")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Top Section: Progress Circle and Average Grade ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular progress indicator
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(140.dp),
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Average grade card
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Promedio", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = averageGrade.toString(),
                        fontSize = 28.sp,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Bottom Section: Semesters List ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                semesters.forEach { (name, progress) ->
                    SemesterProgressItem(name = name, progress = progress)
                }
            }
        }
    }
}

@Composable
fun SemesterProgressItem(name: String, progress: Float) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "Semester Progress Animation")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, modifier = Modifier.weight(1f))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(2f)
                .height(8.dp),
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AcademicProgressScreenPreview() {
    AcademicProgressScreen()
}
