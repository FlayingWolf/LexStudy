package com.example.lexstudy.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.Ley
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.repository.LeyesDemo
import com.example.lexstudy.repository.LexStudyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LexStudyViewModel(private val repository: LexStudyRepository) : ViewModel() {
    var leySeleccionada: Ley by mutableStateOf(LeyesDemo.normas.first())
        private set

    var apuntes: List<Apunte> by mutableStateOf(emptyList())
        private set

    var mapas: List<MapaConceptual> by mutableStateOf(emptyList())
        private set

    private var usuarioIdCargado: Long? = null
    private var tareaApuntes: Job? = null
    private var tareaMapas: Job? = null

    fun seleccionarLey(ley: Ley) {
        leySeleccionada = ley
    }

    fun seleccionarLeyPorId(leyId: String) {
        leySeleccionada = LeyesDemo.buscarLey(leyId)
    }

    fun cargarDatos(usuarioId: Long) {
        if (usuarioIdCargado == usuarioId) return
        usuarioIdCargado = usuarioId
        tareaApuntes?.cancel()
        tareaMapas?.cancel()
        tareaApuntes = viewModelScope.launch {
            repository.observarApuntes(usuarioId).collectLatest { lista -> apuntes = lista }
        }
        tareaMapas = viewModelScope.launch {
            repository.observarMapas(usuarioId).collectLatest { lista -> mapas = lista }
        }
    }

    fun limpiarDatosDeSesion() {
        tareaApuntes?.cancel()
        tareaMapas?.cancel()
        tareaApuntes = null
        tareaMapas = null
        usuarioIdCargado = null
        apuntes = emptyList()
        mapas = emptyList()
    }

    fun guardarApunte(
        usuarioId: Long,
        titulo: String,
        contenido: String,
        archivoPdfUri: String?,
        apunteId: Long? = null,
        alTerminar: (Boolean, String) -> Unit
    ) {
        if (titulo.isBlank()) {
            alTerminar(false, "El título del apunte es obligatorio.")
            return
        }
        if (contenido.isBlank()) {
            alTerminar(false, "Escribe el contenido antes de guardar.")
            return
        }
        val ley = leySeleccionada
        viewModelScope.launch {
            try {
                repository.guardarApunte(usuarioId, ley, titulo, contenido, archivoPdfUri, apunteId)
                alTerminar(true, if (apunteId == null) "Apunte guardado." else "Apunte actualizado.")
            } catch (_: Exception) {
                alTerminar(false, "No se pudo guardar el apunte.")
            }
        }
    }

    fun eliminarApunte(usuarioId: Long, apunteId: Long, alTerminar: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarApunte(apunteId, usuarioId)
                alTerminar(true)
            } catch (_: Exception) {
                alTerminar(false)
            }
        }
    }

    fun guardarMapa(
        usuarioId: Long,
        titulo: String,
        conceptoCentral: String,
        conceptoUno: String,
        conceptoDos: String,
        alTerminar: (Boolean, String) -> Unit
    ) {
        if (titulo.isBlank() || conceptoCentral.isBlank() || conceptoUno.isBlank() || conceptoDos.isBlank()) {
            alTerminar(false, "Completa el título y los tres conceptos.")
            return
        }
        viewModelScope.launch {
            try {
                repository.guardarMapa(usuarioId, titulo, conceptoCentral, conceptoUno, conceptoDos)
                alTerminar(true, "Mapa conceptual guardado.")
            } catch (_: Exception) {
                alTerminar(false, "No se pudo guardar el mapa.")
            }
        }
    }

    fun eliminarMapa(usuarioId: Long, mapaId: Long, alTerminar: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarMapa(mapaId, usuarioId)
                alTerminar(true)
            } catch (_: Exception) {
                alTerminar(false)
            }
        }
    }
}
