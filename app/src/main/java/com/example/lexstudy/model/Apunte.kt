package com.example.lexstudy.model

/** Apunte de estudio asociado a un usuario; en esta etapa vive solo en memoria. */
data class Apunte(
    val id: Long = 0,
    val usuarioId: Long,
    val leyId: String,
    val nombreLey: String,
    val titulo: String,
    val contenido: String,
    val archivoPdfUri: String? = null,
    val actualizadoEn: Long = System.currentTimeMillis()
)
