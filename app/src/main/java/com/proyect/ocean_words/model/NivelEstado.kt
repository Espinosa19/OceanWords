package com.proyect.ocean_words.model

// Mantienes Especies_Nivel sin cambios.

// CORRECCIÓN CLAVE: El campo 'especie' ya no es una lista.
data class NivelEstado(
    // Campos directos del documento (se mapean automáticamente)
    val numero_nivel: Long? = 0,
    val vidas_iniciales: Long? = 0,
    val recompensa_monedas: Long? = 0,

    val especies_id: List<String?>? = null,    // Este campo se asignará en el Repository con el ID del Documento de Firestore
    val id: String = ""
) {
    // La función withFirebaseId está bien para asignar el ID del documento
    fun withFirebaseId(firebaseId: String): NivelEstado = this.copy(
        id = firebaseId,
        numero_nivel = this.numero_nivel,
        vidas_iniciales = this.vidas_iniciales,
        especies_id = this.especies_id,
        recompensa_monedas = this.recompensa_monedas
    )
}