package com.example.lexstudy.model

data class Ley(
    val id: String,
    val nombre: String,
    val articulo: String,
    val materia: String,
    val resumen: String,
    val referenciaOficial: String,
    val ultimaReferencia: String,
    val esVigenteEnCatalogo: Boolean = true
)
