package com.example.lexstudy.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PerfilScreen(
    usuario: Usuario,
    viewModel: AuthViewModel
) {
    var fotoPerfil by remember(usuario.fotoPerfilPath) { mutableStateOf<Bitmap?>(null) }
    var mensajeCamara by remember { mutableStateOf("") }
    var claveActual by remember { mutableStateOf("") }
    var nuevaClave by remember { mutableStateOf("") }
    var confirmarNuevaClave by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.limpiarMensajeCambioContrasena()
    }

    LaunchedEffect(viewModel.cambioContrasenaCorrecto) {
        if (viewModel.cambioContrasenaCorrecto) {
            claveActual = ""
            nuevaClave = ""
            confirmarNuevaClave = ""
        }
    }

    LaunchedEffect(usuario.fotoPerfilPath) {
        val rutaFoto = usuario.fotoPerfilPath
        fotoPerfil = if (rutaFoto.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) { BitmapFactory.decodeFile(rutaFoto) }
        }
    }

    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { imagenCapturada ->
        if (imagenCapturada != null) {
            mensajeCamara = ""
            viewModel.limpiarMensajeFotoPerfil()
            viewModel.actualizarFotoPerfil(imagenCapturada)
        } else {
            mensajeCamara = "No se capturó ninguna fotografía."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = ColoresLexStudy.verdePino
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Personaliza tu cuenta de LexStudy",
            color = ColoresLexStudy.textoSecundario,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(144.dp)
                .clip(CircleShape)
                .background(ColoresLexStudy.verdeSuave),
            contentAlignment = Alignment.Center
        ) {
            val foto = fotoPerfil
            if (foto != null) {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = "Fotografía de perfil de ${usuario.nombre}",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = usuario.nombre.trim().take(1).uppercase().ifBlank { "L" },
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColoresLexStudy.verdePinoMedio
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { lanzadorCamara.launch(null) },
            enabled = !viewModel.estaGuardandoFotoPerfil
        ) {
            Text("📷  Cambiar foto con la cámara")
        }

        if (viewModel.estaGuardandoFotoPerfil) {
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = ColoresLexStudy.verdePinoMedio,
                strokeWidth = 2.dp
            )
            Text("Guardando fotografía…", color = ColoresLexStudy.textoSecundario)
        }

        val mensaje = viewModel.mensajeFotoPerfil.ifBlank { mensajeCamara }
        if (mensaje.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensaje,
                color = if (mensaje.startsWith("No se pudo") || mensaje.startsWith("No se capturó")) {
                    ColoresLexStudy.rojoError
                } else {
                    ColoresLexStudy.verdePinoMedio
                },
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(28.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = usuario.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ColoresLexStudy.verdePino
                )
                Spacer(Modifier.height(16.dp))
                Text("Correo institucional", color = ColoresLexStudy.textoSecundario)
                Spacer(Modifier.height(4.dp))
                Text(usuario.correo, color = ColoresLexStudy.texto)
                Spacer(Modifier.height(16.dp))
                Text("Tipo de cuenta", color = ColoresLexStudy.textoSecundario)
                Spacer(Modifier.height(4.dp))
                Text(tipoCuentaDuoc(usuario.correo), color = ColoresLexStudy.texto)
            }
        }

        Spacer(Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Cambiar contraseña",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ColoresLexStudy.verdePino
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Confirma tu contraseña actual para guardar una nueva.",
                    color = ColoresLexStudy.textoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = claveActual,
                    onValueChange = { claveActual = it },
                    label = { Text("Contraseña actual") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !viewModel.estaCambiandoContrasena,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = nuevaClave,
                    onValueChange = { nuevaClave = it },
                    label = { Text("Nueva contraseña") },
                    supportingText = { Text("Mínimo 6 caracteres") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !viewModel.estaCambiandoContrasena,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = confirmarNuevaClave,
                    onValueChange = { confirmarNuevaClave = it },
                    label = { Text("Confirmar nueva contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !viewModel.estaCambiandoContrasena,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = {
                        viewModel.cambiarContrasena(
                            claveActual = claveActual,
                            nuevaClave = nuevaClave,
                            confirmarClave = confirmarNuevaClave
                        )
                    },
                    enabled = !viewModel.estaCambiandoContrasena,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (viewModel.estaCambiandoContrasena) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.size(8.dp))
                        Text("Actualizando…")
                    } else {
                        Text("Guardar nueva contraseña")
                    }
                }
                if (viewModel.mensajeCambioContrasena.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = viewModel.mensajeCambioContrasena,
                        color = if (viewModel.cambioContrasenaCorrecto) {
                            ColoresLexStudy.verdePinoMedio
                        } else {
                            ColoresLexStudy.rojoError
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "En esta etapa, la cuenta y los datos de estudio se reinician al terminar la aplicación. La persistencia se incorporará en el proyecto final.",
            color = ColoresLexStudy.textoSecundario,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

private fun tipoCuentaDuoc(correo: String): String {
    return when {
        correo.trim().lowercase().endsWith("@profesor.duoc.cl") -> "Docente Duoc UC"
        correo.trim().lowercase().endsWith("@duocuc.cl") -> "Estudiante Duoc UC"
        else -> "Cuenta Duoc UC"
    }
}
