package com.example.sistemaescuela.network



// Para recibir la lista de progreso/tareas
data class ProgresoTarea(
    val tarea: String,           // Título de la tarea
    val fechaVencimiento: String,
    val requiereAval: Boolean,
    val entrega: EntregaInfo?    // Puede ser null si no ha entregado
)

data class EntregaInfo(
    val estado: String,          // "Entregada", "Avalada", "Calificada"
    val calificacion: Double?,
    val comentarios: String?,
    val fechaEntregada: String?
)

// Para enviar una entrega (Subir tarea)
data class EntregarTareaRequest(
    val contenidoTexto: String,
    val archivoUrl: String? = null
)