package com.example.sistemaescuela.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler // <--- Importante para abrir links
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sistemaescuela.network.RetrofitClient
import com.example.sistemaescuela.network.TokenManager
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    titulo: String,
    descripcion: String,
    contenidoEntrega: String,
    estado: String,
    requiereAval: Boolean,
    entregaId: String?,
    archivoUrl: String?, // <--- Nuevo parámetro para el link
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = TokenManager(context)
    val rolUsuario = tokenManager.getUserRole()
    val uriHandler = LocalUriHandler.current // <--- Handler para abrir el navegador

    // --- 1. Decodificación de Datos ---
    val decodedDesc = remember {
        URLDecoder.decode(descripcion, StandardCharsets.UTF_8.toString())
    }

    val decodedContent = remember {
        val texto = URLDecoder.decode(contenidoEntrega, StandardCharsets.UTF_8.toString())
        if (texto == "SIN_CONTENIDO") "" else texto
    }

    // Aquí definimos la variable que te faltaba
    val decodedUrl = remember(archivoUrl) {
        if (archivoUrl == "SIN_URL" || archivoUrl == null || archivoUrl == "null") {
            null
        } else {
            val urlLimpia = URLDecoder.decode(archivoUrl, StandardCharsets.UTF_8.toString())
            // Si el usuario olvidó poner https://, se lo ponemos nosotros
            if (!urlLimpia.startsWith("http://") && !urlLimpia.startsWith("https://")) {
                "https://$urlLimpia"
            } else {
                urlLimpia
            }
        }
    }
    // ----------------------------------

    var estadoActual by remember { mutableStateOf(estado) }
    var procesando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
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
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // --- Encabezado ---
            Text(text = titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            SuggestionChip(
                onClick = {},
                label = { Text(estadoActual) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = if (estadoActual == "Avalada") Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant
                ),
                icon = {
                    if (estadoActual == "Avalada") Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32))
                }
            )

            if (requiereAval && estadoActual == "Entregada") {
                Spacer(modifier = Modifier.height(8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = Color(0xFFEF6C00))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Esta tarea requiere tu aval para ser calificada.", color = Color(0xFFE65100))
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // --- Instrucciones ---
            Text("Instrucciones:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = decodedDesc, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Trabajo del Alumno ---
            Text("Trabajo del Estudiante:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            // 1. Contenido de Texto
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = if (decodedContent.isBlank()) "Sin contenido de texto." else decodedContent,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 2. Link / Archivo Adjunto (Aquí usamos la variable)
            if (decodedUrl != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        try {
                            uriHandler.openUri(decodedUrl) // Abre el navegador
                        } catch (e: Exception) {
                            Toast.makeText(context, "No se pudo abrir el enlace: $decodedUrl", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Archivo Adjunto / Link")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Botón Avalar (Solo Padre) ---
            if (rolUsuario == "Padre de Familia" && estadoActual == "Entregada" && requiereAval && entregaId != null) {
                Button(
                    onClick = {
                        procesando = true
                        scope.launch {
                            val token = tokenManager.getToken()
                            if (token != null) {
                                try {
                                    val response = RetrofitClient.api.avalarTarea(
                                        token = "Bearer $token",
                                        entregaId = entregaId
                                    )
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "¡Tarea Avalada con éxito!", Toast.LENGTH_SHORT).show()
                                        estadoActual = "Avalada"
                                    } else {
                                        Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                                } finally {
                                    procesando = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    enabled = !procesando
                ) {
                    if (procesando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AVALAR TAREA")
                    }
                }
            }
        }
    }
}