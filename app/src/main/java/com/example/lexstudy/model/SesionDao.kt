package com.example.lexstudy.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Operaciones para guardar o recuperar la sesión activa con Room. */
@Dao
interface SesionDao {
    @Query("SELECT * FROM sesion_activa WHERE id = 1 LIMIT 1")
    suspend fun obtenerSesionActiva(): Sesion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSesion(sesion: Sesion)

    @Query("DELETE FROM sesion_activa")
    suspend fun limpiarSesion()
}
