package com.example.lexstudy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Mapa conceptual sencillo guardado de forma local para el MVP. */
@Entity(
    tableName = "mapas_conceptuales",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioId"])]
)
data class MapaConceptual(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val titulo: String,
    val conceptoCentral: String,
    val conceptoRelacionadoUno: String,
    val conceptoRelacionadoDos: String,
    val creadoEn: Long = System.currentTimeMillis()
)
