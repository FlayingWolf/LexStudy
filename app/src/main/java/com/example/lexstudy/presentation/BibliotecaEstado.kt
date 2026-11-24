package com.example.lexstudy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lexstudy.remote.Ficha
import com.example.lexstudy.repository.Seccion

class BibliotecaEstado {
    var seccion by mutableStateOf(Seccion.GLOSARIO)
        internal set

    var fichas by mutableStateOf(emptyList<Ficha>())
        internal set

    var estado by mutableStateOf("Cargando…")
        internal set

    var texto by mutableStateOf("")
        internal set

    var aviso by mutableStateOf("")
        internal set

    var enviando by mutableStateOf(false)
        internal set
}
