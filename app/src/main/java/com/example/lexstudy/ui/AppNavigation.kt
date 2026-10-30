package com.example.lexstudy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import com.example.lexstudy.viewmodel.AuthViewModel
import com.example.lexstudy.viewmodel.AuthViewModelFactory
import com.example.lexstudy.viewmodel.LexStudyViewModel
import com.example.lexstudy.viewmodel.LexStudyViewModelFactory

private data class ItemNavegacion(
    val ruta: String,
    val etiqueta: String,
    val simbolo: String
)

@Composable
fun LexStudyApp(
    repository: LexStudyRepository,
    sesionRepository: SesionRepository,
    fotoPerfilRepository: FotoPerfilRepository
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = remember(repository, sesionRepository, fotoPerfilRepository) {
            AuthViewModelFactory(repository, sesionRepository, fotoPerfilRepository)
        }
    )
    val studyViewModel: LexStudyViewModel = viewModel(factory = remember(repository) { LexStudyViewModelFactory(repository) })
    val usuario = authViewModel.usuarioActual

    LaunchedEffect(usuario?.id, authViewModel.estaRestaurandoSesion) {
        if (!authViewModel.estaRestaurandoSesion) {
            if (usuario == null) {
                studyViewModel.limpiarDatosDeSesion()
            } else {
                studyViewModel.cargarDatos(usuario.id)
            }
        }
    }

    if (authViewModel.estaRestaurandoSesion) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ColoresLexStudy.verdePinoMedio)
        }
    } else if (usuario == null) {
        LoginScreen(viewModel = authViewModel)
    } else {
        NavegacionPrincipal(
            usuario = usuario,
            authViewModel = authViewModel,
            studyViewModel = studyViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavegacionPrincipal(
    usuario: Usuario,
    authViewModel: AuthViewModel,
    studyViewModel: LexStudyViewModel
) {
    val nombreUsuario = usuario.nombre
    val usuarioId = usuario.id
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route ?: "consultar"
    val rutaSeccionActual = if (rutaActual == "detalle_norma") "estudiar" else rutaActual
    var mostrarConfirmacionSalida by remember { mutableStateOf(false) }

    val items = listOf(
        ItemNavegacion("consultar", "Consultar", "⌕"),
        ItemNavegacion("estudiar", "Estudiar", "▤"),
        ItemNavegacion("mapas", "Mapas", "⌘"),
        ItemNavegacion("perfil", "Perfil", "☺")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LexStudy", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Hola, $nombreUsuario", fontSize = 11.sp)
                    }
                },
                actions = {
                    TextButton(onClick = { mostrarConfirmacionSalida = true }) {
                        Text("Salir", color = ColoresLexStudy.blanco)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColoresLexStudy.verdePino,
                    titleContentColor = ColoresLexStudy.blanco
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = ColoresLexStudy.verdePino) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = rutaSeccionActual == item.ruta,
                        onClick = {
                            navController.navigate(item.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        },
                        icon = { Text(item.simbolo, fontSize = 20.sp) },
                        label = { Text(item.etiqueta, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColoresLexStudy.blanco,
                            selectedTextColor = ColoresLexStudy.blanco,
                            indicatorColor = ColoresLexStudy.verdePinoMedio,
                            unselectedIconColor = ColoresLexStudy.blanco.copy(alpha = 0.65f),
                            unselectedTextColor = ColoresLexStudy.blanco.copy(alpha = 0.65f)
                        )
                    )
                }
            }
        },
        containerColor = ColoresLexStudy.marfil
    ) { paddingInterno ->
        NavHost(
            navController = navController,
            startDestination = "consultar",
            modifier = Modifier.padding(paddingInterno)
        ) {
            composable("consultar") {
                ConsultarNormasScreen(onAbrirNorma = { ley ->
                    studyViewModel.seleccionarLey(ley)
                    navController.navigate("detalle_norma") { launchSingleTop = true }
                })
            }
            composable("estudiar") {
                EstudiarOrganizarScreen(usuarioId = usuarioId, viewModel = studyViewModel)
            }
            composable("detalle_norma") {
                EstudiarOrganizarScreen(usuarioId = usuarioId, viewModel = studyViewModel)
            }
            composable("mapas") {
                MapasConceptualesScreen(usuarioId = usuarioId, viewModel = studyViewModel)
            }
            composable("perfil") {
                PerfilScreen(usuario = usuario, viewModel = authViewModel)
            }
        }
    }

    if (mostrarConfirmacionSalida) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionSalida = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres salir de tu cuenta de LexStudy?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarConfirmacionSalida = false
                    authViewModel.cerrarSesion()
                }) { Text("Cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionSalida = false }) { Text("Seguir estudiando") }
            }
        )
    }
}
