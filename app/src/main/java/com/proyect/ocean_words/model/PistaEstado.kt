package com.proyect.ocean_words.model
import com.proyect.ocean_words.R

enum class PaqueteType {
    MONEDAS, PISTAS
}

data class PistaEstado(
val id: Int,
val quantity: Int, // El número "8", "10", "15", "20"
val price: Int,    // El precio "$100", "$180", etc.
val type: PaqueteType, // Para identificar el ícono o tipo de especie
val iconResId: Int   // Referencia al ID del recurso drawable para el ícono
)

val sampleShopItems = listOf(
    PistaEstado(1, 8, 100, PaqueteType.MONEDAS, R.drawable.dolar), // Reemplaza ic_plant_x con tus drawables
    PistaEstado(2, 10, 180, PaqueteType.MONEDAS, R.drawable.dolar),
    PistaEstado(3, 15, 240, PaqueteType.MONEDAS, R.drawable.dolar),
    PistaEstado(4, 20, 290, PaqueteType.MONEDAS, R.drawable.dolar)
)
val vidasShopItems = listOf(
    PistaEstado(5,5,125, PaqueteType.PISTAS,R.drawable.vidas),
    PistaEstado(6,8,200, PaqueteType.PISTAS,R.drawable.vidas),
    PistaEstado(7,10,250, PaqueteType.PISTAS,R.drawable.vidas),
    PistaEstado(8,15,375, PaqueteType.PISTAS,R.drawable.vidas)



)