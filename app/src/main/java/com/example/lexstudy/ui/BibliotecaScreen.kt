package com.example.lexstudy.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lexstudy.repository.Seccion
import com.example.lexstudy.viewmodel.BibliotecaViewModel

@Composable
fun BibliotecaScreen(volver: () -> Unit) {
    val modelo: BibliotecaViewModel = viewModel()
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            TextButton(onClick = volver) { Text("Volver a normas") }
            Text("Biblioteca en línea", style = MaterialTheme.typography.headlineSmall)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Seccion.entries.forEach {
                    FilterChip(selected = it == modelo.estadoPantalla.seccion, onClick = { modelo.acciones.seleccionar(it) }, label = { Text(it.etiqueta) })
                }
            }
            if (modelo.estadoPantalla.estado.isNotEmpty()) Text(modelo.estadoPantalla.estado)
            TextButton(onClick = { modelo.acciones.actualizar() }) { Text("Actualizar sección") }
        }
        items(modelo.estadoPantalla.fichas) { ficha ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(ficha.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(ficha.detalle)
                }
            }
        }
        item {
            OutlinedTextField(value = modelo.estadoPantalla.texto, onValueChange = modelo.acciones::editarSugerencia, enabled = !modelo.estadoPantalla.enviando,
                label = { Text("Sugiere un tema de estudio") }, supportingText = { Text("${modelo.estadoPantalla.texto.trim().length}/500") })
            Button(enabled = !modelo.estadoPantalla.enviando && modelo.estadoPantalla.texto.trim().length in 5..500, onClick = modelo.acciones::enviar) {
                Text(if (modelo.estadoPantalla.enviando) "Enviando…" else "Enviar sugerencia")
            }
            if (modelo.estadoPantalla.aviso.isNotEmpty()) Text(modelo.estadoPantalla.aviso)
        }
    }
}
