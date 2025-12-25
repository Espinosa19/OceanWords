package com.proyect.ocean_words.domain.repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.model.progreso_Niveles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UsuarioRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val _monedasUsuario = MutableStateFlow<Int?>(null)
    val monedasUsuario: StateFlow<Int?> = _monedasUsuario
    private val usuarioRef = firestore.collection("usuarios")
    private val _vidas = MutableStateFlow<List<Boolean>?>(null)
    val vidas: StateFlow<List<Boolean>?> = _vidas
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
    fun crearUsuario(uid: String, email: String, nombre: String): Flow<UsuariosEstado?> {
        // Primero crear el usuario si no existe
        usuarioRef.document(uid).set(
            UsuariosEstado(nombre = nombre, email = email, monedas_obtenidas = 0, pistas_obtenidas = 1,id = uid, progreso_niveles = emptyList())
        )

        // Luego retornamos el flow para escuchar cambios en tiempo real
        return buscarUsuarioPorId(uid)
    }

    suspend fun descontarMonedas(uid: String, monedas: Int): Result<Unit> {

        if (uid.isBlank()) return Result.failure(Exception("UID vacío"))
        if (monedas <= 0) return Result.failure(Exception("Monto inválido"))

        return try {
            usuarioRef.document(uid)
                .update("monedas_obtenidas", FieldValue.increment(-monedas.toLong()))
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun descontarPistas(uid: String, unaPistaMenos: Int): Result<Unit> {

        if (uid.isBlank()) return Result.failure(Exception("UID vacío"))
        if (unaPistaMenos <= 0) return Result.failure(Exception("Monto inválido"))

        return try {
            usuarioRef.document(uid)
                .update("pistas_obtenidas", FieldValue.increment(-unaPistaMenos.toLong()))
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observarUsuario(id: String): Flow<UsuariosEstado?> {
        return buscarUsuarioPorId(id)
    }


    suspend fun actualizarVidas(id: String?, vidas: List<Boolean>?) {

        // 1️⃣ Validaciones básicas
        if (id.isNullOrBlank()) {
            Log.e("Firestore", "❌ ID de usuario nulo o vacío")
            return
        }

        if (vidas.isNullOrEmpty()) {
            Log.e("Firestore", "❌ Lista de vidas nula o vacía")
            return
        }

        if (vidas.any { it !is Boolean }) {
            Log.e("Firestore", "❌ Lista de vidas inválida")
            return
        }

        try {
            // 2️⃣ Referencia segura
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("usuarios").document(id)

            // 3️⃣ Verificar que el documento exista
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                Log.e("Firestore", "❌ El documento del usuario no existe: $id")
                return
            }

            // 4️⃣ Actualizar campo
            docRef
                .update("vidas_restantes", vidas)
                .await()

            Log.d("Firestore", "✅ Vidas actualizadas correctamente: $vidas")

        } catch (e: Exception) {
            Log.e("Firestore", "🔥 Error al actualizar vidas", e)
        }
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
