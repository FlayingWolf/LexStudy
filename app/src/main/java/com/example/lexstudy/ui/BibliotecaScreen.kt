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
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val preferencias = remember(contexto) { contexto.getSharedPreferences("biblioteca", 0) }
    var base by remember { mutableStateOf(preferencias.getString("url", null) ?: com.example.lexstudy.BuildConfig.API_URL) }
    var editar by rememberSaveable { mutableStateOf(false) }
    var nuevaUrl by rememberSaveable { mutableStateOf(base) }
    var errorUrl by remember { mutableStateOf("") }
    if (editar) {
        AlertDialog(onDismissRequest = { editar = false }, title = { Text("Servidor de la biblioteca") },
            text = { Column {
                OutlinedTextField(value = nuevaUrl, onValueChange = { nuevaUrl = it }, singleLine = true,
                    label = { Text("URL del grupo de Xano") })
                Text(errorUrl.ifEmpty { "Deja el campo vacío para usar el servidor local por USB." })
            } },
            confirmButton = { TextButton(onClick = {
                try {
                    BibliotecaCliente.crear(nuevaUrl)
                    base = nuevaUrl.trim()
                    preferencias.edit().putString("url", base).apply()
                    editar = false
                } catch (e: Exception) { errorUrl = "Revisa la URL del grupo de API." }
            }) { Text("Guardar") } },
            dismissButton = { TextButton(onClick = { editar = false }) { Text("Cancelar") } })
    }
    val repositorio = remember(base) { BibliotecaRepository(BibliotecaCliente.crear(base)) }
    var seccion by rememberSaveable { mutableStateOf(Seccion.GLOSARIO) }
    var intento by remember { mutableIntStateOf(0) }
    var fichas by remember(base, seccion, intento) { mutableStateOf(emptyList<Ficha>()) }
    var estado by remember(base, seccion, intento) { mutableStateOf("Cargando…") }
    var texto by rememberSaveable { mutableStateOf("") }
    var aviso by rememberSaveable { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(base, seccion, intento) {
        try {
            fichas = repositorio.consultar(seccion)
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
            TextButton(enabled = !enviando, onClick = { nuevaUrl = base; errorUrl = ""; editar = true }) { Text("Configurar servidor") }
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
                        val id = repositorio.sugerir(texto)
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
