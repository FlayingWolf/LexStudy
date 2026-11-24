package com.example.lexstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.presentation.EstudioEstado
import com.example.lexstudy.presentation.EstudioAcciones

class LexStudyViewModel(
    repository: LexStudyRepository
) : ViewModel() {
    val estadoPantalla = EstudioEstado()
    val acciones = EstudioAcciones(repository, estadoPantalla, viewModelScope)
}
