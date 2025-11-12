package com.proyect.ocean_words.repositories

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.proyect.ocean_words.model.NivelEstado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class NivelRepository(
    // Inyección de la instancia de Firestore.
    // Aunque tiene valor por defecto, se recomienda la inyección por DI.
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val nivelesRef = firestore.collection("niveles")

    /**
     * Retorna un Flow que emite List<NivelEstado> cada vez que los documentos en Firestore cambian.
     */
    fun mostrarNiveles(): Flow<List<NivelEstado>> {

        return nivelesRef
            .orderBy("numero_nivel", Query.Direction.ASCENDING)
            .snapshots()
            .map { querySnapshot ->
                val nivelesList = mutableListOf<NivelEstado>()

                for (document in querySnapshot.documents) {
                    try {
                        val nivelEstado = document.toObject(NivelEstado::class.java)

                        if (nivelEstado != null) {
                            nivelesList.add(nivelEstado.withFirebaseId(document.id))
                        }
                    } catch (e: Exception) {
                        Log.e("NivelRepository", "Error al mapear documento ${document.id}", e)
                    }
                }
                return@map nivelesList
            }
            .catch { exception ->
                Log.e("NivelRepository", "Error en la escucha de Firestore", exception)
                throw exception
            }
    }
}