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
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.AuthRepository

@Composable
fun LexStudyApp() {
    val cuentas = remember { mutableStateListOf<Usuario>() }
    var pantallaAcceso by rememberSaveable { mutableStateOf("login") }
    var usuarioActivo by remember { mutableStateOf<Usuario?>(null) }
    var seccion by rememberSaveable { mutableStateOf("inicio") }
    val cerrarSesion: () -> Unit = {
        usuarioActivo = null
        pantallaAcceso = "login"
        seccion = "inicio"
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

    InicioScreen(usuarioActivo!!, onCerrarSesion = cerrarSesion)
}
