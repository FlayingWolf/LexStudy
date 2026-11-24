package com.example.lexstudy.presentation

import android.graphics.Bitmap
import com.example.lexstudy.model.Validaciones
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class AuthAcciones(
    private val repository: LexStudyRepository,
    private val sesionRepository: SesionRepository,
    private val fotoPerfilRepository: FotoPerfilRepository,
    private val estadoPantalla: AuthEstado,
    private val alcance: CoroutineScope
) {
    init {
        restaurarSesionGuardada()
    }

    private fun restaurarSesionGuardada() {
        alcance.launch {
            try {
                val usuarioId = sesionRepository.obtenerUsuarioIdActivo()
                if (usuarioId != null) {
                    val usuarioGuardado = repository.obtenerUsuarioPorId(usuarioId)
                    if (usuarioGuardado != null) {
                        estadoPantalla.usuarioActual = usuarioGuardado
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
                estadoPantalla.estaRestaurandoSesion = false
            }
        }
    }

    fun ingresar(correo: String, clave: String) {
        estadoPantalla.mensajeError = ""
        val correoNormalizado = correo.trim()
        Validaciones.ingreso(correoNormalizado, clave)?.let {
            estadoPantalla.mensajeError = it
            return
        }

        alcance.launch {
            estadoPantalla.estaCargando = true
            try {
                val usuario = repository.autenticar(correoNormalizado, clave)
                if (usuario == null) {
                    estadoPantalla.mensajeError = "Correo o contraseña incorrectos."
                } else {
                    sesionRepository.guardarSesion(usuario.id)
                    estadoPantalla.usuarioActual = usuario
                }
            } catch (_: Exception) {
                estadoPantalla.mensajeError = "No se pudo iniciar sesión. Revisa los datos e inténtalo otra vez."
            } finally {
                estadoPantalla.estaCargando = false
            }
        }
    }

    fun registrar(nombre: String, correo: String, clave: String, confirmarClave: String) {
        estadoPantalla.mensajeError = ""
        val nombreNormalizado = nombre.trim()
        val correoNormalizado = correo.trim()

        Validaciones.registro(nombreNormalizado, correoNormalizado, clave, confirmarClave)?.let {
            estadoPantalla.mensajeError = it
            return
        }

        alcance.launch {
            estadoPantalla.estaCargando = true
            try {
                val usuario = repository.registrarUsuario(nombreNormalizado, correoNormalizado, clave)
                if (usuario == null) {
                    estadoPantalla.mensajeError = "Ya existe una cuenta con ese correo."
                } else {
                    sesionRepository.guardarSesion(usuario.id)
                    estadoPantalla.usuarioActual = usuario
                }
            } catch (_: Exception) {
                estadoPantalla.mensajeError = "No se pudo crear la cuenta. Inténtalo nuevamente."
            } finally {
                estadoPantalla.estaCargando = false
            }
        }
    }

    fun limpiarErrorParaCambiarFormulario() {
        estadoPantalla.mensajeError = ""
    }

    fun actualizarFotoPerfil(bitmap: Bitmap) {
        val usuario = estadoPantalla.usuarioActual ?: return
        estadoPantalla.mensajeFotoPerfil = ""

        alcance.launch {
            estadoPantalla.estaGuardandoFotoPerfil = true
            try {
                val usuarioActualizado = fotoPerfilRepository.guardarFotoPerfil(usuario.id, bitmap)
                if (usuarioActualizado != null) {
                    estadoPantalla.usuarioActual = usuarioActualizado
                    estadoPantalla.mensajeFotoPerfil = "Foto de perfil actualizada."
                } else {
                    estadoPantalla.mensajeFotoPerfil = "No se encontró el usuario para actualizar la foto."
                }
            } catch (_: Exception) {
                estadoPantalla.mensajeFotoPerfil = "No se pudo guardar la foto. Inténtalo nuevamente."
            } finally {
                estadoPantalla.estaGuardandoFotoPerfil = false
            }
        }
    }

    fun limpiarMensajeFotoPerfil() {
        estadoPantalla.mensajeFotoPerfil = ""
    }

    fun cambiarContrasena(claveActual: String, nuevaClave: String, confirmarClave: String) {
        estadoPantalla.mensajeCambioContrasena = ""
        estadoPantalla.cambioContrasenaCorrecto = false
        val usuario = estadoPantalla.usuarioActual

        if (usuario == null) {
            estadoPantalla.mensajeCambioContrasena = "No hay una sesión activa."
            return
        }
        Validaciones.cambioContrasena(claveActual, nuevaClave, confirmarClave)?.let {
            estadoPantalla.mensajeCambioContrasena = it
            return
        }

        alcance.launch {
            estadoPantalla.estaCambiandoContrasena = true
            try {
                val actualizado = repository.cambiarContrasena(
                    usuarioId = usuario.id,
                    claveActual = claveActual,
                    nuevaClave = nuevaClave
                )
                if (actualizado) {
                    estadoPantalla.usuarioActual = repository.obtenerUsuarioPorId(usuario.id) ?: usuario
                    estadoPantalla.cambioContrasenaCorrecto = true
                    estadoPantalla.mensajeCambioContrasena = "Contraseña actualizada correctamente."
                } else {
                    estadoPantalla.mensajeCambioContrasena = "La contraseña actual es incorrecta."
                }
            } catch (_: Exception) {
                estadoPantalla.mensajeCambioContrasena = "No se pudo actualizar la contraseña. Inténtalo otra vez."
            } finally {
                estadoPantalla.estaCambiandoContrasena = false
            }
        }
    }

    fun limpiarMensajeCambioContrasena() {
        estadoPantalla.mensajeCambioContrasena = ""
        estadoPantalla.cambioContrasenaCorrecto = false
    }

    fun cerrarSesion() {
        alcance.launch {
            var errorAlCerrar = false
            try {
                sesionRepository.cerrarSesion()
            } catch (_: Exception) {
                errorAlCerrar = true
            } finally {
                estadoPantalla.usuarioActual = null
                estadoPantalla.mensajeError = if (errorAlCerrar) {
                    "No se pudo guardar el cierre de sesión. Vuelve a intentarlo."
                } else {
                    ""
                }
            }
        }
    }
}
