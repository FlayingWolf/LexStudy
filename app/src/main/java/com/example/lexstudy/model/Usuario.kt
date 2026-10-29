package com.example.lexstudy.model

data class Usuario(
    val nombre: String,
    val correo: String,
    val saltClave: String,
    val hashClave: String
)
