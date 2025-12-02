package com.example.sistemaescuela.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
// Asegúrate de importar tus clases de red correctamente:
import com.example.sistemaescuela.network.RetrofitClient
import com.example.sistemaescuela.network.LoginRequest

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit, // (Opcional: Puedes quitar esto si la navegación la haces aquí directo)
    onActivateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    // 1. ¡AQUÍ VAN LAS VARIABLES! (Dentro de la función)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Tus variables de estado
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.School, contentDescription = "App Logo", modifier = Modifier.size(100.dp))
        Text("Sistema Escuela", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario / Correo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username Icon") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Lógica del Botón Conectada
                Button(
                    onClick = {
                        // Lanzamos la corrutina
                        coroutineScope.launch {
                            try {
                                // Hacemos la petición
                                // CORRECCIÓN: Usamos 'username' que es tu variable, y lo mandamos como 'correo'
                                val response = RetrofitClient.api.login(
                                    LoginRequest(email = username , password)
                                )

                                if (response.isSuccessful) {
                                    val data = response.body()
                                    val nombre = data?.usuario?.nombre

                                    Toast.makeText(context, "Bienvenido $nombre", Toast.LENGTH_LONG).show()

                                    // Si tu navegación la maneja el padre, llamas a onLoginClick
                                    onLoginClick()
                                } else {
                                    Toast.makeText(context, "Error: Credenciales inválidas", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fallo de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                                e.printStackTrace()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("INICIAR SESIÓN")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(onClick = onActivateAccountClick) {
                Text("Activar cuenta")
            }
            TextButton(onClick = onForgotPasswordClick) {
                Text("¿Olvidé contraseña?")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginClick = {}, onActivateAccountClick = {}, onForgotPasswordClick = {})
}