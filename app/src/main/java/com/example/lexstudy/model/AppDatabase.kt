package com.example.lexstudy.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class, Apunte::class, MapaConceptual::class, Sesion::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun apunteDao(): ApunteDao
    abstract fun mapaConceptualDao(): MapaConceptualDao
    abstract fun sesionDao(): SesionDao
}
