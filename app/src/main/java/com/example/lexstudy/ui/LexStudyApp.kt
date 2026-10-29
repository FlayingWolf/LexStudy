package com.example.lexstudy.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.AuthRepository

@Composable
fun LexStudyApp() {
    val cuentas = remember { mutableStateListOf<Usuario>() }
    var pantallaAcceso by rememberSaveable { mutableStateOf("login") }
    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var seccion by rememberSaveable { mutableStateOf("inicio") }
    val apuntes = remember { mutableStateListOf<Apunte>() }
    val mapas = remember { mutableStateListOf<MapaConceptual>() }
    var fotoPerfil by remember { mutableStateOf<Bitmap?>(null) }
    val cerrarSesion: () -> Unit = {
        usuarioActivo = null
        pantallaAcceso = "login"
        seccion = "inicio"
        apuntes.clear()
        mapas.clear()
        fotoPerfil = null
    }

    if (usuarioActivo == null) {
        if (pantallaAcceso == "registro") {
            RegistroScreen(
                onRegistrar = { nombre, correo, clave ->
                    if (cuentas.any { it.correo.equals(correo.trim(), ignoreCase = true) }) "Ese correo ya está registrado."
                    else { cuentas.add(AuthRepository.crearUsuario(nombre, correo, clave)); null }
                },
                onIrALogin = { pantallaAcceso = "login" }
            )
        } else {
            LoginScreen(
                onLogin = { correo, clave -> AuthRepository.autenticar(cuentas, correo, clave) },
                onLoginCorrecto = { usuario -> usuarioActivo = usuario; seccion = "inicio" },
                onIrARegistro = { pantallaAcceso = "registro" }
            )
        }
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf("inicio" to "Inicio", "apuntes" to "Apuntes", "mapas" to "Mapas", "perfil" to "Perfil").forEach { (ruta, etiqueta) ->
                    NavigationBarItem(
                        selected = seccion == ruta,
                        onClick = { seccion = ruta },
                        icon = { Text(when (ruta) { "inicio" -> "⌂"; "apuntes" -> "▤"; "mapas" -> "◇"; else -> "○" }) },
                        label = { Text(etiqueta) }
                    )
                }
            }
        }
    ) { padding ->
        when (seccion) {
            "inicio" -> InicioScreen(usuarioActivo!!, onCerrarSesion = cerrarSesion, modifier = Modifier.padding(padding))
            "apuntes" -> ApuntesScreen(apuntes = apuntes, onGuardar = { apuntes.add(it) }, modifier = Modifier.padding(padding))
            "mapas" -> MapasConceptualesScreen(mapas = mapas, onGuardar = { mapas.add(it) }, modifier = Modifier.padding(padding))
            "perfil" -> PerfilScreen(
                usuario = usuarioActivo!!,
                fotoPerfil = fotoPerfil,
                onFotoTomada = { fotoPerfil = it },
                onCambiarClave = { claveActual, claveNueva, confirmacion ->
                    val usuario = usuarioActivo!!
                    when {
                        !AuthRepository.claveCorrecta(usuario, claveActual) -> "La contraseña actual no es correcta."
                        claveNueva.length < 6 -> "La nueva contraseña debe tener al menos 6 caracteres."
                        claveNueva != confirmacion -> "Las contraseñas nuevas no coinciden."
                        else -> {
                            val actualizado = AuthRepository.cambiarClave(usuario, claveNueva)
                            val indice = cuentas.indexOfFirst { it.correo.equals(usuario.correo, ignoreCase = true) }
                            if (indice >= 0) cuentas[indice] = actualizado
                            usuarioActivo = actualizado
                            null
                        }
                    }
                },
                onCerrarSesion = cerrarSesion,
                modifier = Modifier.padding(padding)
            )
            else -> InicioScreen(usuarioActivo!!, onCerrarSesion = cerrarSesion, modifier = Modifier.padding(padding))
        }
    }
}
