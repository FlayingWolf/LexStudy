package com.example.lexstudy.model

/** Cuenta de demostración. Las contraseñas se guardan como hash con salt en memoria. */
data class Usuario(
    val nombre: String,
    val correo: String,
    val saltClave: String,
    val hashClave: String
)
