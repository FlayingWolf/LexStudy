package com.example.lexstudy.model

import java.util.UUID

data class Apunte(
    val id: String = UUID.randomUUID().toString(),
    val usuarioCorreo: String,
    val leyId: String,
    val nombreLey: String,
    val titulo: String,
    val contenido: String,
    val archivoPdfUri: String? = null,
    val actualizadoEn: Long = System.currentTimeMillis()
)
