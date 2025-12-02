package com.example.sistemaescuela.network

data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que el Backend nos responde
data class LoginResponse(
    val message: String,
    val token: String,
    val usuario: UsuarioData
)

data class UsuarioData(
    val id: String,
    val nombre: String,
    val rol: String,
    val estudianteId: String? = null
)