package com.proyect.ocean_words.model
data class Zonas(
    val nombre: String="",
    val profundidad : String ="",
    val region :String=""
)
data class EspecieEstado (
    val _id : String ="",
    val nombre :String ="",
    val descripcion : String = "",
    val tipo : String ="",
    val habitat : String = "",
    val zonas: List<Zonas> = emptyList(), // ✅ usa tu clase Zonas
    val dificultad : String ="",
    val agua : String = "",
    val imagen : String ="",
    val Datos: String = ""

    )