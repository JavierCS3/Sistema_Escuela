package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onNavigateUp: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Parent Information Section ---
        item {
            Text("Información del Padre", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    SettingsItem(icon = Icons.Default.Person, title = "Nombre", value = "Juan Pérez", onClick = { /* TODO: Edit Name */ })
                    Divider()
                    SettingsItem(icon = Icons.Default.Lock, title = "Contraseña", value = "********", onClick = { /* TODO: Edit Password */ })
                    Divider()
                    SettingsItem(icon = Icons.Default.Email, title = "Correo", value = "juan.perez@email.com", onClick = { /* TODO: Edit Email */ })
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // --- Student Information Section (Read-only) ---
        item {
            Text("Información del Alumno", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    // These items are not clickable
                    SettingsItem(icon = Icons.Default.Person, title = "Nombre", value = "Miguel Pérez", showEditIcon = false)
                    Divider()
                    SettingsItem(icon = Icons.Default.Lock, title = "Contraseña", value = "********", showEditIcon = false)
                    Divider()
                    SettingsItem(icon = Icons.Default.Email, title = "Correo", value = "miguel.perez@email.com", showEditIcon = false)
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            Button(
                onClick = { /* TODO: Handle save changes */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR CAMBIOS")
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    value: String,
    showEditIcon: Boolean = true,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = if (showEditIcon) Modifier.clickable(onClick = onClick) else Modifier,
        leadingContent = { Icon(icon, contentDescription = title) },
        headlineContent = { Text(title) },
        supportingContent = { Text(value) },
        trailingContent = {
            if (showEditIcon) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onNavigateUp = {})
}
