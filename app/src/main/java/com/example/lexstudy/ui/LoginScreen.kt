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
import com.example.lexstudy.model.Usuario

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Usuario?,
    onLoginCorrecto: ((Usuario) -> Unit)? = null,
    onIrARegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(R.drawable.lexstudy_logo), contentDescription = "Logotipo LexStudy", modifier = Modifier.size(84.dp))
        Spacer(Modifier.height(12.dp))
        Text("LexStudy", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Inicia sesión para continuar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(correo, { correo = it; error = "" }, label = { Text("Correo institucional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(clave, { clave = it; error = "" }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true, modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                val usuario = if (correo.isBlank() || clave.isBlank()) null else onLogin(correo.trim(), clave)
                if (usuario == null) error = "Correo o contraseña incorrectos."
                else if (onLoginCorrecto != null) onLoginCorrecto(usuario)
                else mensajeExito = "Las credenciales son correctas. La pantalla Inicio se incorporará en la siguiente etapa."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ingresar") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onIrARegistro) { Text("¿No tienes cuenta? Regístrate") }
    }

    if (mensajeExito.isNotBlank()) {
        AlertDialog(
            onDismissRequest = { mensajeExito = "" },
            title = { Text("Acceso correcto") },
            text = { Text(mensajeExito) },
            confirmButton = { Button(onClick = { mensajeExito = "" }) { Text("Aceptar") } }
        )
    }
}
