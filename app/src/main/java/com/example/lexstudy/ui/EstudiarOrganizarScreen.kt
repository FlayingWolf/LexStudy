package com.example.lexstudy.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.Ley

@Composable
fun EstudiarOrganizarScreen(
    usuarioCorreo: String,
    ley: Ley,
    apuntes: List<Apunte>,
    onGuardar: (Apunte) -> Unit,
    onEliminar: (Apunte) -> Unit,
    onIrAConsultar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    var tamanoTexto by remember(ley.id) { mutableStateOf(16) }
    var titulo by remember(ley.id) { mutableStateOf("") }
    var contenido by remember(ley.id) { mutableStateOf("") }
    var archivoPdfUri by remember(ley.id) { mutableStateOf<String?>(null) }
    var apunteEnEdicionId by remember(ley.id) { mutableStateOf<String?>(null) }
    var error by remember(ley.id) { mutableStateOf("") }
    var mostrarResultado by remember { mutableStateOf(false) }
    var mensajeResultado by remember { mutableStateOf("") }
    var apuntePendienteEliminar by remember { mutableStateOf<Apunte?>(null) }

    val selectorPdf = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contexto.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }
            archivoPdfUri = uri.toString()
            error = ""
        }
    }

    val apuntesDeEstaLey = apuntes.filter { it.leyId == ley.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Estudiar y organizar",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Lee la ficha, ajusta el texto y guarda apuntes vinculados a esta norma.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        TextButton(onClick = onIrAConsultar) { Text("← Volver a consultar normas") }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(Modifier.padding(17.dp)) {
                Text("FICHA DE ESTUDIO", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(7.dp))
                Text(ley.nombre, color = MaterialTheme.colorScheme.onPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text(ley.articulo, color = MaterialTheme.colorScheme.onPrimary, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Text(ley.resumen, color = MaterialTheme.colorScheme.onPrimary, fontSize = tamanoTexto.sp, lineHeight = (tamanoTexto + 6).sp)
                Spacer(Modifier.height(12.dp))
                Text("Fuente de referencia: ${ley.referenciaOficial}", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f), fontSize = 10.sp)
                Text(ley.ultimaReferencia, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f), fontSize = 10.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text("Tamaño de texto", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
            TextButton(onClick = { tamanoTexto = (tamanoTexto - 1).coerceAtLeast(12) }) { Text("A−") }
            TextButton(onClick = { tamanoTexto = (tamanoTexto + 1).coerceAtMost(24) }) { Text("A+") }
        }

        Text(
            text = if (apunteEnEdicionId == null) "Crear apunte" else "Editar apunte",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it; error = "" },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Título del apunte") },
            colors = coloresCamposEstudio()
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it; error = "" },
            modifier = Modifier.fillMaxWidth().height(130.dp),
            label = { Text("Tus notas de estudio") },
            minLines = 4,
            colors = coloresCamposEstudio()
        )
        OutlinedButton(
            onClick = { selectorPdf.launch(arrayOf("application/pdf")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (archivoPdfUri == null) "Adjuntar PDF desde el dispositivo" else "PDF adjunto · Cambiar archivo")
        }
        if (archivoPdfUri != null) {
            Text(
                text = "Documento adjunto: ${archivoPdfUri!!.substringAfterLast('/')}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
        if (error.isNotBlank()) {
            Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
        Button(
            onClick = {
                when {
                    titulo.isBlank() -> error = "El título del apunte es obligatorio."
                    contenido.isBlank() -> error = "Escribe el contenido del apunte."
                    else -> {
                        val existente = apuntesDeEstaLey.firstOrNull { it.id == apunteEnEdicionId }
                        onGuardar(
                            Apunte(
                                id = existente?.id ?: apunteEnEdicionId ?: java.util.UUID.randomUUID().toString(),
                                usuarioCorreo = usuarioCorreo,
                                leyId = ley.id,
                                nombreLey = ley.nombre,
                                titulo = titulo.trim(),
                                contenido = contenido.trim(),
                                archivoPdfUri = archivoPdfUri,
                                actualizadoEn = System.currentTimeMillis()
                            )
                        )
                        titulo = ""
                        contenido = ""
                        archivoPdfUri = null
                        apunteEnEdicionId = null
                        error = ""
                        mensajeResultado = "El apunte se guardó para ${ley.nombre}."
                        mostrarResultado = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) { Text(if (apunteEnEdicionId == null) "Guardar apunte" else "Guardar cambios") }

        if (apunteEnEdicionId != null) {
            TextButton(onClick = {
                titulo = ""
                contenido = ""
                archivoPdfUri = null
                apunteEnEdicionId = null
                error = ""
            }) { Text("Cancelar edición") }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Apuntes guardados para esta norma · ${apuntesDeEstaLey.size}",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (apuntesDeEstaLey.isEmpty()) {
            Text(
                text = "Todavía no tienes apuntes para esta norma. Crea uno con el formulario anterior.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        } else {
            apuntesDeEstaLey.forEach { apunte ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(13.dp)) {
                        Text(apunte.titulo, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(apunte.contenido, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        if (apunte.archivoPdfUri != null) {
                            Spacer(Modifier.height(4.dp))
                            Text("Incluye un PDF adjunto", color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp)
                        }
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                titulo = apunte.titulo
                                contenido = apunte.contenido
                                archivoPdfUri = apunte.archivoPdfUri
                                apunteEnEdicionId = apunte.id
                                error = ""
                            }) { Text("Editar") }
                            TextButton(onClick = { apuntePendienteEliminar = apunte }) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Aviso: las fichas son resúmenes didácticos de demostración y no reemplazan el texto legal oficial ni constituyen asesoría jurídica.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }

    if (mostrarResultado) {
        AlertDialog(
            onDismissRequest = { mostrarResultado = false },
            title = { Text("Listo") },
            text = { Text(mensajeResultado) },
            confirmButton = { TextButton(onClick = { mostrarResultado = false }) { Text("Aceptar") } }
        )
    }

    apuntePendienteEliminar?.let { apunte ->
        AlertDialog(
            onDismissRequest = { apuntePendienteEliminar = null },
            title = { Text("Eliminar apunte") },
            text = { Text("¿Quieres eliminar ‘${apunte.titulo}’? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(apunte)
                    if (apunteEnEdicionId == apunte.id) {
                        titulo = ""
                        contenido = ""
                        archivoPdfUri = null
                        apunteEnEdicionId = null
                    }
                    apuntePendienteEliminar = null
                    mensajeResultado = "Apunte eliminado."
                    mostrarResultado = true
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { apuntePendienteEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun coloresCamposEstudio() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary
)
