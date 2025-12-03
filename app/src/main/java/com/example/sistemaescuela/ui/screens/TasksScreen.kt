package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sistemaescuela.network.ProgresoTarea
import com.example.sistemaescuela.network.RetrofitClient
import com.example.sistemaescuela.network.TokenManager
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    materiaId: String,
    onTaskClick: (String, String) -> Unit,
    onViewDetailClick: (String, String, String?, String, Boolean, String?) -> Unit // <--- NUEVO PARÁMETRO
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    // Estados para la UI
    var listaTareas by remember { mutableStateOf<List<ProgresoTarea>>(emptyList()) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }
    val rolUsuario = tokenManager.getUserRole()


    // Carga inicial de datos
    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        val studentId = tokenManager.getStudentId()

        if (token != null && studentId != null) {
            try {
                val response = RetrofitClient.api.obtenerProgreso(
                    token = "Bearer $token",
                    materiaId = materiaId,
                    estudianteId = studentId
                )

                if (response.isSuccessful) {
                    listaTareas = response.body() ?: emptyList()
                } else {
                    mensajeError = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                mensajeError = "Fallo: ${e.message}"
            } finally {
                cargando = false
            }
        } else {
            mensajeError = "No hay sesión activa"
            cargando = false
        }
    }

    // --- UI ---
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Tareas") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            if (cargando) {
                CircularProgressIndicator()
            } else if (mensajeError != null) {
                Text(text = mensajeError!!, color = Color.Red)
            } else {
                if (listaTareas.isEmpty()) {
                    Text("No hay tareas registradas en esta materia.")
                } else {
                    LazyColumn {
                        items(listaTareas) { tarea ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = tarea.tarea, style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Vence: ${tarea.fechaVencimiento.take(10)}")

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (tarea.entrega != null) {
                                        // Tarea ya entregada
                                        Text(text = "Estado: ${tarea.entrega.estado}", color = Color.Blue)

                                        if (rolUsuario == "Padre de Familia" &&
                                            tarea.entrega.estado == "Entregada" &&
                                            tarea.requiereAval) {

                                            Spacer(modifier = Modifier.height(4.dp))
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.Info, null, tint = Color(0xFFEF6C00), modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Requiere tu aval", color = Color(0xFFE65100), style = MaterialTheme.typography.labelMedium)
                                                }
                                            }
                                        }
                                        if (tarea.entrega.calificacion != null) {
                                            Text(
                                                text = "Calificación: ${tarea.entrega.calificacion}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedButton(
                                            onClick = {
                                                onViewDetailClick(
                                                    tarea.tarea,
                                                    tarea.descripcion,
                                                    tarea.entrega.contenidoTexto,
                                                    tarea.entrega.estado,
                                                    tarea.requiereAval,
                                                    tarea.entrega.id
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            // Cambiamos el texto según la urgencia
                                            if (rolUsuario == "Padre de Familia" && tarea.entrega.estado == "Entregada" && tarea.requiereAval) {
                                                Text("Revisar y Avalar")
                                            } else {
                                                Text("Ver Detalles de Entrega")
                                            }
                                        }
                                    } else {
                                        // Tarea pendiente
                                        Text(text = "Estado: Pendiente", color = Color.Red)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = {

                                                onTaskClick(tarea.id, tarea.tarea)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Entregar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    StudentProfileScreen()
}
