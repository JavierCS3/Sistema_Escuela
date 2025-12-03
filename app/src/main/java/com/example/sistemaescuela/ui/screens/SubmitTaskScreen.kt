package com.example.sistemaescuela.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sistemaescuela.network.EntregarTareaRequest
import com.example.sistemaescuela.network.RetrofitClient
import com.example.sistemaescuela.network.TokenManager
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Link

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitTaskScreen(
    tareaId: String,
    tituloTarea: String, // Pasamos el título para que se vea bonito
    onNavigateUp: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = TokenManager(context)

    var contenidoTexto by remember { mutableStateOf("") }
    var linkArchivo by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entregar Tarea") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = tituloTarea,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Tu respuesta o contenido:", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = contenidoTexto,
                onValueChange = { contenidoTexto = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Campo grande tipo área de texto
                placeholder = { Text("Escribe aquí el contenido de tu tarea...") },
                enabled = !enviando
            )

            Text("Link al Archivo (Opcional):", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = linkArchivo,
                onValueChange = { linkArchivo = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pega aquí el link de Drive, Docs, etc.") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (contenidoTexto.isBlank()) {
                        Toast.makeText(context, "Escribe algo antes de enviar", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    enviando = true
                    scope.launch {
                        val token = tokenManager.getToken()
                        if (token != null) {
                            try {
                                val response = RetrofitClient.api.entregarTarea(
                                    token = "Bearer $token",
                                    tareaId = tareaId,
                                    entrega = EntregarTareaRequest(
                                        contenidoTexto = contenidoTexto,
                                        archivoUrl = if (linkArchivo.isBlank()) null else linkArchivo
                                    )
                                )

                                if (response.isSuccessful || response.code() == 201) {
                                    Toast.makeText(context, "¡Tarea entregada con éxito!", Toast.LENGTH_LONG).show()
                                    onSuccess() // Volver atrás y recargar
                                } else {
                                    Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                enviando = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !enviando
            ) {
                if (enviando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Text("ENVIAR ENTREGA")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SubmitTaskScreenPreview() {
    StudentProfileScreen()
}
