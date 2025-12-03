package com.example.sistemaescuela.network

data class LoginRequest(
    val email: String,
    val password: String,

)


data class LoginResponse(
    val message: String,
    val token: String,
    val usuario: UsuarioData
)

data class UsuarioData(
    val matricula: String? = null,
    val id: String,
    val nombre: String,
    val rol: String,
    val estudianteId: String? = null,
    val hijos: List<HijoVinculo>? = null
)
data class HijoVinculo(
    val _id: String, // ID del vínculo
    val hijo: InfoHijo
)

data class InfoHijo(
    val _id: String, // ESTE ES EL ID DEL ESTUDIANTE (El que necesitamos)
    val matricula: String,
    val usuario: InfoUsuarioHijo
)

data class InfoUsuarioHijo(
    val nombre: String,
    val apellido: String
)