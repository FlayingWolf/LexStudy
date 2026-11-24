package com.example.lexstudy

import com.example.lexstudy.model.Validaciones
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionesTest {
    @Test fun ingresoValido() { assertNull(Validaciones.ingreso("alumno@duocuc.cl", "123456")) }
    @Test fun correoExterno() { assertNotNull(Validaciones.ingreso("alumno@gmail.com", "123456")) }
    @Test fun claveVacia() { assertNotNull(Validaciones.ingreso("alumno@duocuc.cl", "")) }
    @Test fun registroValido() { assertNull(Validaciones.registro("Ana", "ana@duocuc.cl", "123456", "123456")) }
    @Test fun clavesDistintas() { assertNotNull(Validaciones.registro("Ana", "ana@duocuc.cl", "123456", "654321")) }
    @Test fun cambioValido() { assertNull(Validaciones.cambioContrasena("123456", "654321", "654321")) }
    @Test fun claveRepetida() { assertNotNull(Validaciones.cambioContrasena("123456", "123456", "123456")) }
    @Test fun apunteVacio() { assertNotNull(Validaciones.apunte("Título", "")) }
    @Test fun apunteValido() { assertNull(Validaciones.apunte("Título", "Contenido")) }
    @Test fun mapaIncompleto() { assertNotNull(Validaciones.mapa("Título", "Centro", "Uno", "")) }
}
