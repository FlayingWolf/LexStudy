package com.example.lexstudy.presentation

import com.example.lexstudy.BuildConfig
import com.example.lexstudy.remote.BibliotecaCliente
import com.example.lexstudy.repository.BibliotecaRepository
import com.example.lexstudy.repository.Seccion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class BibliotecaAcciones(
    private val estadoPantalla: BibliotecaEstado,
    private val alcance: CoroutineScope
) {
    private val repositorio = BibliotecaRepository(BibliotecaCliente.crear(BuildConfig.API_URL))
    private var carga: Job? = null

    init { actualizar() }

    fun seleccionar(seccionNueva: Seccion) {
        estadoPantalla.seccion = seccionNueva
        actualizar()
    }

    fun actualizar() {
        carga?.cancel()
        estadoPantalla.fichas = emptyList()
        estadoPantalla.estado = "Cargando…"
        val seleccion = estadoPantalla.seccion
        carga = alcance.launch {
            try {
                estadoPantalla.fichas = repositorio.consultar(seleccion)
                estadoPantalla.estado = if (estadoPantalla.fichas.isEmpty()) "Todavía no hay fichas." else ""
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                estadoPantalla.estado = "No se pudo cargar la sección. ${e.localizedMessage.orEmpty()}"
            }
        }
    }

    fun editarSugerencia(valor: String) {
        estadoPantalla.texto = valor
        estadoPantalla.aviso = ""
    }

    fun enviar() {
        if (estadoPantalla.enviando) return
        estadoPantalla.enviando = true
        alcance.launch {
            try {
                val id = repositorio.sugerir(estadoPantalla.texto)
                estadoPantalla.texto = ""
                estadoPantalla.aviso = "Sugerencia recibida (#$id)."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                estadoPantalla.aviso = "No se pudo confirmar el envío. ${e.localizedMessage.orEmpty()}"
            } finally {
                estadoPantalla.enviando = false
            }
        }
    }
}
