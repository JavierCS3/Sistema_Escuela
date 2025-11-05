package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Data classes for structure ---
data class Alert(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val timestamp: String
)

// --- Dummy Data ---
val alerts = listOf(
    Alert(Icons.Default.Grade, "Nueva Calificación", "Se ha registrado una calificación de 9.8 en Matemáticas.", "Hace 5 min"),
    Alert(Icons.Default.TaskAlt, "Tarea Entregada", "Tu reporte de lectura fue entregado con éxito.", "Hace 1 hora"),
    Alert(Icons.Default.Campaign, "Anuncio General", "La escuela suspenderá actividades el día de mañana.", "Ayer"),
    Alert(Icons.Default.Grade, "Nueva Calificación", "Se ha registrado una calificación de 8.5 en Historia.", "Ayer"),
)

@Composable
fun AlertsScreen() {
    if (alerts.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(alerts) { alert ->
                ListItem(
                    headlineContent = { Text(alert.title) },
                    supportingContent = { Text(alert.description) },
                    leadingContent = {
                        Icon(
                            alert.icon,
                            contentDescription = alert.title,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    trailingContent = { Text(alert.timestamp) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Default.NotificationsOff,
                contentDescription = "No hay notificaciones",
                modifier = Modifier.size(120.dp),
                tint = Color.Gray
            )
            Text(
                text = "No tienes notificaciones nuevas",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlertsScreenPreview() {
    AlertsScreen()
}

@Preview(showBackground = true)
@Composable
fun EmptyAlertsScreenPreview() {
    // To see the empty state, we need a composable that uses it.
    // We can just call it directly.
    EmptyState()
}
