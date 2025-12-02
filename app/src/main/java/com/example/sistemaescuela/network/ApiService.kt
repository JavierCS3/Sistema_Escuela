package com.example.sistemaescuela.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Header

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- TAREAS ---

    // 1. Obtener el progreso (lista de tareas) de una materia
    @GET("tareas/progreso/materia/{materiaId}")
    suspend fun obtenerProgreso(
        @Header("Authorization") token: String, // ¡Aquí enviamos la llave!
        @Path("materiaId") materiaId: String,
        @Query("estudianteId") estudianteId: String
    ): Response<List<ProgresoTarea>>

    // 2. Entregar una tarea
    @POST("tareas/{tareaId}/entregar")
    suspend fun entregarTarea(
        @Header("Authorization") token: String,
        @Path("tareaId") tareaId: String,
        @Body entrega: EntregarTareaRequest
    ): Response<Void>
    @GET("inscripciones/mis-materias")
    suspend fun obtenerMisMaterias(
        @Header("Authorization") token: String
    ): Response<List<Materia>>
}