package com.example.lexstudy.repository

import com.example.lexstudy.model.Usuario
import java.security.MessageDigest
import java.util.UUID

/** Autenticación local solo para estos prototipos; no sustituye un backend real. */
object AuthRepository {
    fun crearUsuario(nombre: String, correo: String, clave: String): Usuario {
        val salt = UUID.randomUUID().toString()
        return Usuario(
            nombre = nombre.trim(),
            correo = correo.trim().lowercase(),
            saltClave = salt,
            hashClave = hash(clave, salt)
        )
    }

    fun autenticar(usuarios: List<Usuario>, correo: String, clave: String): Usuario? {
        val usuario = usuarios.firstOrNull { it.correo.equals(correo.trim(), ignoreCase = true) }
            ?: return null
        return usuario.takeIf { hash(clave, it.saltClave) == it.hashClave }
    }

    fun cambiarClave(usuario: Usuario, nuevaClave: String): Usuario {
        val nuevoSalt = UUID.randomUUID().toString()
        return usuario.copy(saltClave = nuevoSalt, hashClave = hash(nuevaClave, nuevoSalt))
    }

    fun claveCorrecta(usuario: Usuario, clave: String): Boolean =
        hash(clave, usuario.saltClave) == usuario.hashClave

    private fun hash(clave: String, salt: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest("$salt:$clave".toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
