package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Dummy Data ---
val recentEvents = listOf(
    "Entrega de Proyecto Final" to "25 de Mayo",
    "Examen de Cálculo" to "28 de Mayo",
    "Reunión de Padres" to "30 de Mayo"
)

@Composable
fun StudentProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Profile Header Card ---
        item {
            Card(
                modifier = Modifier.padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de Perfil",
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Nombre Estudiante",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "ID: 00000000",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // --- Stats Card ---
        item {
            Text("Resumen Académico", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            Card(
                modifier = Modifier.padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                ListItem(
                    leadingContent = { Icon(Icons.Default.Grade, contentDescription = "Promedio") },
                    headlineContent = { Text("Promedio General") },
                    trailingContent = { Text("9.5 / 10.0", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // --- Recent Events Card ---
        item {
            Text("Eventos Recientes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    recentEvents.forEachIndexed { index, event ->
                        ListItem(
                            leadingContent = { Icon(Icons.Default.CalendarToday, contentDescription = "Evento") },
                            headlineContent = { Text(event.first) },
                            supportingContent = { Text(event.second) }
                        )
                        if (index < recentEvents.size - 1) {
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentProfileScreenPreview() {
    StudentProfileScreen()
}
