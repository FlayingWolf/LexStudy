package com.example.lexstudy.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lexstudy.remote.*
import com.example.lexstudy.repository.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun BibliotecaScreen(volver: () -> Unit) {
    var seccion by rememberSaveable { mutableStateOf(Seccion.GLOSARIO) }
    var intento by remember { mutableIntStateOf(0) }
    var fichas by remember(seccion, intento) { mutableStateOf(emptyList<Ficha>()) }
    var estado by remember(seccion, intento) { mutableStateOf("Cargando…") }
    var texto by rememberSaveable { mutableStateOf("") }
    var aviso by rememberSaveable { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(seccion, intento) {
        try {
            fichas = BibliotecaRepository(BibliotecaCliente.api).consultar(seccion)
            estado = if (fichas.isEmpty()) "Todavía no hay fichas." else ""
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            estado = "No se pudo cargar la sección. ${e.localizedMessage.orEmpty()}"
        }
    }

    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            TextButton(onClick = volver) { Text("Volver a normas") }
            Text("Biblioteca en línea", style = MaterialTheme.typography.headlineSmall)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Seccion.entries.forEach {
                    FilterChip(selected = it == seccion, onClick = { seccion = it }, label = { Text(it.etiqueta) })
                }
            }
            if (estado.isNotEmpty()) Text(estado)
            TextButton(onClick = { intento++ }) { Text("Actualizar sección") }
        }
        items(fichas) { ficha ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(ficha.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(ficha.detalle)
                }
            }
        }
        item {
            OutlinedTextField(value = texto, onValueChange = { texto = it; aviso = "" }, enabled = !enviando,
                label = { Text("Sugiere un tema de estudio") }, supportingText = { Text("${texto.trim().length}/500") })
            Button(enabled = !enviando && texto.trim().length in 5..500, onClick = {
                enviando = true
                scope.launch {
                    try {
                        val id = BibliotecaRepository(BibliotecaCliente.api).sugerir(texto)
                        texto = ""
                        aviso = "Sugerencia recibida (#$id)."
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        aviso = "No se pudo confirmar el envío. ${e.localizedMessage.orEmpty()}"
                    } finally {
                        enviando = false
                    }
                }
            }) { Text(if (enviando) "Enviando…" else "Enviar sugerencia") }
            if (aviso.isNotEmpty()) Text(aviso)
        }
    }
}
