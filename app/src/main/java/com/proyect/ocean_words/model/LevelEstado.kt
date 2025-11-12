package com.proyect.ocean_words.model


data class LevelEstado(
    val id: Int,
    val especie_id:String,
    val nombreEspecie: String,
    val dificultad: String,
    val imagen: String,
    val isUnlocked: Boolean
)
