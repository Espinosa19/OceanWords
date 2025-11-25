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
    val progreso_niveles : List<progreso_Niveles> = emptyList(),
    val id : String =""
) {
    constructor() :this("","",emptyList(),"")
    fun withFirebaseId(firebaseId: String): UsuariosEstado = copy(id = firebaseId)


}