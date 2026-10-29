package com.example.lexstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lexstudy.ui.LexStudyApp
import com.example.lexstudy.ui.theme.LexStudyTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LexStudyTheme {
                Surface {
                    LexStudyApp()
                }
            }
        }
    }
}
