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
import com.example.lexstudy.model.Ley
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.AuthRepository
import com.example.lexstudy.repository.LeyesDemo
import com.example.lexstudy.ui.theme.LexStudyTheme

@Composable
fun LexStudyApp() {
    val cuentas = remember { mutableStateListOf<Usuario>() }
    var pantallaAcceso by rememberSaveable { mutableStateOf("login") }
    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var seccion by rememberSaveable { mutableStateOf("consultar") }
    var leySeleccionada by remember { mutableStateOf<Ley>(LeyesDemo.normas.first()) }
    val apuntes = remember { mutableStateListOf<Apunte>() }
    val mapas = remember { mutableStateListOf<MapaConceptual>() }
    var fotoPerfil by remember { mutableStateOf<Bitmap?>(null) }

    val cerrarSesion: () -> Unit = {
        usuarioActivo = null
        pantallaAcceso = "login"
        seccion = "consultar"
        mapas.clear()
        fotoPerfil = null
    }

    if (usuarioActivo == null) {
        if (pantallaAcceso == "registro") {
            RegistroScreen(
                onRegistrar = { nombre, correo, clave ->
                    if (cuentas.any { it.correo.equals(correo.trim(), ignoreCase = true) }) {
                        "Ese correo ya está registrado."
                    } else {
                        cuentas.add(AuthRepository.crearUsuario(nombre, correo, clave))
                        null
                    }
                },
                onIrALogin = { pantallaAcceso = "login" }
            )
        } else {
            LoginScreen(
                onLogin = { correo, clave -> AuthRepository.autenticar(cuentas, correo, clave) },
                onLoginCorrecto = { usuario -> usuarioActivo = usuario; seccion = "consultar" },
                onIrARegistro = { pantallaAcceso = "registro" }
            )
        }
        return
    }

    val usuario = usuarioActivo!!
    val apuntesUsuario = apuntes.filter { it.usuarioCorreo.equals(usuario.correo, ignoreCase = true) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    "consultar" to "Consultar",
                    "estudiar" to "Estudiar",
                    "mapas" to "Mapas",
                    "perfil" to "Perfil"
                ).forEach { (ruta, etiqueta) ->
                    val seleccionado = seccion == ruta || (seccion == "detalle_norma" && ruta == "estudiar")
                    NavigationBarItem(
                        selected = seleccionado,
                        onClick = { seccion = ruta },
                        icon = {
                            Text(
                                when (ruta) {
                                    "consultar" -> "⌕"
                                    "estudiar" -> "▤"
                                    "mapas" -> "◇"
                                    else -> "○"
                                }
                            )
                        },
                        label = { Text(etiqueta) }
                    )
                }
            }
        }
    ) { padding ->
        when (seccion) {
            "consultar" -> ConsultarNormasScreen(
                onAbrirNorma = { ley ->
                    leySeleccionada = ley
                    seccion = "detalle_norma"
                },
                modifier = Modifier.padding(padding)
            )
            "estudiar", "detalle_norma" -> EstudiarOrganizarScreen(
                usuarioCorreo = usuario.correo,
                ley = leySeleccionada,
                apuntes = apuntesUsuario.filter { it.leyId == leySeleccionada.id },
                onGuardar = { apunte ->
                    val indice = apuntes.indexOfFirst { it.id == apunte.id }
                    if (indice >= 0) apuntes[indice] = apunte else apuntes.add(apunte)
                },
                onEliminar = { apunte -> apuntes.removeAll { it.id == apunte.id } },
                onIrAConsultar = { seccion = "consultar" },
                modifier = Modifier.padding(padding)
            )
            "mapas" -> MapasConceptualesScreen(
                mapas = mapas,
                onGuardar = { mapas.add(it) },
                onEliminar = { mapa -> mapas.removeAll { it.id == mapa.id } },
                modifier = Modifier.padding(padding)
            )
            "perfil" -> PerfilScreen(
                usuario = usuario,
                fotoPerfil = fotoPerfil,
                onFotoTomada = { fotoPerfil = it },
                onCambiarClave = { claveActual, claveNueva, confirmacion ->
                    val cuenta = usuarioActivo!!
                    when {
                        !AuthRepository.claveCorrecta(cuenta, claveActual) -> "La contraseña actual no es correcta."
                        claveNueva.length < 6 -> "La nueva contraseña debe tener al menos 6 caracteres."
                        claveNueva != confirmacion -> "Las contraseñas nuevas no coinciden."
                        else -> {
                            val actualizado = AuthRepository.cambiarClave(cuenta, claveNueva)
                            val indice = cuentas.indexOfFirst { it.correo.equals(cuenta.correo, ignoreCase = true) }
                            if (indice >= 0) cuentas[indice] = actualizado
                            usuarioActivo = actualizado
                            null
                        }
                    }
                },
                onCerrarSesion = cerrarSesion,
                modifier = Modifier.padding(padding)
            )
            else -> ConsultarNormasScreen(
                onAbrirNorma = { ley -> leySeleccionada = ley; seccion = "detalle_norma" },
                modifier = Modifier.padding(padding)
            )
        }
    }
}
