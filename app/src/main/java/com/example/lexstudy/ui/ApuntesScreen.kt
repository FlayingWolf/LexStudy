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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lexstudy.model.Apunte

@Composable
fun ApuntesScreen(apuntes: List<Apunte>, onGuardar: (Apunte) -> Unit, modifier: Modifier = Modifier) {
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Estudiar y organizar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(6.dp))
        Text("Crea apuntes breves para guardar lo importante.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(titulo, { titulo = it; error = "" }, label = { Text("Título del apunte") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(contenido, { contenido = it; error = "" }, label = { Text("Contenido") }, minLines = 3, modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                when {
                    titulo.isBlank() -> error = "El título es obligatorio."
                    contenido.isBlank() -> error = "Escribe el contenido del apunte."
                    else -> { onGuardar(Apunte(titulo.trim(), contenido.trim())); titulo = ""; contenido = ""; error = "" }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar apunte") }
        Spacer(Modifier.height(18.dp))
        Text("Mis apuntes (${apuntes.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(apuntes) { apunte ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text(apunte.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(apunte.contenido)
                    }
                }
            }
        }
    }
}
