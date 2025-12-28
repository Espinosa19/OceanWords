package com.proyect.ocean_words.viewmodels

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.proyect.ocean_words.model.EspecieEstado
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.model.progreso_Niveles
import com.proyect.ocean_words.domain.repositories.UsuarioRepository
import com.proyect.ocean_words.model.ProgresoLetra
import com.proyect.ocean_words.model.SlotEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProgresoViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    private val _usuarioLiveData = MutableLiveData<UsuariosEstado?>()
    val usuarioLiveData: LiveData<UsuariosEstado?> = _usuarioLiveData

    // Estado de progreso del usuario
    private val _progreso = MutableStateFlow<List<EspecieEstado>>(emptyList())
    val progreso: StateFlow<List<EspecieEstado>> = _progreso

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Mensajes de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error



    fun activarEscuchaTiempoReal() {
        val usuario = UserSession.currentUser ?: return

        listenUsuarioTiempoReal(usuario.id) { usuarioActualizado: UsuariosEstado? ->

            _usuarioLiveData.postValue(usuarioActualizado)

            usuarioActualizado?.progreso_niveles?.let { progreso ->
                Log.i("TiempoReal", "Progreso actualizado: $progreso")
            }
        }
    }
    fun listenUsuarioTiempoReal(
        userId: String,
        onChange: (UsuariosEstado?) -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    onChange(snapshot.toObject(UsuariosEstado::class.java))
                } else {
                    onChange(null)
                }
            }
    }fun buscarProgresoUsuId(
        level: Int,
        especieId: String,
        userId: String,
        completado: Boolean,
        letras: String, // formato esperado: "A-0,B-2,C-4"
    ) {
        val usuario: UsuariosEstado? = UserSession.currentUser

        if (usuario == null) {
            _error.value = "Usuario no logueado"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true

                val usuarioEncontrado = usuarioRepository
                    .buscarUsuarioPorId(usuario.id)
                    .first()

                if (usuarioEncontrado != null) {

                    val progresoActual =
                        usuarioEncontrado.progreso_niveles?.toMutableList() ?: mutableListOf()

                    val estadoNivel = if (completado) "completado" else "en_progreso"

                    val progresoExistente =
                        progresoActual.firstOrNull { it.nivel == level && it.id == especieId }
                    if (progresoExistente?.estado.equals("completado")) {
                        val indexProgreso = progresoActual.indexOf(progresoExistente)
                        val cantidadActual = progresoExistente?.cantidad_jugadas ?: 0
                        // Incrementamos la cantidad_jugadas si ya estaba completado
                        progresoActual[indexProgreso] = progresoExistente!!.copy(
                            cantidad_jugadas = cantidadActual + 1
                        )
                    } else{
                        // Convertimos las letras enviadas en pares (letra, índice)
                        val nuevasLetras = letras.split(",").mapNotNull {
                            val parts = it.split("-")
                            if (parts.size == 2) {
                                val char = parts[0].firstOrNull()
                                val index = parts[1].toIntOrNull()
                                if (char != null && index != null) char to index else null
                            } else null
                        }

                        if (progresoExistente == null) {
                            // Creamos un arreglo del tamaño máximo del índice +1 para no perder posiciones
                            val maxIndex = nuevasLetras.maxOfOrNull { it.second } ?: 0
                            val letrasArray = CharArray(maxIndex + 1) { ' ' }

                            nuevasLetras.forEach { (char, index) ->
                                letrasArray[index] = char
                            }

                            progresoActual.add(
                                progreso_Niveles(
                                    nivel = level,
                                    estado = estadoNivel,
                                    id = especieId,
                                    letra = String(letrasArray) ,
                                    cantidad_jugadas = if (completado) 1 else 0

                                )
                            )
                        } else {
                            val contenidoExistente = progresoExistente.letra
                            val letrasArray = contenidoExistente.toCharArray().toMutableList()

                            // Ajustamos el tamaño del array si viene alguna letra con índice más alto
                            val maxIndex = nuevasLetras.maxOfOrNull { it.second } ?: 0
                            while (letrasArray.size <= maxIndex) {
                                letrasArray.add(' ')
                            }

                            // Colocamos las nuevas letras en su posición
                            nuevasLetras.forEach { (char, index) ->
                                letrasArray[index] = char
                            }

                            val indexProgreso = progresoActual.indexOf(progresoExistente)
                            progresoActual[indexProgreso] = progresoExistente.copy(
                                estado = estadoNivel,
                                letra = letrasArray.joinToString("")
                            )
                        }
                    }


                    // Guardamos
                    usuarioRepository.actualizarProgresoUsuario(
                        usuarioEncontrado.id,
                        progresoActual
                    )

                    val usuarioActualizado =
                        usuarioEncontrado.copy(progreso_niveles = progresoActual)

                    _usuarioLiveData.value = usuarioActualizado
                    UserSession.currentUser = usuarioActualizado
                }

                _isLoading.value = false

            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }







}
