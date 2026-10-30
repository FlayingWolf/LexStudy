package com.example.lexstudy.repository

/** Sesión temporal de la etapa 09. No se guarda en almacenamiento persistente. */
class SesionRepository {
    @Volatile private var usuarioIdActivo: Long? = null

    suspend fun guardarSesion(usuarioId: Long) {
        usuarioIdActivo = usuarioId
    }

    suspend fun obtenerUsuarioIdActivo(): Long? = usuarioIdActivo

    suspend fun cerrarSesion() {
        usuarioIdActivo = null
    }
}
