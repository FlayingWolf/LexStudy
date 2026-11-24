package com.example.lexstudy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lexstudy.model.Apunte
import com.example.lexstudy.model.Ley
import com.example.lexstudy.model.MapaConceptual
import com.example.lexstudy.repository.LeyesDemo

class EstudioEstado {
    var leySeleccionada: Ley by mutableStateOf(LeyesDemo.normas.first())
        internal set

    var apuntes: List<Apunte> by mutableStateOf(emptyList())
        internal set

    var mapas: List<MapaConceptual> by mutableStateOf(emptyList())
        internal set
}
