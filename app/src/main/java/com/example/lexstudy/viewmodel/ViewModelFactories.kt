package com.example.lexstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository

class AuthViewModelFactory(
    private val repository: LexStudyRepository,
    private val sesionRepository: SesionRepository,
    private val fotoPerfilRepository: FotoPerfilRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, sesionRepository, fotoPerfilRepository) as T
        }
        throw IllegalArgumentException("ViewModel no compatible: ${modelClass.name}")
    }
}

class LexStudyViewModelFactory(
    private val repository: LexStudyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LexStudyViewModel::class.java)) {
            return LexStudyViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel no compatible: ${modelClass.name}")
    }
}
