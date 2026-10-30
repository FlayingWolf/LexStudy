package com.example.lexstudy.model

/** Mapa conceptual de la etapa 09; no se almacena de forma persistente. */
data class MapaConceptual(
    val id: Long = 0,
    val usuarioId: Long,
    val titulo: String,
    val conceptoCentral: String,
    val conceptoRelacionadoUno: String,
    val conceptoRelacionadoDos: String,
    val creadoEn: Long = System.currentTimeMillis()
)
