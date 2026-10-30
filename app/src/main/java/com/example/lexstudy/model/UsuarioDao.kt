package com.example.lexstudy.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId LIMIT 1")
    suspend fun buscarPorId(usuarioId: Long): Usuario?

    @Query("UPDATE usuarios SET fotoPerfilPath = :rutaFoto WHERE id = :usuarioId")
    suspend fun actualizarFotoPerfil(usuarioId: Long, rutaFoto: String)

    /** Actualiza el salt y el hash, nunca almacena la contraseña en texto plano. */
    @Query("UPDATE usuarios SET saltClave = :saltClave, hashClave = :hashClave WHERE id = :usuarioId")
    suspend fun actualizarCredenciales(usuarioId: Long, saltClave: String, hashClave: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long
}
