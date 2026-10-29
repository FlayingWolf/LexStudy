package com.example.lexstudy.ui

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lexstudy.model.Usuario

@Composable
fun PerfilScreen(
    usuario: Usuario,
    fotoPerfil: Bitmap?,
    onFotoTomada: (Bitmap) -> Unit,
    onCambiarClave: (String, String, String) -> String?,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarCambioClave by remember { mutableStateOf(false) }
    var confirmarSalida by remember { mutableStateOf(false) }
    var claveActual by remember { mutableStateOf("") }
    var claveNueva by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var errorClave by remember { mutableStateOf("") }
    var exitoClave by remember { mutableStateOf(false) }
    val selectorCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) onFotoTomada(bitmap)
    }
    val tipoCuenta = when {
        usuario.correo.endsWith("@profesor.duoc.cl", ignoreCase = true) -> "Docente Duoc UC"
        usuario.correo.endsWith("@duocuc.cl", ignoreCase = true) -> "Estudiante Duoc UC"
        else -> "Usuario"
    }

    Column(modifier = modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        if (fotoPerfil != null) {
            Image(bitmap = fotoPerfil.asImageBitmap(), contentDescription = "Fotografía de perfil", modifier = Modifier.size(132.dp).clip(CircleShape))
        } else {
            Box(Modifier.size(132.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Text(usuario.nombre.firstOrNull()?.uppercase() ?: "L", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
        Button(onClick = { selectorCamara.launch(null) }) { Text("Cambiar foto con la cámara") }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(usuario.correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(tipoCuenta, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }
        Button(onClick = { errorClave = ""; mostrarCambioClave = true }, modifier = Modifier.fillMaxWidth()) { Text("Cambiar contraseña") }
        TextButton(onClick = { confirmarSalida = true }) { Text("Cerrar sesión") }
    }

    if (mostrarCambioClave) {
        AlertDialog(
            onDismissRequest = { mostrarCambioClave = false },
            title = { Text("Cambiar contraseña") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(claveActual, { claveActual = it; errorClave = "" }, label = { Text("Contraseña actual") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
                    OutlinedTextField(claveNueva, { claveNueva = it; errorClave = "" }, label = { Text("Nueva contraseña") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
                    OutlinedTextField(confirmarClave, { confirmarClave = it; errorClave = "" }, label = { Text("Confirmar nueva contraseña") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
                    if (errorClave.isNotBlank()) Text(errorClave, color = MaterialTheme.colorScheme.error)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val resultado = onCambiarClave(claveActual, claveNueva, confirmarClave)
                    if (resultado == null) {
                        mostrarCambioClave = false
                        claveActual = ""; claveNueva = ""; confirmarClave = ""
                        exitoClave = true
                    } else errorClave = resultado
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { mostrarCambioClave = false }) { Text("Cancelar") } }
        )
    }
    if (exitoClave) {
        AlertDialog(onDismissRequest = { exitoClave = false }, title = { Text("Contraseña actualizada") }, text = { Text("La contraseña se actualizó en este prototipo. Los datos se mantienen solo mientras la app está abierta.") }, confirmButton = { Button(onClick = { exitoClave = false }) { Text("Aceptar") } })
    }
    if (confirmarSalida) {
        AlertDialog(onDismissRequest = { confirmarSalida = false }, title = { Text("Cerrar sesión") }, text = { Text("¿Quieres salir de tu cuenta?") }, confirmButton = { Button(onClick = { confirmarSalida = false; onCerrarSesion() }) { Text("Salir") } }, dismissButton = { TextButton(onClick = { confirmarSalida = false }) { Text("Cancelar") } })
    }
}
