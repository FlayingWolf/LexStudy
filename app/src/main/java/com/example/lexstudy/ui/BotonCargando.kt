package com.example.lexstudy.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BotonCargando(
    texto: String,
    cargando: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    color: Color = ColoresLexStudy.verdePinoMedio
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = habilitado && !cargando,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = ColoresLexStudy.blanco,
            disabledContainerColor = color.copy(alpha = 0.65f),
            disabledContentColor = ColoresLexStudy.blanco
        )
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = ColoresLexStudy.blanco
            )
        } else {
            Text(texto)
        }
    }
}
