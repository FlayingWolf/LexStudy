package com.example.lexstudy.model

object Validaciones {
    private val correoInstitucional = Regex("^[A-Za-z0-9._%+-]+@(profesor\\.duoc\\.cl|duocuc\\.cl)$", RegexOption.IGNORE_CASE)

    fun ingreso(correo: String, clave: String): String? = when {
        !correoInstitucional.matches(correo.trim()) -> "Ingresa un correo electrónico válido."
        clave.isBlank() -> "Ingresa tu contraseña."
        else -> null
    }

    fun registro(nombre: String, correo: String, clave: String, confirmacion: String): String? = when {
        nombre.trim().length < 2 -> "Escribe tu nombre (al menos 2 caracteres)."
        !correoInstitucional.matches(correo.trim()) -> "Ingresa un correo electrónico válido."
        clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
        clave != confirmacion -> "Las contraseñas no coinciden."
        else -> null
    }

    fun cambioContrasena(actual: String, nueva: String, confirmacion: String): String? = when {
        actual.isBlank() -> "Ingresa tu contraseña actual."
        nueva.length < 6 -> "La nueva contraseña debe tener al menos 6 caracteres."
        nueva != confirmacion -> "La nueva contraseña y su confirmación no coinciden."
        actual == nueva -> "La nueva contraseña debe ser distinta de la actual."
        else -> null
    }

    fun apunte(titulo: String, contenido: String): String? = when {
        titulo.isBlank() -> "El título del apunte es obligatorio."
        contenido.isBlank() -> "Escribe el contenido antes de guardar."
        else -> null
    }

    fun mapa(titulo: String, central: String, uno: String, dos: String): String? =
        if (listOf(titulo, central, uno, dos).any { it.isBlank() }) "Completa el título y los tres conceptos." else null
}
