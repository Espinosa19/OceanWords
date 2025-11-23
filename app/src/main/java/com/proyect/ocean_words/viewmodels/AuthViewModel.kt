package com.proyect.ocean_words.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.repositories.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
class AuthViewModel : ViewModel() {
    val usuarioRepository = UsuarioRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _usuarioLiveData = MutableLiveData<UsuariosEstado?>()
    val usuarioLiveData: LiveData<UsuariosEstado?> = _usuarioLiveData

    private val _authState = MutableStateFlow<String?>(null)
    val authState: StateFlow<String?> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid ?: throw Exception("UID del usuario es null")

                usuarioRepository.buscarUsuarioPorId(uid).collect { usuario ->
                    Log.i("Repository","$usuario")
                    if (usuario != null) {
                        // Guardamos datos en LiveData y en UserSession
                        _usuarioLiveData.value = usuario
                        UserSession.currentUser = usuario
                        UserSession.isAuthenticated = true

                        _isAuthenticated.value = true
                        _authState.value = "Login exitoso"
                    } else {
                        _isAuthenticated.value = false
                        UserSession.isAuthenticated = false
                        _authState.value = "Usuario no encontrado"
                    }
                }

            } catch (e: Exception) {
                _authState.value = e.message
                _isAuthenticated.value = false
                UserSession.isAuthenticated = false
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        _isAuthenticated.value = true
                        _authState.value = "Login con Google exitoso"
                        // Aquí podrías buscar datos en Firestore y actualizar UserSession
                    }
                } else {
                    _authState.value = task.exception?.message
                    _isAuthenticated.value = false
                }
            }
    }

    fun registerUser(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            usuarioRepository.crearYObservarUsuario(uid, email, nombre)
                            // Guardamos datos en UserSession
                            val newUser = UsuariosEstado(email, nombre,emptyList(),uid)
                            UserSession.currentUser = newUser
                            UserSession.isAuthenticated = true
                        }
                        _authState.value = "Registro exitoso"
                        _isAuthenticated.value = true
                    } else {
                        _authState.value = task.exception?.message
                        _isAuthenticated.value = false
                        UserSession.isAuthenticated = false
                    }
                }
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = "Se ha enviado un enlace de recuperación a su correo"
                } else {
                    _authState.value = task.exception?.localizedMessage
                }
            }
    }

    fun logout() {
        auth.signOut()
        UserSession.clear()
        _usuarioLiveData.value = null
        _isAuthenticated.value = false
        _authState.value = "Sesión cerrada"
    }
}
