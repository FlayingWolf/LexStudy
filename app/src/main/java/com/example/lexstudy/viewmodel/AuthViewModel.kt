package com.example.lexstudy.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: LexStudyRepository,
    private val sesionRepository: SesionRepository,
    private val fotoPerfilRepository: FotoPerfilRepository
) : ViewModel() {
    var usuarioActual: Usuario? by mutableStateOf(null)
        private set

    var estaCargando: Boolean by mutableStateOf(false)
        private set

    var estaRestaurandoSesion: Boolean by mutableStateOf(true)
        private set

    var mensajeError: String by mutableStateOf("")
        private set

    var estaGuardandoFotoPerfil: Boolean by mutableStateOf(false)
        private set

    var mensajeFotoPerfil: String by mutableStateOf("")
        private set

    var estaCambiandoContrasena: Boolean by mutableStateOf(false)
        private set

    var mensajeCambioContrasena: String by mutableStateOf("")
        private set

    var cambioContrasenaCorrecto: Boolean by mutableStateOf(false)
        private set

    private val correoInstitucionalRegex =
        Regex("^[A-Za-z0-9._%+-]+@(profesor\\.duoc\\.cl|duocuc\\.cl)$", RegexOption.IGNORE_CASE)

    init {
        restaurarSesionGuardada()
    }

    private fun restaurarSesionGuardada() {
        viewModelScope.launch {
            try {
                val usuarioId = sesionRepository.obtenerUsuarioIdActivo()
                if (usuarioId != null) {
                    val usuarioGuardado = repository.obtenerUsuarioPorId(usuarioId)
                    if (usuarioGuardado != null) {
                        usuarioActual = usuarioGuardado
                    } else {
                        sesionRepository.cerrarSesion()
                    }
                }
            } catch (_: Exception) {
                try {
                    sesionRepository.cerrarSesion()
                } catch (_: Exception) {
                }
            } finally {
                estaRestaurandoSesion = false
            }
        }
    }

    fun ingresar(correo: String, clave: String) {
        mensajeError = ""
        val correoNormalizado = correo.trim()
        if (!correoInstitucionalRegex.matches(correoNormalizado)) {
            mensajeError = "Ingresa un correo electrónico válido."
            return
        }
        if (clave.isBlank()) {
            mensajeError = "Ingresa tu contraseña."
            return
        }

        viewModelScope.launch {
            estaCargando = true
            try {
                val usuario = repository.autenticar(correoNormalizado, clave)
                if (usuario == null) {
                    mensajeError = "Correo o contraseña incorrectos."
                } else {
                    sesionRepository.guardarSesion(usuario.id)
                    usuarioActual = usuario
                }
            } catch (_: Exception) {
                mensajeError = "No se pudo iniciar sesión. Revisa los datos e inténtalo otra vez."
            } finally {
                estaCargando = false
            }
        }
    }

    fun registrar(nombre: String, correo: String, clave: String, confirmarClave: String) {
        mensajeError = ""
        val nombreNormalizado = nombre.trim()
        val correoNormalizado = correo.trim()

        if (nombreNormalizado.length < 2) {
            mensajeError = "Escribe tu nombre (al menos 2 caracteres)."
            return
        }
        if (!correoInstitucionalRegex.matches(correoNormalizado)) {
            mensajeError = "Ingresa un correo electrónico válido."
            return
        }
        if (clave.length < 6) {
            mensajeError = "La contraseña debe tener al menos 6 caracteres."
            return
        }
        if (clave != confirmarClave) {
            mensajeError = "Las contraseñas no coinciden."
            return
        }

        viewModelScope.launch {
            estaCargando = true
            try {
                val usuario = repository.registrarUsuario(nombreNormalizado, correoNormalizado, clave)
                if (usuario == null) {
                    mensajeError = "Ya existe una cuenta con ese correo."
                } else {
                    sesionRepository.guardarSesion(usuario.id)
                    usuarioActual = usuario
                }
            } catch (_: Exception) {
                mensajeError = "No se pudo crear la cuenta. Inténtalo nuevamente."
            } finally {
                estaCargando = false
            }
        }
    }

    fun limpiarErrorParaCambiarFormulario() {
        mensajeError = ""
    }

    fun actualizarFotoPerfil(bitmap: Bitmap) {
        val usuario = usuarioActual ?: return
        mensajeFotoPerfil = ""

        viewModelScope.launch {
            estaGuardandoFotoPerfil = true
            try {
                val usuarioActualizado = fotoPerfilRepository.guardarFotoPerfil(usuario.id, bitmap)
                if (usuarioActualizado != null) {
                    usuarioActual = usuarioActualizado
                    mensajeFotoPerfil = "Foto de perfil actualizada."
                } else {
                    mensajeFotoPerfil = "No se encontró el usuario para actualizar la foto."
                }
            } catch (_: Exception) {
                mensajeFotoPerfil = "No se pudo guardar la foto. Inténtalo nuevamente."
            } finally {
                estaGuardandoFotoPerfil = false
            }
        }
    }

    fun limpiarMensajeFotoPerfil() {
        mensajeFotoPerfil = ""
    }

    fun cambiarContrasena(claveActual: String, nuevaClave: String, confirmarClave: String) {
        mensajeCambioContrasena = ""
        cambioContrasenaCorrecto = false
        val usuario = usuarioActual

        if (usuario == null) {
            mensajeCambioContrasena = "No hay una sesión activa."
            return
        }
        if (claveActual.isBlank()) {
            mensajeCambioContrasena = "Ingresa tu contraseña actual."
            return
        }
        if (nuevaClave.length < 6) {
            mensajeCambioContrasena = "La nueva contraseña debe tener al menos 6 caracteres."
            return
        }
        if (nuevaClave != confirmarClave) {
            mensajeCambioContrasena = "La nueva contraseña y su confirmación no coinciden."
            return
        }
        if (claveActual == nuevaClave) {
            mensajeCambioContrasena = "La nueva contraseña debe ser distinta de la actual."
            return
        }

        viewModelScope.launch {
            estaCambiandoContrasena = true
            try {
                val actualizado = repository.cambiarContrasena(
                    usuarioId = usuario.id,
                    claveActual = claveActual,
                    nuevaClave = nuevaClave
                )
                if (actualizado) {
                    usuarioActual = repository.obtenerUsuarioPorId(usuario.id) ?: usuario
                    cambioContrasenaCorrecto = true
                    mensajeCambioContrasena = "Contraseña actualizada correctamente."
                } else {
                    mensajeCambioContrasena = "La contraseña actual es incorrecta."
                }
            } catch (_: Exception) {
                mensajeCambioContrasena = "No se pudo actualizar la contraseña. Inténtalo otra vez."
            } finally {
                estaCambiandoContrasena = false
            }
        }
    }

    fun limpiarMensajeCambioContrasena() {
        mensajeCambioContrasena = ""
        cambioContrasenaCorrecto = false
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            var errorAlCerrar = false
            try {
                sesionRepository.cerrarSesion()
            } catch (_: Exception) {
                errorAlCerrar = true
            } finally {
                usuarioActual = null
                mensajeError = if (errorAlCerrar) {
                    "No se pudo guardar el cierre de sesión. Vuelve a intentarlo."
                } else {
                    ""
                }
            }
        }
    }
}
