package com.example.lexstudy.model

import java.util.UUID

data class MapaConceptual(
    val titulo: String,
    val conceptoCentral: String,
    val conceptoRelacionadoUno: String,
    val conceptoRelacionadoDos: String,
    val id: String = UUID.randomUUID().toString()
)
