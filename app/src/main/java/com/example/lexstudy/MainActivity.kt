package com.example.lexstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lexstudy.ui.theme.LexStudyTheme
import com.example.lexstudy.ui.theme.*
import com.example.lexstudy.viewmodel.FormularioViewModel

class MainActivity : ComponentActivity() {

    private val viewModelForm = FormularioViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LexStudyTheme{ // <- aquí va el nombre de TU TEMA, lo puedes encontrar en ui/theme/Theme.kt
                //Login()
                Formulario(viewModelForm)
            }
        }
    }
}