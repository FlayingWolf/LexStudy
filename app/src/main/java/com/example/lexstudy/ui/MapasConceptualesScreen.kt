package com.example.lexstudy.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.viewmodel.LexStudyViewModel

@Composable
fun MapasConceptualesScreen(
    usuarioId: Long,
    viewModel: LexStudyViewModel
) {
    var mostrarEditor by remember { mutableStateOf(false) }
    var mostrarAsistente by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var conceptoCentral by remember { mutableStateOf("") }
    var conceptoUno by remember { mutableStateOf("") }
    var conceptoDos by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarMensaje by remember { mutableStateOf(false) }
    var esError by remember { mutableStateOf(false) }
    var mapaPendienteEliminar by remember { mutableStateOf<MapaConceptual?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(ColoresLexStudy.marfil).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text("Mapas conceptuales", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = ColoresLexStudy.verdePino)
        Spacer(Modifier.height(5.dp))
        Text("Organiza ideas principales y relaciones entre conceptos.", color = ColoresLexStudy.textoSecundario, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColoresLexStudy.verdePinoClaro)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("MAPA DE ESTUDIO", color = ColoresLexStudy.verdeAcento, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("De una norma a sus conceptos clave", color = ColoresLexStudy.verdePino, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("Crea un esquema manual o prueba una propuesta de ejemplo. La opción asistida está simulada en este MVP y no utiliza un servicio externo de IA.", color = ColoresLexStudy.textoSecundario, fontSize = 12.sp, lineHeight = 17.sp)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { mostrarEditor = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColoresLexStudy.blanco, contentColor = ColoresLexStudy.verdeAcento)
                    ) { Text("Manual") }
                    Button(
                        onClick = { mostrarAsistente = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColoresLexStudy.verdeAcento)
                    ) { Text("Con IA · demo") }
                }
            }
        }

        Spacer(Modifier.height(22.dp))
        Text("Mis mapas · ${viewModel.mapas.size}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = ColoresLexStudy.verdePino)
        Spacer(Modifier.height(10.dp))

        if (viewModel.mapas.isEmpty()) {
            Text("Todavía no has guardado mapas. Crea el primero con el botón Manual.", color = ColoresLexStudy.textoSecundario, fontSize = 12.sp)
        } else {
            viewModel.mapas.forEach { mapa ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = ColoresLexStudy.blanco)
                ) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(mapa.titulo, color = ColoresLexStudy.verdePino, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(Modifier.height(10.dp))
                        Surface(color = ColoresLexStudy.verdeAcento, shape = RoundedCornerShape(12.dp)) {
                            Text(mapa.conceptoCentral, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = ColoresLexStudy.blanco, fontWeight = FontWeight.SemiBold)
                        }
                        Text("↙                         ↘", color = ColoresLexStudy.verdeAcento, fontSize = 22.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = ColoresLexStudy.verdePinoClaro,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(mapa.conceptoRelacionadoUno, modifier = Modifier.padding(10.dp), color = ColoresLexStudy.texto, fontSize = 12.sp)
                            }
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = ColoresLexStudy.verdePinoClaro,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(mapa.conceptoRelacionadoDos, modifier = Modifier.padding(10.dp), color = ColoresLexStudy.texto, fontSize = 12.sp)
                            }
                        }
                        TextButton(onClick = { mapaPendienteEliminar = mapa }, modifier = Modifier.align(Alignment.End)) {
                            Text("Eliminar mapa", color = ColoresLexStudy.rojoError)
                        }
                    }
                }
            }
        }
    }

    if (mostrarEditor) {
        AlertDialog(
            onDismissRequest = { mostrarEditor = false },
            title = { Text("Nuevo mapa conceptual") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoMapa(titulo, { titulo = it }, "Título del mapa")
                    CampoMapa(conceptoCentral, { conceptoCentral = it }, "Concepto central")
                    CampoMapa(conceptoUno, { conceptoUno = it }, "Concepto relacionado 1")
                    CampoMapa(conceptoDos, { conceptoDos = it }, "Concepto relacionado 2")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.guardarMapa(usuarioId, titulo, conceptoCentral, conceptoUno, conceptoDos) { correcto, respuesta ->
                        esError = !correcto
                        mensaje = respuesta
                        mostrarMensaje = true
                        if (correcto) {
                            mostrarEditor = false
                            titulo = ""
                            conceptoCentral = ""
                            conceptoUno = ""
                            conceptoDos = ""
                        }
                    }
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEditor = false }) { Text("Cancelar") }
            }
        )
    }

    if (mostrarAsistente) {
        AlertDialog(
            onDismissRequest = { mostrarAsistente = false },
            title = { Text("Propuesta asistida · demo") },
            text = { Text("Para la evaluación se creará un ejemplo local sobre los elementos de un contrato. No se enviará contenido a una IA ni a un servicio externo.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.guardarMapa(
                        usuarioId = usuarioId,
                        titulo = "Elementos de un contrato (demo)",
                        conceptoCentral = "Contrato",
                        conceptoUno = "Acuerdo entre partes",
                        conceptoDos = "Efectos jurídicos"
                    ) { correcto, respuesta ->
                        esError = !correcto
                        mensaje = respuesta
                        mostrarMensaje = true
                    }
                    mostrarAsistente = false
                }) { Text("Crear ejemplo") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAsistente = false }) { Text("Cancelar") }
            }
        )
    }

    if (mostrarMensaje) {
        AlertDialog(
            onDismissRequest = { mostrarMensaje = false },
            title = { Text(if (esError) "Revisa los datos" else "Listo") },
            text = { Text(mensaje) },
            confirmButton = { TextButton(onClick = { mostrarMensaje = false }) { Text("Aceptar") } }
        )
    }

    mapaPendienteEliminar?.let { mapa ->
        AlertDialog(
            onDismissRequest = { mapaPendienteEliminar = null },
            title = { Text("Eliminar mapa") },
            text = { Text("¿Quieres eliminar el mapa ‘${mapa.titulo}’?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarMapa(usuarioId, mapa.id) { correcto ->
                        esError = !correcto
                        mensaje = if (correcto) "Mapa eliminado." else "No se pudo eliminar el mapa."
                        mostrarMensaje = true
                    }
                    mapaPendienteEliminar = null
                }) { Text("Eliminar", color = ColoresLexStudy.rojoError) }
            },
            dismissButton = { TextButton(onClick = { mapaPendienteEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun CampoMapa(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColoresLexStudy.verdeAcento,
            focusedLabelColor = ColoresLexStudy.verdeAcento,
            cursorColor = ColoresLexStudy.verdeAcento
        )
    )
}
