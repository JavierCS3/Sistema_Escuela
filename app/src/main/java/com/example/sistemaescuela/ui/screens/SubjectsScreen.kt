package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sistemaescuela.network.Materia
import com.example.sistemaescuela.network.RetrofitClient
import com.example.sistemaescuela.network.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    onMateriaClick: (String) -> Unit
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    var listaMaterias by remember { mutableStateOf<List<Materia>>(emptyList()) }

    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        if (token != null) {
            try {
                val response = RetrofitClient.api.obtenerMisMaterias("Bearer $token")
                if (response.isSuccessful) {
                    listaMaterias = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Nota: No usamos Scaffold aquí porque MainMenuScreen ya tiene uno
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Selecciona una materia", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
        }
        items(listaMaterias) { materia ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onMateriaClick(materia.id) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = materia.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Prof. ${materia.maestro}")
                }
            }
        }
    }
}