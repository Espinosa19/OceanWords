package com.proyect.ocean_words.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.model.progreso_Niveles
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UsuarioRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val usuarioRef = firestore.collection("usuarios")

    /**
     * Crea un usuario en Firestore con el UID proporcionado
     */
    fun crearYObservarUsuario(uid: String, email: String, nombre: String): Flow<UsuariosEstado?> {
        // Primero crear el usuario si no existe
        usuarioRef.document(uid).set(
            UsuariosEstado(nombre = nombre, email = email, id = uid, progreso_niveles = emptyList())
        )

        // Luego retornamos el flow para escuchar cambios en tiempo real
        return buscarUsuarioPorId(uid)
    }


    /**
     * Escucha los cambios del usuario en tiempo real
     */
    fun buscarUsuarioPorId(uid: String): Flow<UsuariosEstado?> {
        return usuarioRef
            .document(uid)
            .snapshots() // Escucha en tiempo real el documento
            .map { documentSnapshot ->
                try {
                    // Convertimos el snapshot en nuestra clase UsuariosEstado
                    val usuario = documentSnapshot.toObject(UsuariosEstado::class.java)
                    // Aseguramos que el id esté siempre asignado
                    usuario?.copy(id = documentSnapshot.id)
                } catch (e: Exception) {
                    Log.e("UsuarioRepository", "Error al mapear documento ${documentSnapshot.id}", e)
                    null
                }
            }
            .catch { exception ->
                Log.e("UsuarioRepository", "Error en la escucha de Firestore", exception)
                throw exception
            }
    }
    fun actualizarProgresoUsuario(uid: String, nuevosProgresos: List<progreso_Niveles>) {
        usuarioRef.document(uid)
            .update("progreso_niveles", nuevosProgresos)
            .addOnSuccessListener { Log.i("Firestore", "Progreso actualizado") }
            .addOnFailureListener { e -> Log.e("Firestore", "Error al actualizar progreso: ${e.message}") }
    }

}
