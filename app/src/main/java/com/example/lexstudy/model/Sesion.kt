package com.example.lexstudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Registro único de la sesión activa, almacenado en Room. */
@Entity(tableName = "sesion_activa")
data class Sesion(
    @PrimaryKey val id: Int = 1,
    val usuarioId: Long
)
