package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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

// --- Data classes for structure ---
data class Contact(val name: String, val title: String, val lastMessage: String)

// --- Dummy Data ---
val contacts = listOf(
    Contact("Juan Pérez", "Profesor de Matemáticas", "¡No olvides la tarea para mañana!"),
    Contact("María García", "Profesora de Ciencias", "El laboratorio será el viernes."),
    Contact("Carlos López", "Profesor de Inglés", "Okay, see you in class."),
    Contact("Ana Martínez", "Profesora de Física", "Recibido, gracias."),
    Contact("Pedro Ramírez", "Profesor de Historia", "El ensayo es para el próximo lunes."),
    Contact("Laura Flores", "Profesora de Geografía", "Aquí tienes el mapa que pediste."),
    Contact("Sofía Torres", "Profesora de Computación", "Intenta reiniciar el programa.")
)

@Composable
fun MessagingScreen(onContactClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(contacts) { contact ->
            ContactItem(
                contact = contact,
                onClick = { onContactClick(contact.name) }
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Last Message
        Column {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = contact.title, // Or contact.lastMessage for a real chat app
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagingScreenPreview() {
    MessagingScreen(onContactClick = {})
}
