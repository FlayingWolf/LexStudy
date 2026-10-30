package com.example.lexstudy.repository

import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.Ley
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.model.Usuario
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Repositorio en memoria para la etapa 09.
 * La interfaz y las operaciones son las del proyecto final, pero no se utiliza Room.
 * Los registros se reinician al cerrar el proceso de la aplicación.
 */
class LexStudyRepository {
    private val lock = Any()
    private val siguienteUsuarioId = AtomicLong(1)
    private val siguienteApunteId = AtomicLong(1)
    private val siguienteMapaId = AtomicLong(1)
    private val usuarios = linkedMapOf<Long, Usuario>()
    private val apuntes = mutableMapOf<Long, MutableStateFlow<List<Apunte>>>()
    private val mapas = mutableMapOf<Long, MutableStateFlow<List<MapaConceptual>>>()

    suspend fun registrarUsuario(nombre: String, correo: String, clave: String): Usuario? = synchronized(lock) {
        val correoNormalizado = correo.trim().lowercase()
        if (usuarios.values.any { it.correo.equals(correoNormalizado, ignoreCase = true) }) return@synchronized null
        val salt = generarSalt()
        val usuario = Usuario(
            id = siguienteUsuarioId.getAndIncrement(),
            nombre = nombre.trim(),
            correo = correoNormalizado,
            saltClave = salt,
            hashClave = calcularHash(clave, salt)
        )
        usuarios[usuario.id] = usuario
        usuario
    }

    suspend fun obtenerUsuarioPorId(usuarioId: Long): Usuario? = synchronized(lock) { usuarios[usuarioId] }

    suspend fun autenticar(correo: String, clave: String): Usuario? = synchronized(lock) {
        val usuario = usuarios.values.firstOrNull { it.correo.equals(correo.trim(), ignoreCase = true) } ?: return@synchronized null
        val hashIngresado = calcularHash(clave, usuario.saltClave)
        usuario.takeIf { hashIngresado == it.hashClave }
    }

    suspend fun cambiarContrasena(usuarioId: Long, claveActual: String, nuevaClave: String): Boolean = synchronized(lock) {
        val usuario = usuarios[usuarioId] ?: return@synchronized false
        if (calcularHash(claveActual, usuario.saltClave) != usuario.hashClave) return@synchronized false
        val nuevoSalt = generarSalt()
        val actualizado = usuario.copy(saltClave = nuevoSalt, hashClave = calcularHash(nuevaClave, nuevoSalt))
        usuarios[usuarioId] = actualizado
        true
    }

    suspend fun actualizarFotoPerfilPath(usuarioId: Long, ruta: String?): Usuario? = synchronized(lock) {
        val usuario = usuarios[usuarioId] ?: return@synchronized null
        val actualizado = usuario.copy(fotoPerfilPath = ruta)
        usuarios[usuarioId] = actualizado
        actualizado
    }

    fun observarApuntes(usuarioId: Long): Flow<List<Apunte>> = synchronized(lock) {
        apuntes.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }
    }

    suspend fun guardarApunte(
        usuarioId: Long,
        ley: Ley,
        titulo: String,
        contenido: String,
        archivoPdfUri: String?,
        apunteId: Long? = null
    ) {
        synchronized(lock) {
            val flujo = apuntes.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }
            val id = apunteId ?: siguienteApunteId.getAndIncrement()
            val apunte = Apunte(
                id = id,
                usuarioId = usuarioId,
                leyId = ley.id,
                nombreLey = ley.nombre,
                titulo = titulo.trim(),
                contenido = contenido.trim(),
                archivoPdfUri = archivoPdfUri,
                actualizadoEn = System.currentTimeMillis()
            )
            flujo.value = (flujo.value.filterNot { it.id == id } + apunte).sortedByDescending { it.actualizadoEn }
        }
    }

    suspend fun eliminarApunte(apunteId: Long, usuarioId: Long) {
        synchronized(lock) {
            apuntes.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }.let { flujo ->
                flujo.value = flujo.value.filterNot { it.id == apunteId && it.usuarioId == usuarioId }
            }
        }
    }

    fun observarMapas(usuarioId: Long): Flow<List<MapaConceptual>> = synchronized(lock) {
        mapas.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }
    }

    suspend fun guardarMapa(
        usuarioId: Long,
        titulo: String,
        conceptoCentral: String,
        conceptoUno: String,
        conceptoDos: String
    ) {
        synchronized(lock) {
            val flujo = mapas.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }
            val mapa = MapaConceptual(
                id = siguienteMapaId.getAndIncrement(),
                usuarioId = usuarioId,
                titulo = titulo.trim(),
                conceptoCentral = conceptoCentral.trim(),
                conceptoRelacionadoUno = conceptoUno.trim(),
                conceptoRelacionadoDos = conceptoDos.trim()
            )
            flujo.value = listOf(mapa) + flujo.value
        }
    }

    suspend fun eliminarMapa(mapaId: Long, usuarioId: Long) {
        synchronized(lock) {
            mapas.getOrPut(usuarioId) { MutableStateFlow(emptyList()) }.let { flujo ->
                flujo.value = flujo.value.filterNot { it.id == mapaId && it.usuarioId == usuarioId }
            }
        }
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
