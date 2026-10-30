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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.viewmodel.LexStudyViewModel

@Composable
fun EstudiarOrganizarScreen(
    usuarioId: Long,
    viewModel: LexStudyViewModel
) {
    val ley = viewModel.leySeleccionada
    val contexto = LocalContext.current
    var tamanoTexto by remember { mutableStateOf(16) }
    var titulo by remember(ley.id) { mutableStateOf("") }
    var contenido by remember(ley.id) { mutableStateOf("") }
    var archivoPdfUri by remember(ley.id) { mutableStateOf<String?>(null) }
    var apunteEnEdicionId by remember(ley.id) { mutableStateOf<Long?>(null) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var mensajeResultado by remember { mutableStateOf("") }
    var resultadoCorrecto by remember { mutableStateOf(false) }
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
        }
    }

    val apuntesDeEstaLey = viewModel.apuntes.filter { it.leyId == ley.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoresLexStudy.marfil)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text(
            "Estudiar y organizar",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = ColoresLexStudy.verdePino
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "Lee la ficha, ajusta el texto y guarda tus apuntes.",
            color = ColoresLexStudy.textoSecundario,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColoresLexStudy.verdeTarjeta)
        ) {
            Column(Modifier.padding(17.dp)) {
                Text("FICHA DE ESTUDIO", color = ColoresLexStudy.blanco.copy(alpha = 0.78f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(7.dp))
                Text(ley.nombre, color = ColoresLexStudy.blanco, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text(ley.articulo, color = ColoresLexStudy.blanco, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Text(ley.resumen, color = ColoresLexStudy.blanco, fontSize = tamanoTexto.sp, lineHeight = (tamanoTexto + 6).sp)
                Spacer(Modifier.height(12.dp))
                Text("Fuente de referencia: ${ley.referenciaOficial}", color = ColoresLexStudy.blanco.copy(alpha = 0.82f), fontSize = 10.sp)
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tamaño de texto", color = ColoresLexStudy.textoSecundario, fontSize = 12.sp)
            TextButton(onClick = { tamanoTexto = (tamanoTexto - 1).coerceAtLeast(12) }) { Text("A−") }
            TextButton(onClick = { tamanoTexto = (tamanoTexto + 1).coerceAtMost(24) }) { Text("A+") }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            if (apunteEnEdicionId == null) "Crear apunte" else "Editar apunte",
            color = ColoresLexStudy.verdePino,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Título del apunte") },
            colors = coloresCamposEstudio()
        )
        Spacer(Modifier.height(9.dp))
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            modifier = Modifier.fillMaxWidth().height(130.dp),
            label = { Text("Tus notas de estudio") },
            minLines = 4,
            colors = coloresCamposEstudio()
        )
        Spacer(Modifier.height(9.dp))
        OutlinedButton(
            onClick = { selectorPdf.launch(arrayOf("application/pdf")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (archivoPdfUri == null) "Adjuntar PDF desde el dispositivo" else "PDF adjunto · Cambiar archivo")
        }
        if (archivoPdfUri != null) {
            Text(
                "Documento adjunto: ${archivoPdfUri!!.substringAfterLast('/')}",
                color = ColoresLexStudy.textoSecundario,
                fontSize = 10.sp
            )
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.guardarApunte(
                    usuarioId = usuarioId,
                    titulo = titulo,
                    contenido = contenido,
                    archivoPdfUri = archivoPdfUri,
                    apunteId = apunteEnEdicionId
                ) { correcto, mensaje ->
                    resultadoCorrecto = correcto
                    mensajeResultado = mensaje
                    mostrarResultado = true
                    if (correcto) {
                        titulo = ""
                        contenido = ""
                        archivoPdfUri = null
                        apunteEnEdicionId = null
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ColoresLexStudy.verdePinoMedio)
        ) {
            Text(if (apunteEnEdicionId == null) "Guardar apunte" else "Guardar cambios")
        }
        if (apunteEnEdicionId != null) {
            TextButton(
                onClick = {
                    titulo = ""
                    contenido = ""
                    archivoPdfUri = null
                    apunteEnEdicionId = null
                },
                modifier = Modifier.align(Alignment.End)
            ) { Text("Cancelar edición") }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Apuntes guardados · ${apuntesDeEstaLey.size}",
            color = ColoresLexStudy.verdePino,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(10.dp))
        if (apuntesDeEstaLey.isEmpty()) {
            Text(
                "Todavía no tienes apuntes para esta norma. Tus notas se guardarán en el dispositivo.",
                color = ColoresLexStudy.textoSecundario,
                fontSize = 12.sp
            )
        } else {
            apuntesDeEstaLey.forEach { apunte ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = CardDefaults.cardColors(containerColor = ColoresLexStudy.blanco)
                ) {
                    Column(Modifier.padding(13.dp)) {
                        Text(apunte.titulo, color = ColoresLexStudy.verdePino, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(apunte.contenido, color = ColoresLexStudy.textoSecundario, fontSize = 12.sp, maxLines = 3)
                        if (apunte.archivoPdfUri != null) {
                            Spacer(Modifier.height(4.dp))
                            Text("Incluye un PDF adjunto", color = ColoresLexStudy.verdePinoMedio, fontSize = 10.sp)
                        }
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                titulo = apunte.titulo
                                contenido = apunte.contenido
                                archivoPdfUri = apunte.archivoPdfUri
                                apunteEnEdicionId = apunte.id
                            }) { Text("Editar") }
                            TextButton(onClick = { apuntePendienteEliminar = apunte }) { Text("Eliminar", color = ColoresLexStudy.rojoError) }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text(
            "Aviso: la ficha contiene un resumen didáctico de ejemplo, no el texto legal completo ni una actualización oficial.",
            color = ColoresLexStudy.textoSecundario,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }

    if (mostrarResultado) {
        AlertDialog(
            onDismissRequest = { mostrarResultado = false },
            title = { Text(if (resultadoCorrecto) "Listo" else "No se pudo completar") },
            text = { Text(mensajeResultado) },
            confirmButton = {
                TextButton(onClick = { mostrarResultado = false }) { Text("Aceptar") }
            }
        )
    }

    apuntePendienteEliminar?.let { apunte ->
        AlertDialog(
            onDismissRequest = { apuntePendienteEliminar = null },
            title = { Text("Eliminar apunte") },
            text = { Text("¿Quieres eliminar ‘${apunte.titulo}’? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarApunte(usuarioId, apunte.id) { correcto ->
                        if (correcto && apunteEnEdicionId == apunte.id) {
                            titulo = ""
                            contenido = ""
                            archivoPdfUri = null
                            apunteEnEdicionId = null
                        }
                        mensajeResultado = if (correcto) "Apunte eliminado." else "No se pudo eliminar el apunte."
                        resultadoCorrecto = correcto
                        mostrarResultado = true
                    }
                    apuntePendienteEliminar = null
                }) { Text("Eliminar", color = ColoresLexStudy.rojoError) }
            },
            dismissButton = {
                TextButton(onClick = { apuntePendienteEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun coloresCamposEstudio() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ColoresLexStudy.verdeTarjeta,
    focusedLabelColor = ColoresLexStudy.verdeTarjeta,
    cursorColor = ColoresLexStudy.verdeTarjeta,
    focusedTextColor = ColoresLexStudy.texto,
    unfocusedTextColor = ColoresLexStudy.texto
)
