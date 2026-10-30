package com.example.lexstudy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Apunte privado de un usuario, asociado a una norma del catálogo. */
@Entity(
    tableName = "apuntes",
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
data class Apunte(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val leyId: String,
    val nombreLey: String,
    val titulo: String,
    val contenido: String,
    val archivoPdfUri: String? = null,
    val actualizadoEn: Long = System.currentTimeMillis()
)
