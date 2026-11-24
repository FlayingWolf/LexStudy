package com.example.lexstudy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lexstudy.model.Usuario

class AuthEstado {
    var usuarioActual: Usuario? by mutableStateOf(null)
        internal set

    var estaCargando: Boolean by mutableStateOf(false)
        internal set

    var estaRestaurandoSesion: Boolean by mutableStateOf(true)
        internal set

    var mensajeError: String by mutableStateOf("")
        internal set

    var estaGuardandoFotoPerfil: Boolean by mutableStateOf(false)
        internal set

    var mensajeFotoPerfil: String by mutableStateOf("")
        internal set

    var estaCambiandoContrasena: Boolean by mutableStateOf(false)
        internal set

    var mensajeCambioContrasena: String by mutableStateOf("")
        internal set

    var cambioContrasenaCorrecto: Boolean by mutableStateOf(false)
        internal set
}
