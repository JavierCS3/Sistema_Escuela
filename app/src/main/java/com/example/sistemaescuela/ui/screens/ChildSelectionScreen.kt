package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sistemaescuela.network.HijoVinculo
import com.example.sistemaescuela.network.TokenManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildSelectionScreen(
    onChildSelected: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    // Recuperamos la lista de hijos que guardaremos en el Login (como JSON)
    val hijosJson = tokenManager.getChildrenListJson()
    val listaHijos: List<HijoVinculo> = remember {
        try {
            val type = object : TypeToken<List<HijoVinculo>>() {}.type
            Gson().fromJson(hijosJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Selecciona a tu hijo") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenido, ${tokenManager.getUserName()}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("¿A quién deseas consultar hoy?", color = MaterialTheme.colorScheme.secondary)

            Spacer(modifier = Modifier.height(24.dp))

            if (listaHijos.isEmpty()) {
                Text("No tienes estudiantes asociados.")
            } else {
                LazyColumn {
                    items(listaHijos) { vinculo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    // --- AQUÍ ESTÁ LA MAGIA ---
                                    // 1. Guardamos el ID del hijo seleccionado como el "student_id" actual
                                    tokenManager.saveSelectedStudentId(vinculo.hijo._id)
                                    // 2. Avanzamos al menú
                                    onChildSelected()
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Face,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "${vinculo.hijo.usuario.nombre} ${vinculo.hijo.usuario.apellido}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = "Matrícula: ${vinculo.hijo.matricula}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}