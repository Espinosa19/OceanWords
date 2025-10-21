package com.proyect.ocean_words.model

// Mantienes Especies_Nivel sin cambios.
data class Especies_Nivel(
    val _id: String = "", // Usamos _id para coincidir con tu BD
    val nombre: String = "",
    val dificultad: String ="",
    val imagen: String =""
)

// CORRECCIÓN CLAVE: El campo 'especie' ya no es una lista.
data class NivelEstado(
    // Campos directos del documento (se mapean automáticamente)
    val numero_nivel: Long? = 0,
    val vidas_iniciales: Long? = 0,
    val recompensa_monedas: Long? = 0,

    // ¡CAMBIO AQUÍ! Ahora es un objeto único (un mapa anidado).
    val especie: Especies_Nivel? = null,

    // Este campo se asignará en el Repository con el ID del Documento de Firestore
    val id: String = ""
) {
    // La función withFirebaseId está bien para asignar el ID del documento
    fun withFirebaseId(firebaseId: String): NivelEstado = this.copy(
        id = firebaseId,
        numero_nivel = this.numero_nivel,
        vidas_iniciales = this.vidas_iniciales,
        especie = this.especie,
        recompensa_monedas = this.recompensa_monedas
    )
}