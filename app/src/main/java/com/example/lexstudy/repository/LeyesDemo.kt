package com.example.lexstudy.repository

import com.example.lexstudy.model.Ley

object LeyesDemo {
    val normas = listOf(
        Ley(
            id = "codigo-civil-1438",
            nombre = "Código Civil",
            articulo = "Art. 1438",
            materia = "Civil",
            resumen = "Ficha de estudio sobre el concepto de contrato y sus efectos jurídicos. Este texto es un resumen didáctico; consulta la fuente oficial para revisar el artículo íntegro.",
            referenciaOficial = "Biblioteca del Congreso Nacional de Chile · LeyChile",
            ultimaReferencia = "Referencia de catálogo: 2022"
        ),
        Ley(
            id = "codigo-trabajo-7",
            nombre = "Código del Trabajo",
            articulo = "Art. 7",
            materia = "Laboral",
            resumen = "Ficha de estudio sobre los elementos generales de una relación laboral. Este texto es un resumen didáctico; consulta la fuente oficial para revisar el artículo íntegro.",
            referenciaOficial = "Biblioteca del Congreso Nacional de Chile · LeyChile",
            ultimaReferencia = "Referencia de catálogo: 2024"
        ),
        Ley(
            id = "constitucion-19",
            nombre = "Constitución Política de la República",
            articulo = "Art. 19",
            materia = "Constitucional",
            resumen = "Ficha de estudio sobre garantías y derechos constitucionales. Este texto es un resumen didáctico; consulta la fuente oficial para revisar el artículo íntegro.",
            referenciaOficial = "Biblioteca del Congreso Nacional de Chile · LeyChile",
            ultimaReferencia = "Referencia de catálogo: 2023"
        ),
        Ley(
            id = "codigo-penal-1",
            nombre = "Código Penal",
            articulo = "Art. 1",
            materia = "Penal",
            resumen = "Ficha introductoria para estudiar los conceptos generales de la legislación penal. Este texto es un resumen didáctico; consulta la fuente oficial para revisar el artículo íntegro.",
            referenciaOficial = "Biblioteca del Congreso Nacional de Chile · LeyChile",
            ultimaReferencia = "Referencia de catálogo: 2021"
        )
    )

    fun buscarLey(id: String): Ley = normas.firstOrNull { it.id == id } ?: normas.first()
}
