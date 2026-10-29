package com.example.lexstudy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.AuthRepository

@Composable
fun LexStudyApp() {
    val cuentas = remember { mutableStateListOf<Usuario>() }
    var pantallaAcceso by remember { mutableStateOf("login") }
    if (pantallaAcceso == "login") {
        LoginScreen(
            onLogin = { correo, clave -> AuthRepository.autenticar(cuentas, correo, clave) },
            onLoginCorrecto = null,
            onIrARegistro = { pantallaAcceso = "registro" }
        )
    } else {
        RegistroScreen(
            onRegistrar = { nombre, correo, clave ->
                if (cuentas.any { it.correo.equals(correo.trim(), ignoreCase = true) }) "Ese correo ya está registrado."
                else { cuentas.add(AuthRepository.crearUsuario(nombre, correo, clave)); null }
            },
            onIrALogin = { pantallaAcceso = "login" }
        )
    }
}
