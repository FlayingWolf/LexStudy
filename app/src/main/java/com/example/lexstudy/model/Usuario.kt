package com.example.lexstudy.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Usuario local de LexStudy. La contraseña nunca se almacena en texto plano. */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val saltClave: String,
    val hashClave: String,
    /** Ruta privada en el almacenamiento interno de la foto de perfil, si existe. */
    val fotoPerfilPath: String? = null
)
