package com.example.lexstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexstudy.presentation.BibliotecaEstado
import com.example.lexstudy.presentation.BibliotecaAcciones

class BibliotecaViewModel : ViewModel() {
    val estadoPantalla = BibliotecaEstado()
    val acciones = BibliotecaAcciones(estadoPantalla, viewModelScope)
}
