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
    }
    fun buscarProgresoUsuId(
        level: Int,
        especieId: String,
        userId: String,
        completado: Boolean,
        letras: String,
    ) {
        val usuario: UsuariosEstado? = UserSession.currentUser

        if (usuario == null) {
            _error.value = "Usuario no logueado"
            return
        }

        viewModelScope.launch {
            try {
                usuarioRepository.buscarUsuarioPorId(usuario.id)
                    .onStart { _isLoading.value = completado }
                    .catch { exception ->
                        _error.value = "Error al cargar el progreso: ${exception.message}"
                        _progreso.value = emptyList()
                        _isLoading.value = false
                    }
                    .collect { usuarioEncontrado ->
                        if (usuarioEncontrado != null) {
                            val progresoActual = usuarioEncontrado.progreso_niveles?.toMutableList() ?: mutableListOf()
                            Log.i("progresoActual", "$progresoActual")

                            val estadoNivel = if (completado) "completado" else "en_progreso"

                            // ✅ Buscamos si ya existe un progreso para este nivel y especie
                            val progresoExistente = progresoActual.firstOrNull { it.nivel == level && it.id == especieId }

                            if (progresoExistente == null) {
                                // Nivel no existe → agregamos nuevo
                                val nuevoProgreso = progreso_Niveles(
                                    nivel = level,
                                    estado = estadoNivel,
                                    id = especieId,
                                    letra = letras
                                )
                                progresoActual.add(nuevoProgreso)
                            } else {
                                // Nivel ya existe → actualizamos el existente
                                val progresoActualizadoNivel = progresoExistente.copy(
                                    estado = estadoNivel,
                                    letra = letras
                                )
                                val index = progresoActual.indexOf(progresoExistente)
                                if (index != -1) {
                                    progresoActual[index] = progresoActualizadoNivel
                                }
                            }

                            // Guardamos en Firebase y actualizamos LiveData / sesión
                            usuarioRepository.actualizarProgresoUsuario(usuarioEncontrado.id, progresoActual)
                            val usuarioActualizado = usuarioEncontrado.copy(progreso_niveles = progresoActual)
                            _usuarioLiveData.value = usuarioActualizado
                            UserSession.currentUser = usuarioActualizado

                            Log.i("progresoGuardado", "${_usuarioLiveData.value}")
                            _isLoading.value = false
                        } else {
                            _error.value = "No se encontró el usuario"
                            _progreso.value = emptyList()
                            _isLoading.value = false
                        }
                    }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.message}"
                _progreso.value = emptyList()
                _isLoading.value = false
            }
        }
    }





}
