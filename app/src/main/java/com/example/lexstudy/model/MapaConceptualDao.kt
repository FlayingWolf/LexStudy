package com.example.lexstudy.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapaConceptualDao {
    @Query("SELECT * FROM mapas_conceptuales WHERE usuarioId = :usuarioId ORDER BY creadoEn DESC")
    fun observarPorUsuario(usuarioId: Long): Flow<List<MapaConceptual>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(mapa: MapaConceptual): Long

    @Query("DELETE FROM mapas_conceptuales WHERE id = :mapaId AND usuarioId = :usuarioId")
    suspend fun eliminar(mapaId: Long, usuarioId: Long)
}
