package com.example.lexstudy.repository

import com.example.lexstudy.model.Sesion
import com.example.lexstudy.model.SesionDao

/**
 * Repositorio de sesión siguiendo el patrón del proyecto base del profesor:
 * DAO -> Repository -> ViewModel.
 */
class SesionRepository(private val sesionDao: SesionDao) {
    suspend fun guardarSesion(usuarioId: Long) {
        sesionDao.guardarSesion(Sesion(usuarioId = usuarioId))
    }

    suspend fun obtenerUsuarioIdActivo(): Long? =
        sesionDao.obtenerSesionActiva()?.usuarioId

    suspend fun cerrarSesion() {
        sesionDao.limpiarSesion()
    }
}
