package com.example.lexstudy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lexstudy.R
import com.example.lexstudy.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    var mostrarRegistro by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoresLexStudy.marfil)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.lexstudy_logo),
            contentDescription = "Logotipo de LexStudy",
            modifier = Modifier.size(96.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "LexStudy",
            style = MaterialTheme.typography.headlineLarge,
            color = ColoresLexStudy.verdePino,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = if (mostrarRegistro) "Crea tu cuenta de estudio" else "Consulta, estudia y organiza legislación",
            color = ColoresLexStudy.textoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = if (mostrarRegistro) "Crear cuenta" else "Iniciar sesión",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = ColoresLexStudy.verdePino
        )
        Spacer(Modifier.height(14.dp))

        if (mostrarRegistro) {
            CampoLexStudy(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre completo"
            )
            Spacer(Modifier.height(10.dp))
        }

        CampoLexStudy(
            value = correo,
            onValueChange = { correo = it },
            label = "Correo electrónico",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(10.dp))
        CampoLexStudy(
            value = clave,
            onValueChange = { clave = it },
            label = "Contraseña",
            password = true
        )

        if (mostrarRegistro) {
            Spacer(Modifier.height(10.dp))
            CampoLexStudy(
                value = confirmarClave,
                onValueChange = { confirmarClave = it },
                label = "Confirmar contraseña",
                password = true
            )
        }

        if (viewModel.mensajeError.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = viewModel.mensajeError,
                modifier = Modifier.fillMaxWidth(),
                color = ColoresLexStudy.rojoError,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(22.dp))
        BotonCargando(
            texto = if (mostrarRegistro) "Registrar" else "Ingresar",
            cargando = viewModel.estaCargando,
            onClick = {
                if (mostrarRegistro) {
                    viewModel.registrar(nombre, correo, clave, confirmarClave)
                } else {
                    viewModel.ingresar(correo, clave)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            habilitado = !viewModel.estaCargando
        )
        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = {
                mostrarRegistro = !mostrarRegistro
                viewModel.limpiarErrorParaCambiarFormulario()
            }
        ) {
            Text(
                text = if (mostrarRegistro) "Ya tengo una cuenta · Iniciar sesión" else "¿No tienes cuenta? · Registrar",
                color = ColoresLexStudy.verdePinoMedio
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "App en desarrollo",
            color = ColoresLexStudy.textoSecundario,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun CampoLexStudy(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColoresLexStudy.verdePinoMedio,
            focusedLabelColor = ColoresLexStudy.verdePinoMedio,
            cursorColor = ColoresLexStudy.verdePinoMedio,
            focusedTextColor = ColoresLexStudy.texto,
            unfocusedTextColor = ColoresLexStudy.texto
        )
    )
}
