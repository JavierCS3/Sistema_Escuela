package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    contactName: String,
    onNavigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Placeholder for chat messages
        Text(text = "Aquí irán los mensajes con $contactName.")
    }
}
