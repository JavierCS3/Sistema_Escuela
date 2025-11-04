package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessagingScreen(onContactClick: (String) -> Unit) {
    // Sample list of contacts
    val contacts = listOf("Juan Pérez", "María García", "Carlos López", "Ana Martínez")

    LazyColumn {
        items(contacts) { contact ->
            ContactItem(
                name = contact,
                onClick = { onContactClick(contact) }
            )
        }
    }
}

@Composable
fun ContactItem(name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        // You could add a profile picture here
        Column {
            Text(text = name)
            // You could add last message preview here
        }
    }
}
