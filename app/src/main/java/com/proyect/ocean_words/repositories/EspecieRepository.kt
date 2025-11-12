package com.proyect.ocean_words.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.proyect.ocean_words.model.EspecieEstado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
class EspecieRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val especiesRef = firestore.collection("especies")

    fun buscarEspeciePorId(claveBusqueda: String): Flow<EspecieEstado?> {
        return especiesRef
            .document(claveBusqueda)
            .snapshots()
            .map { documentSnapshot ->
                try {
                    val especie = documentSnapshot.toObject(EspecieEstado::class.java)

                    especie?.copy(_id = documentSnapshot.id)
                } catch (e: Exception) {
                    Log.e("EspecieRepository", "Error al mapear documento ${documentSnapshot.id}", e)
                    null
                }
            }
            .catch { exception ->
                Log.e("EspecieRepository", "Error en la escucha de Firestore", exception)
                throw exception
            }
    }
}
