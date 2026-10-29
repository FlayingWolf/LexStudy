package com.example.lexstudy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lexstudy.model.MapaConceptual

@Composable
fun MapasConceptualesScreen(mapas: List<MapaConceptual>, onGuardar: (MapaConceptual) -> Unit, modifier: Modifier = Modifier) {
    var titulo by remember { mutableStateOf("") }
    var central by remember { mutableStateOf("") }
    var relacionadoUno by remember { mutableStateOf("") }
    var relacionadoDos by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Mapas conceptuales", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Representa ideas jurídicas y sus relaciones.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(titulo, { titulo = it; error = "" }, label = { Text("Nombre del mapa") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(central, { central = it; error = "" }, label = { Text("Concepto central") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(relacionadoUno, { relacionadoUno = it; error = "" }, label = { Text("Concepto relacionado 1") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(relacionadoDos, { relacionadoDos = it; error = "" }, label = { Text("Concepto relacionado 2") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            when {
                titulo.isBlank() || central.isBlank() -> error = "Completa el nombre y el concepto central."
                relacionadoUno.isBlank() || relacionadoDos.isBlank() -> error = "Agrega los dos conceptos relacionados."
                else -> { onGuardar(MapaConceptual(titulo.trim(), central.trim(), relacionadoUno.trim(), relacionadoDos.trim())); titulo = ""; central = ""; relacionadoUno = ""; relacionadoDos = ""; error = "" }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Guardar mapa") }
        Spacer(Modifier.height(14.dp))
        Text("Mis mapas (${mapas.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(mapas) { mapa ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(mapa.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) { Text(mapa.conceptoCentral, modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp), fontWeight = FontWeight.Bold) }
                        Text("↓", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineSmall)
                        Text("${mapa.conceptoRelacionadoUno}   •   ${mapa.conceptoRelacionadoDos}")
                    }
                }
            }
        }
    }
}
