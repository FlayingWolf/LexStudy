package com.example.lexstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import com.example.lexstudy.ui.LexStudyApp
import com.example.lexstudy.ui.theme.LexStudyTheme

class MainActivity : ComponentActivity() {
    private val repositorio: LexStudyRepository by lazy { LexStudyRepository() }
    private val repositorioSesion: SesionRepository by lazy { SesionRepository() }
    private val repositorioFotoPerfil: FotoPerfilRepository by lazy {
        FotoPerfilRepository(applicationContext, repositorio)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LexStudyTheme {
                LexStudyApp(
                    repository = repositorio,
                    sesionRepository = repositorioSesion,
                    fotoPerfilRepository = repositorioFotoPerfil
                )
            }
        }
    }
}
