package com.proyect.ocean_words.model

// Define el estado de la letra para saber qué color usar
data class SlotEstado(
    val char: Char? = null,
    val esCorrecto: Boolean? = null // null: no validado, true: correcto, false: incorrecto
)