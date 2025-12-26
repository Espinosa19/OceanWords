package com.proyect.ocean_words.model

data class CustomDialogConfig(
    val title: String,
    val imageRes: Int,
    val headline: String,
    val message: String,
    val leftButtonText: String,
    val rightButtonText: String = "" // opcional
)
