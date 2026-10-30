package com.example.lexstudy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = pinoClaro,
    onPrimary = pinoOscuro,
    primaryContainer = pinoMedio,
    onPrimaryContainer = Color.White,
    secondary = pinoMedio,
    onSecondary = Color.White,
    secondaryContainer = pinoOscuro,
    onSecondaryContainer = pinoClaro,
    tertiary = pinoClaro,
    onTertiary = pinoOscuro,
    background = pinoOscuro,
    onBackground = textoClaro,
    surface = pinoOscuro,
    onSurface = textoClaro,
    outline = bordeSuave,
    error = rojoError
)

private val LightColorScheme = lightColorScheme(
    primary = pinoPrimario,
    onPrimary = Color.White,
    primaryContainer = pinoClaro,
    onPrimaryContainer = pinoOscuro,
    secondary = pinoMedio,
    onSecondary = Color.White,
    secondaryContainer = pinoClaro,
    onSecondaryContainer = pinoOscuro,
    tertiary = pinoMedio,
    onTertiary = Color.White,
    background = fondoMarfil,
    onBackground = textoBosque,
    surface = Color.White,
    onSurface = textoBosque,
    outline = bordeSuave,
    error = rojoError
)

@Composable
fun LexStudyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
