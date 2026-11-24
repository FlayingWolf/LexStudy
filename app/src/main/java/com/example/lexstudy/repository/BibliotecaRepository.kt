package com.example.lexstudy.repository

import com.example.lexstudy.remote.*

enum class Seccion(val etiqueta: String) {
    GLOSARIO("Glosario"), MATERIAS("Materias"), FUENTES("Fuentes"),
    EJERCICIOS("Ejercicios"), TECNICAS("Técnicas"), PREGUNTAS("Preguntas")
}

class BibliotecaRepository(private val api: BibliotecaApi) {
    suspend fun consultar(seccion: Seccion): List<Ficha> = when (seccion) {
        Seccion.GLOSARIO -> api.glosario()
        Seccion.MATERIAS -> api.materias()
        Seccion.FUENTES -> api.fuentes()
        Seccion.EJERCICIOS -> api.ejercicios()
        Seccion.TECNICAS -> api.tecnicas()
        Seccion.PREGUNTAS -> api.preguntas()
    }

    suspend fun sugerir(texto: String): Int {
        val limpio = texto.trim()
        require(limpio.length in 5..500) { "Escribe entre 5 y 500 caracteres." }
        val id = api.sugerir(Sugerencia(limpio)).id
        check(id > 0) { "El servidor no confirmó el registro." }
        return id
    }
}
