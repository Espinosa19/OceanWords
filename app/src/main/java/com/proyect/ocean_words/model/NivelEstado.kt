package com.proyect.ocean_words.model
data class EspecieNivel(
    var id: String = "",
    var nombre: String = "",
    var dificultad: String = "",
    var imagen: String = ""
) {
    constructor() : this("", "", "", "")
}

data class NivelEstado(
    val numero_nivel: Int = 0,
    val vidas_iniciales: Long = 0,
    val recompensa_monedas: Long = 0,
    val especies_id: List<EspecieNivel> = emptyList(),
    val id: String = ""
) {
    constructor() : this(0, 0, 0, emptyList(), "")

    fun withFirebaseId(firebaseId: String): NivelEstado = copy(id = firebaseId)
}
