package com.example.lexstudy.model

/** Usuario local de la etapa 09. Esta etapa mantiene datos solo en memoria. */
data class Usuario(
    val id: Long = 0,
    val nombre: String,
    val correo: String,
    val saltClave: String,
    val hashClave: String,
    val fotoPerfilPath: String? = null
)
