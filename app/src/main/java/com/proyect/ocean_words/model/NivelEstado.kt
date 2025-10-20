package com.proyect.ocean_words.model
data class Especies_Nivel(
    val id :String = "",
    val nombre : String = "",
    val dificultad : String ="",
    val imagen : String =""
)
data class NivelEstado (
    val id : String = "",
    val numero_nivel :Number =0,
    val vidas_iniciales : Number =0,
    val especie : List <Especies_Nivel> = emptyList(),
    val recompensa_monedas : Number = 0
)