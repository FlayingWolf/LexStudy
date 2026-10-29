package com.example.lexstudy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TextoPrincipal = Color(0xFF1F2D26)

private val PaletaLexStudy = lightColorScheme(
    primary = VerdePino,
    onPrimary = Color.White,
    secondary = VerdeMedio,
    onSecondary = Color.White,
    tertiary = Color(0xFFD4A75B),
    background = Marfil,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdePino
)

@Composable
fun LexStudyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PaletaLexStudy,
        content = content
    )
}
