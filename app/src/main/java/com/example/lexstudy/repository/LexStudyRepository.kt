package com.example.lexstudy.repository

import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.ApunteDao
import com.example.lexstudy.model.Ley
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.model.MapaConceptualDao
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.model.UsuarioDao
import java.security.MessageDigest
import java.security.SecureRandom
import kotlinx.coroutines.flow.Flow

/** Coordina el acceso a Room y expone operaciones para las capas ViewModel. */
class LexStudyRepository(
    private val usuarioDao: UsuarioDao,
    private val apunteDao: ApunteDao,
    private val mapaDao: MapaConceptualDao
) {
    suspend fun registrarUsuario(nombre: String, correo: String, clave: String): Usuario? {
        if (usuarioDao.buscarPorCorreo(correo) != null) return null

        val salt = generarSalt()
        val usuarioSinId = Usuario(
            nombre = nombre.trim(),
            correo = correo.trim().lowercase(),
            saltClave = salt,
            hashClave = calcularHash(clave, salt)
        )
        val id = usuarioDao.insertar(usuarioSinId)
        return usuarioSinId.copy(id = id)
    }

    suspend fun obtenerUsuarioPorId(usuarioId: Long): Usuario? =
        usuarioDao.buscarPorId(usuarioId)

    suspend fun autenticar(correo: String, clave: String): Usuario? {
        val usuario = usuarioDao.buscarPorCorreo(correo.trim().lowercase()) ?: return null
        val hashIngresado = calcularHash(clave, usuario.saltClave)
        return usuario.takeIf { hashIngresado == it.hashClave }
    }

    /** Verifica la contraseña actual y reemplaza sus credenciales usando un salt nuevo. */
    suspend fun cambiarContrasena(
        usuarioId: Long,
        claveActual: String,
        nuevaClave: String
    ): Boolean {
        val usuario = usuarioDao.buscarPorId(usuarioId) ?: return false
        val hashActual = calcularHash(claveActual, usuario.saltClave)
        if (hashActual != usuario.hashClave) return false

        val nuevoSalt = generarSalt()
        val nuevoHash = calcularHash(nuevaClave, nuevoSalt)
        usuarioDao.actualizarCredenciales(usuarioId, nuevoSalt, nuevoHash)
        return true
    }

    fun observarApuntes(usuarioId: Long): Flow<List<Apunte>> =
        apunteDao.observarPorUsuario(usuarioId)

    suspend fun guardarApunte(
        usuarioId: Long,
        ley: Ley,
        titulo: String,
        contenido: String,
        archivoPdfUri: String?,
        apunteId: Long? = null
    ) {
        val apunte = Apunte(
            id = apunteId ?: 0,
            usuarioId = usuarioId,
            leyId = ley.id,
            nombreLey = ley.nombre,
            titulo = titulo.trim(),
            contenido = contenido.trim(),
            archivoPdfUri = archivoPdfUri,
            actualizadoEn = System.currentTimeMillis()
        )
        if (apunteId == null) apunteDao.insertar(apunte) else apunteDao.actualizar(apunte)
    }

    suspend fun eliminarApunte(apunteId: Long, usuarioId: Long) {
        apunteDao.eliminar(apunteId, usuarioId)
    }

    fun observarMapas(usuarioId: Long): Flow<List<MapaConceptual>> =
        mapaDao.observarPorUsuario(usuarioId)

    suspend fun guardarMapa(
        usuarioId: Long,
        titulo: String,
        conceptoCentral: String,
        conceptoUno: String,
        conceptoDos: String
    ) {
        mapaDao.insertar(
            MapaConceptual(
                usuarioId = usuarioId,
                titulo = titulo.trim(),
                conceptoCentral = conceptoCentral.trim(),
                conceptoRelacionadoUno = conceptoUno.trim(),
                conceptoRelacionadoDos = conceptoDos.trim()
            )
        )
    }

    suspend fun eliminarMapa(mapaId: Long, usuarioId: Long) {
        mapaDao.eliminar(mapaId, usuarioId)
    }

    private fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    private fun calcularHash(clave: String, salt: String): String {
        val datos = "$salt:$clave".toByteArray(Charsets.UTF_8)
        val resultado = MessageDigest.getInstance("SHA-256").digest(datos)
        return resultado.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
