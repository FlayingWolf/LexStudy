package com.example.lexstudy.presentation

import com.example.lexstudy.model.Validaciones
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.Ley
import com.example.lexstudy.repository.LexStudyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class EstudioAcciones(
    private val repository: LexStudyRepository,
    private val estadoPantalla: EstudioEstado,
    private val alcance: CoroutineScope
) {
    private var usuarioIdCargado: Long? = null
    private var tareaApuntes: Job? = null
    private var tareaMapas: Job? = null

    fun seleccionarLey(ley: Ley) {
        estadoPantalla.leySeleccionada = ley
    }

    fun cargarDatos(usuarioId: Long) {
        if (usuarioIdCargado == usuarioId) return
        usuarioIdCargado = usuarioId
        tareaApuntes?.cancel()
        tareaMapas?.cancel()
        tareaApuntes = alcance.launch {
            repository.observarApuntes(usuarioId).collectLatest { lista -> estadoPantalla.apuntes = lista }
        }
        tareaMapas = alcance.launch {
            repository.observarMapas(usuarioId).collectLatest { lista -> estadoPantalla.mapas = lista }
        }
    }

    fun limpiarDatosDeSesion() {
        tareaApuntes?.cancel()
        tareaMapas?.cancel()
        tareaApuntes = null
        tareaMapas = null
        usuarioIdCargado = null
        estadoPantalla.apuntes = emptyList()
        estadoPantalla.mapas = emptyList()
    }

    fun guardarApunte(
        usuarioId: Long,
        titulo: String,
        contenido: String,
        archivoPdfUri: String?,
        apunteId: Long? = null,
        alTerminar: (Boolean, String) -> Unit
    ) {
        Validaciones.apunte(titulo, contenido)?.let {
            alTerminar(false, it)
            return
        }
        val ley = estadoPantalla.leySeleccionada
        alcance.launch {
            try {
                repository.guardarApunte(usuarioId, ley, titulo, contenido, archivoPdfUri, apunteId)
                alTerminar(true, if (apunteId == null) "Apunte guardado." else "Apunte actualizado.")
            } catch (_: Exception) {
                alTerminar(false, "No se pudo guardar el apunte.")
            }
        }
    }

    fun eliminarApunte(usuarioId: Long, apunteId: Long, alTerminar: (Boolean) -> Unit) {
        alcance.launch {
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
        Validaciones.mapa(titulo, conceptoCentral, conceptoUno, conceptoDos)?.let {
            alTerminar(false, it)
            return
        }
        alcance.launch {
            try {
                repository.guardarMapa(usuarioId, titulo, conceptoCentral, conceptoUno, conceptoDos)
                alTerminar(true, "Mapa conceptual guardado.")
            } catch (_: Exception) {
                alTerminar(false, "No se pudo guardar el mapa.")
            }
        }
    }

    fun eliminarMapa(usuarioId: Long, mapaId: Long, alTerminar: (Boolean) -> Unit) {
        alcance.launch {
            try {
                repository.eliminarMapa(mapaId, usuarioId)
                alTerminar(true)
            } catch (_: Exception) {
                alTerminar(false)
            }
        }
    }
}
