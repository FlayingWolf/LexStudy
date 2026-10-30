package com.example.lexstudy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.model.Ley
import com.example.lexstudy.repository.LeyesDemo

@Composable
fun ConsultarNormasScreen(onAbrirNorma: (Ley) -> Unit) {
    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    val categorias = listOf("Todas", "Civil", "Laboral", "Constitucional", "Penal")
    val normasFiltradas = remember(busqueda, categoriaSeleccionada) {
        LeyesDemo.normas.filter { ley ->
            val coincideTexto = listOf(ley.nombre, ley.articulo, ley.materia)
                .any { it.contains(busqueda.trim(), ignoreCase = true) }
            val coincideCategoria = categoriaSeleccionada == "Todas" || ley.materia == categoriaSeleccionada
            coincideTexto && coincideCategoria
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(ColoresLexStudy.marfil).padding(top = 18.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                "Consultar normas",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = ColoresLexStudy.verdePino
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Busca legislación por nombre, artículo o materia.",
                color = ColoresLexStudy.textoSecundario,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Buscar por materia o artículo...") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColoresLexStudy.verdePinoMedio,
                    focusedLabelColor = ColoresLexStudy.verdePinoMedio,
                    cursorColor = ColoresLexStudy.verdePinoMedio,
                    focusedTextColor = ColoresLexStudy.texto,
                    unfocusedTextColor = ColoresLexStudy.texto
                )
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria) }
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "${normasFiltradas.size} normas en el catálogo de demostración",
                fontSize = 11.sp,
                color = ColoresLexStudy.textoSecundario
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(normasFiltradas, key = { it.id }) { ley ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onAbrirNorma(ley) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColoresLexStudy.blanco),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                ley.materia.uppercase(),
                                color = ColoresLexStudy.verdePinoMedio,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                "REFERENCIA",
                                color = ColoresLexStudy.textoSecundario,
                                fontSize = 9.sp
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            ley.nombre,
                            color = ColoresLexStudy.verdePino,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(ley.articulo, color = ColoresLexStudy.texto, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            ley.resumen,
                            color = ColoresLexStudy.textoSecundario,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            maxLines = 3
                        )
                        Spacer(Modifier.height(9.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Abrir ficha de estudio",
                                color = ColoresLexStudy.verdePinoMedio,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("→", color = ColoresLexStudy.verdePinoMedio)
                        }
                    }
                }
            }
            if (normasFiltradas.isEmpty()) {
                item {
                    Text(
                        "No se encontraron normas con esos filtros.",
                        color = ColoresLexStudy.textoSecundario,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }
            item {
                Text(
                    "Contenido sintético para evaluación académica. Verifica siempre el texto vigente en LeyChile, Biblioteca del Congreso Nacional.",
                    color = ColoresLexStudy.textoSecundario,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )
            }
        }
    }
}
