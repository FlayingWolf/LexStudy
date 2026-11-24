package com.example.lexstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import com.example.lexstudy.presentation.AuthEstado
import com.example.lexstudy.presentation.AuthAcciones

class AuthViewModel(
    repository: LexStudyRepository,
    sesionRepository: SesionRepository,
    fotoPerfilRepository: FotoPerfilRepository
) : ViewModel() {
    val estadoPantalla = AuthEstado()
    val acciones = AuthAcciones(repository, sesionRepository, fotoPerfilRepository, estadoPantalla, viewModelScope)
}
