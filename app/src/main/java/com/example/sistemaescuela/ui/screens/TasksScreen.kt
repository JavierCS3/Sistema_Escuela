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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    materiaId: String //
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = TokenManager(context)

    // Estados para la UI
    var listaTareas by remember { mutableStateOf<List<ProgresoTarea>>(emptyList()) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }



    // Efecto que se ejecuta al iniciar la pantalla
    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        val studentId = tokenManager.getStudentId()

        if (token != null && studentId != null) {
            try {
                // Llamada al Backend
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
                // Lista de Tareas
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

                                // Mostrar estado
                                if (tarea.entrega != null) {
                                    Text(text = "Estado: ${tarea.entrega.estado}", color = Color.Blue)
                                    if (tarea.entrega.calificacion != null) {
                                        Text(text = "Calificación: ${tarea.entrega.calificacion}", style = MaterialTheme.typography.bodyLarge)
                                    }
                                } else {
                                    Text(text = "Estado: Pendiente", color = Color.Red)
                                    Button(onClick = { /* Navegar a pantalla de subir */ }) {
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
