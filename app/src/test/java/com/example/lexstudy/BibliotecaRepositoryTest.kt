package com.example.lexstudy

import com.example.lexstudy.remote.*
import com.example.lexstudy.repository.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class BibliotecaRepositoryTest {
    private class ApiFalsa : BibliotecaApi {
        var enviado: Sugerencia? = null
        private fun ficha(nombre: String) = listOf(Ficha(nombre, "Prueba"))
        override suspend fun glosario() = ficha("Glosario")
        override suspend fun materias() = ficha("Materias")
        override suspend fun fuentes() = ficha("Fuentes")
        override suspend fun ejercicios() = ficha("Ejercicios")
        override suspend fun tecnicas() = ficha("Técnicas")
        override suspend fun preguntas() = ficha("Preguntas")
        override suspend fun sugerir(datos: Sugerencia): Recibo {
            enviado = datos
            return Recibo(42)
        }
    }

    @Test fun cadaSeccionConsultaSuColeccion() = runBlocking {
        val repo = BibliotecaRepository(ApiFalsa())
        for (seccion in Seccion.entries) {
            assertEquals(seccion.etiqueta, repo.consultar(seccion).single().titulo)
        }
    }

    @Test fun sugerirRecortaYDevuelveElIdConfirmado() = runBlocking {
        val api = ApiFalsa()
        val id = BibliotecaRepository(api).sugerir("  Fuentes primarias  ")
        assertEquals(Sugerencia("Fuentes primarias"), api.enviado)
        assertEquals(42, id)
    }

    @Test fun sugerenciasFueraDeLimiteNuncaLleganALaApi() {
        val api = ApiFalsa()
        val repo = BibliotecaRepository(api)
        for (texto in listOf("    ", "abcd", "a".repeat(501))) {
            assertThrows(IllegalArgumentException::class.java) { runBlocking { repo.sugerir(texto) } }
        }
        assertNull(api.enviado)
    }
}
