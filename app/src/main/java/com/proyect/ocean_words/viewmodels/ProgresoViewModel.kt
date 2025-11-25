package com.proyect.ocean_words.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.proyect.ocean_words.auth.UserSession
import com.proyect.ocean_words.model.EspecieEstado
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.model.progreso_Niveles
import com.proyect.ocean_words.repositories.UsuarioRepository
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


    fun buscarProgresoUsuId(level: Int, especieId: String) {
        val usuario: UsuariosEstado? = UserSession.currentUser

        if (usuario == null) {
            _error.value = "Usuario no logueado"
            return
        }

        viewModelScope.launch {
            try {
                usuarioRepository.buscarUsuarioPorId(usuario.id)
                    .onStart { _isLoading.value = true }
                    .catch { exception ->
                        _error.value = "Error al cargar el progreso: ${exception.message}"
                        _progreso.value = emptyList()
                        _isLoading.value = false
                    }
                    .collect { usuarioEncontrado ->
                        if (usuarioEncontrado != null) {
                            val progresoActual = usuarioEncontrado.progreso_niveles?.toMutableList() ?: mutableListOf()
                            Log.i("progresoActual", "$progresoActual")

                            // Verificar si ya existe un progreso para esta especie y nivel
                            val existe = progresoActual.any { it.id == especieId && it.nivel == level }

                            if (!existe) {
                                val nuevoProgreso = progreso_Niveles(
                                    nivel = level,
                                    estado = "completado",
                                    vidad_restantes = 3,
                                    id = especieId
                                )

                                val progresoActualizado = if (progresoActual.isEmpty()) {
                                    // Si no hay elementos, crear una nueva lista con el nuevo progreso
                                    mutableListOf(nuevoProgreso)
                                } else {
                                    // Si ya hay elementos, agregar al final
                                    progresoActual.apply { add(nuevoProgreso) }
                                }

                                // Guardar en Firebase
                                usuarioRepository.actualizarProgresoUsuario(usuarioEncontrado.id, progresoActualizado)

                                // Actualizar LiveData y sesión
                                val usuarioActualizado = usuarioEncontrado.copy(progreso_niveles = progresoActualizado)
                                _usuarioLiveData.value = usuarioActualizado
                                UserSession.currentUser = usuarioActualizado

                                Log.i("progresoGuardado", "${_usuarioLiveData.value}")
                            } else {
                                Log.i("progresoExistente", "El progreso ya existe, no se inserta")
                            }

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


}
