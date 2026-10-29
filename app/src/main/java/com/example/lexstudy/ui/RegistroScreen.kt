package com.example.lexstudy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lexstudy.R

private val correoInstitucional = Regex(
    "^[A-Za-z0-9._%+-]+@(profesor\\.duoc\\.cl|duocuc\\.cl)$",
    RegexOption.IGNORE_CASE
)

@Composable
fun RegistroScreen(
    onRegistrar: (String, String, String) -> String?,
    onIrALogin: (() -> Unit)? = null
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var mostrarExito by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(R.drawable.lexstudy_logo), contentDescription = "Logotipo LexStudy", modifier = Modifier.size(84.dp))
        Spacer(Modifier.height(12.dp))
        Text("LexStudy", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Crea tu cuenta de estudio", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(nombre, { nombre = it; error = "" }, label = { Text("Nombre completo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(correo, { correo = it; error = "" }, label = { Text("Correo institucional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(clave, { clave = it; error = "" }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(confirmarClave, { confirmarClave = it; error = "" }, label = { Text("Confirmar contraseña") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true, modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                error = when {
                    nombre.isBlank() -> "Ingresa tu nombre."
                    !correoInstitucional.matches(correo.trim()) -> "Usa un correo @duocuc.cl o @profesor.duoc.cl."
                    clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
                    clave != confirmarClave -> "Las contraseñas no coinciden."
                    else -> onRegistrar(nombre.trim(), correo.trim(), clave).orEmpty()
                }
                if (error.isBlank()) mostrarExito = true
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear cuenta") }
        if (onIrALogin != null) {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onIrALogin) { Text("¿Ya tienes cuenta? Inicia sesión") }
        }
    }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = { mostrarExito = false },
            title = { Text("Registro completado") },
            text = { Text("La cuenta de demostración quedó registrada correctamente.") },
            confirmButton = {
                Button(onClick = { mostrarExito = false; onIrALogin?.invoke() }) {
                    Text(if (onIrALogin == null) "Aceptar" else "Ir al login")
                }
            }
        )
    }
}
