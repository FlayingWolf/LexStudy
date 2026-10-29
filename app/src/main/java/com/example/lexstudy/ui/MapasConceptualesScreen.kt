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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
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

@Composable
fun MapasConceptualesScreen(
    mapas: List<MapaConceptual>,
    onGuardar: (MapaConceptual) -> Unit,
    onEliminar: (MapaConceptual) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarEditor by remember { mutableStateOf(false) }
    var mostrarAsistente by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var central by remember { mutableStateOf("") }
    var uno by remember { mutableStateOf("") }
    var dos by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarMensaje by remember { mutableStateOf(false) }
    var mapaPendienteEliminar by remember { mutableStateOf<MapaConceptual?>(null) }

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Mapas conceptuales", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Organiza las ideas principales de una norma y las relaciones entre conceptos.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("MAPA DE ESTUDIO", color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("De una norma a sus conceptos clave", color = MaterialTheme.colorScheme.primary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("Crea un esquema manual o genera un ejemplo local. La opción asistida es una simulación académica; no utiliza una IA externa.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { mostrarEditor = true }, modifier = Modifier.weight(1f)) { Text("Manual") }
                    Button(onClick = { mostrarAsistente = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) { Text("Con IA · demo") }
                }
            }
        }
        Text("Mis mapas · ${mapas.size}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        if (mapas.isEmpty()) {
            Text("Todavía no has guardado mapas. Crea el primero con el botón Manual.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        } else {
            mapas.forEach { mapa ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(mapa.titulo, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(Modifier.height(10.dp))
                        Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)) {
                            Text(mapa.conceptoCentral, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold)
                        }
                        Text("↙                         ↘", color = MaterialTheme.colorScheme.secondary, fontSize = 22.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Surface(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(10.dp)) {
                                Text(mapa.conceptoRelacionadoUno, modifier = Modifier.padding(10.dp), color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp)
                            }
                            Surface(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(10.dp)) {
                                Text(mapa.conceptoRelacionadoDos, modifier = Modifier.padding(10.dp), color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp)
                            }
                        }
                        TextButton(onClick = { mapaPendienteEliminar = mapa }, modifier = Modifier.align(Alignment.End)) { Text("Eliminar mapa", color = MaterialTheme.colorScheme.error) }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Los mapas se conservan en memoria en esta etapa. La persistencia se añade en el siguiente paso.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }

    if (mostrarEditor) {
        AlertDialog(
            onDismissRequest = { mostrarEditor = false },
            title = { Text("Nuevo mapa conceptual") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(titulo, { titulo = it }, label = { Text("Título del mapa") }, singleLine = true)
                    OutlinedTextField(central, { central = it }, label = { Text("Concepto central") }, singleLine = true)
                    OutlinedTextField(uno, { uno = it }, label = { Text("Concepto relacionado 1") }, singleLine = true)
                    OutlinedTextField(dos, { dos = it }, label = { Text("Concepto relacionado 2") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    when {
                        titulo.isBlank() || central.isBlank() || uno.isBlank() || dos.isBlank() -> {
                            mensaje = "Completa el título y los tres conceptos."
                            mostrarMensaje = true
                        }
                        else -> {
                            onGuardar(MapaConceptual(titulo.trim(), central.trim(), uno.trim(), dos.trim()))
                            titulo = ""; central = ""; uno = ""; dos = ""
                            mostrarEditor = false
                        }
                    }
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { mostrarEditor = false }) { Text("Cancelar") } }
        )
    }

    if (mostrarAsistente) {
        AlertDialog(
            onDismissRequest = { mostrarAsistente = false },
            title = { Text("Propuesta asistida · demo") },
            text = { Text("Se creará un ejemplo local sobre los elementos generales de un contrato. No se enviará información a un servicio externo.") },
            confirmButton = {
                TextButton(onClick = {
                    onGuardar(MapaConceptual("Elementos de un contrato (demo)", "Contrato", "Acuerdo entre partes", "Efectos jurídicos"))
                    mostrarAsistente = false
                    mensaje = "Mapa de demostración creado."
                    mostrarMensaje = true
                }) { Text("Crear ejemplo") }
            },
            dismissButton = { TextButton(onClick = { mostrarAsistente = false }) { Text("Cancelar") } }
        )
    }

    if (mapaPendienteEliminar != null) {
        val mapa = mapaPendienteEliminar!!
        AlertDialog(
            onDismissRequest = { mapaPendienteEliminar = null },
            title = { Text("Eliminar mapa") },
            text = { Text("¿Quieres eliminar el mapa ‘${mapa.titulo}’? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(mapa)
                    mapaPendienteEliminar = null
                    mensaje = "Mapa eliminado."
                    mostrarMensaje = true
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { mapaPendienteEliminar = null }) { Text("Cancelar") } }
        )
    }

    if (mostrarMensaje) {
        AlertDialog(onDismissRequest = { mostrarMensaje = false }, title = { Text("Aviso") }, text = { Text(mensaje) }, confirmButton = { TextButton(onClick = { mostrarMensaje = false }) { Text("Aceptar") } })
    }
}
