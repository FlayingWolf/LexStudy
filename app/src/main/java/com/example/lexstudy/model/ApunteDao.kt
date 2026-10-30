package com.example.lexstudy.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ApunteDao {
    @Query("SELECT * FROM apuntes WHERE usuarioId = :usuarioId ORDER BY actualizadoEn DESC")
    fun observarPorUsuario(usuarioId: Long): Flow<List<Apunte>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(apunte: Apunte): Long

    @Update
    suspend fun actualizar(apunte: Apunte)

    @Query("DELETE FROM apuntes WHERE id = :apunteId AND usuarioId = :usuarioId")
    suspend fun eliminar(apunteId: Long, usuarioId: Long)
}
