package com.proyect.ocean_words.model

data class progreso_Niveles(
    val nivel: Int = 0,
    val estado: String ="",
    val vidad_restantes: Int=0,
    val id : String =""
)

data class UsuariosEstado(
    val nombre: String ="",
    val email : String ="",
    val monedas_obtenidas: Int = 0,
    val vidas_restantes: List<Boolean> = emptyList(),
    val pistas_obtenidas: Int= 0,
    val progreso_niveles : List<progreso_Niveles> = emptyList(),
    val id : String =""
) {
    constructor() :this("","",0,emptyList(),0,emptyList(),"")
    fun withFirebaseId(firebaseId: String): UsuariosEstado = copy(id = firebaseId)


}