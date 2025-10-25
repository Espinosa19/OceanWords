package com.proyect.ocean_words.model
import com.proyect.ocean_words.R

enum class PlantType {
    SEAWEED, CORAL, ANEMONE, KELP // Ejemplos de especies
}

data class PistaEstado(
val id: Int,
val quantity: Int, // El número "8", "10", "15", "20"
val price: Int,    // El precio "$100", "$180", etc.
val type: PlantType, // Para identificar el ícono o tipo de especie
val iconResId: Int   // Referencia al ID del recurso drawable para el ícono
)

val sampleShopItems = listOf(
    PistaEstado(1, 8, 100, PlantType.SEAWEED, R.drawable.dolar), // Reemplaza ic_plant_x con tus drawables
    PistaEstado(2, 10, 180, PlantType.CORAL, R.drawable.dolar),
    PistaEstado(3, 15, 240, PlantType.ANEMONE, R.drawable.dolar),
    PistaEstado(4, 20, 290, PlantType.KELP, R.drawable.dolar)
)